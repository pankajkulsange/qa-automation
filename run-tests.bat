@echo off
setlocal enabledelayedexpansion

REM QA Automation Test Runner Script for Windows
REM Usage: run-tests.bat [options]

REM Default values
set TAGS=@smoke
set BROWSER=chrome
set HEADLESS=false
set THREADS=1
set ENV=local
set GRID_ENABLED=false
set CLEAN=false

REM Parse command line arguments
:parse_args
if "%~1"=="" goto :end_parse
if "%~1"=="-t" (
    set TAGS=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--tags" (
    set TAGS=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="-b" (
    set BROWSER=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--browser" (
    set BROWSER=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="-h" (
    set HEADLESS=true
    shift
    goto :parse_args
)
if "%~1"=="--headless" (
    set HEADLESS=true
    shift
    goto :parse_args
)
if "%~1"=="-p" (
    set THREADS=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--parallel" (
    set THREADS=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="-e" (
    set ENV=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--env" (
    set ENV=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="-g" (
    set GRID_ENABLED=true
    shift
    goto :parse_args
)
if "%~1"=="--grid" (
    set GRID_ENABLED=true
    shift
    goto :parse_args
)
if "%~1"=="-c" (
    set CLEAN=true
    shift
    goto :parse_args
)
if "%~1"=="--clean" (
    set CLEAN=true
    shift
    goto :parse_args
)
if "%~1"=="--help" (
    goto :usage
)
echo Unknown option: %~1
goto :usage

:end_parse

REM Display configuration
echo === QA Automation Test Runner ===
echo Tags: %TAGS%
echo Browser: %BROWSER%
echo Headless: %HEADLESS%
echo Threads: %THREADS%
echo Environment: %ENV%
echo Selenium Grid: %GRID_ENABLED%
echo ================================

REM Build Maven command
set MAVEN_CMD=mvn

if "%CLEAN%"=="true" (
    set MAVEN_CMD=%MAVEN_CMD% clean
)

set MAVEN_CMD=%MAVEN_CMD% test

REM Add system properties
set MAVEN_CMD=%MAVEN_CMD% -Dbrowser=%BROWSER%
set MAVEN_CMD=%MAVEN_CMD% -Dheadless=%HEADLESS%
set MAVEN_CMD=%MAVEN_CMD% -Dthreads=%THREADS%
set MAVEN_CMD=%MAVEN_CMD% -Denv=%ENV%

if "%GRID_ENABLED%"=="true" (
    set MAVEN_CMD=%MAVEN_CMD% -Dselenium.grid.enabled=true
)

REM Add Cucumber options
set MAVEN_CMD=%MAVEN_CMD% -Dcucumber.options="--tags '%TAGS%'"

echo Running: %MAVEN_CMD%
echo.

REM Execute Maven command
%MAVEN_CMD%

REM Check exit code
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ Tests completed successfully!
    echo 📊 Reports available in target/cucumber-reports/
) else (
    echo.
    echo ❌ Tests failed!
    exit /b 1
)

goto :end

:usage
echo Usage: %0 [options]
echo Options:
echo   -t, --tags TAG        Cucumber tags to run (default: @smoke)
echo   -b, --browser BROWSER Browser to use (default: chrome)
echo   -h, --headless        Run in headless mode
echo   -p, --parallel THREADS Number of parallel threads (default: 1)
echo   -e, --env ENV         Environment (default: local)
echo   -g, --grid            Use Selenium Grid
echo   -c, --clean           Clean target directory before running
echo   --help                Display this help message
echo.
echo Examples:
echo   %0                                    # Run smoke tests with default settings
echo   %0 -t @regression -b firefox         # Run regression tests with Firefox
echo   %0 -t @smoke -h -p 4                 # Run smoke tests headless with 4 threads
echo   %0 -t @login -g                      # Run login tests with Selenium Grid

:end
