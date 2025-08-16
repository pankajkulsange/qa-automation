# Jenkins Setup Script for QA Automation Project
# Run this script as Administrator

param(
    [string]$JenkinsPort = "8080",
    [string]$JenkinsHome = "$env:USERPROFILE\.jenkins",
    [string]$ProjectPath = "D:\projects\first-project\qa-automation"
)

Write-Host "=== Jenkins Setup for QA Automation Project ===" -ForegroundColor Green
Write-Host ""

# Check if running as Administrator
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")
if (-not $isAdmin) {
    Write-Host "Warning: This script should be run as Administrator for best results" -ForegroundColor Yellow
}

# Check prerequisites
Write-Host "Checking prerequisites..." -ForegroundColor Cyan

# Check Java
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    if ($javaVersion) {
        Write-Host "✅ Java found: $javaVersion" -ForegroundColor Green
    } else {
        Write-Host "❌ Java not found. Please install Java 11 or higher" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ Java not found. Please install Java 11 or higher" -ForegroundColor Red
    exit 1
}

# Check Docker
try {
    $dockerVersion = docker --version
    if ($dockerVersion) {
        Write-Host "✅ Docker found: $dockerVersion" -ForegroundColor Green
    } else {
        Write-Host "❌ Docker not found. Please install Docker Desktop" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ Docker not found. Please install Docker Desktop" -ForegroundColor Red
    exit 1
}

# Check Docker Compose
try {
    $dockerComposeVersion = docker-compose --version
    if ($dockerComposeVersion) {
        Write-Host "✅ Docker Compose found: $dockerComposeVersion" -ForegroundColor Green
    } else {
        Write-Host "❌ Docker Compose not found. Please install Docker Compose" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ Docker Compose not found. Please install Docker Compose" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Create Jenkins directory
$jenkinsDir = "jenkins"
if (-not (Test-Path $jenkinsDir)) {
    Write-Host "Creating Jenkins directory..." -ForegroundColor Cyan
    New-Item -ItemType Directory -Path $jenkinsDir | Out-Null
}

# Download Jenkins WAR file
$jenkinsWar = "$jenkinsDir\jenkins.war"
if (-not (Test-Path $jenkinsWar)) {
    Write-Host "Downloading Jenkins WAR file..." -ForegroundColor Cyan
    try {
        Invoke-WebRequest -Uri "https://get.jenkins.io/war-stable/latest/jenkins.war" -OutFile $jenkinsWar
        Write-Host "✅ Jenkins WAR file downloaded successfully" -ForegroundColor Green
    } catch {
        Write-Host "❌ Failed to download Jenkins WAR file" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "✅ Jenkins WAR file already exists" -ForegroundColor Green
}

# Create Jenkins startup script
$startScript = "$jenkinsDir\start-jenkins.bat"
$startScriptContent = @"
@echo off
echo Starting Jenkins on port $JenkinsPort...
echo Jenkins will be available at: http://localhost:$JenkinsPort
echo.
echo To stop Jenkins, press Ctrl+C
echo.
java -jar jenkins.war --httpPort=$JenkinsPort --prefix=/jenkins
"@

Set-Content -Path $startScript -Value $startScriptContent
Write-Host "✅ Jenkins startup script created: $startScript" -ForegroundColor Green

# Create Jenkins stop script
$stopScript = "$jenkinsDir\stop-jenkins.bat"
$stopScriptContent = @"
@echo off
echo Stopping Jenkins...
taskkill /F /IM java.exe /FI "WINDOWTITLE eq jenkins*" 2>nul
echo Jenkins stopped.
"@

Set-Content -Path $stopScript -Value $stopScriptContent
Write-Host "✅ Jenkins stop script created: $stopScript" -ForegroundColor Green

# Create Jenkins configuration script
$configScript = "$jenkinsDir\configure-jenkins.ps1"
$configScriptContent = @"
# Jenkins Configuration Script
# Run this after Jenkins is started and initial setup is complete

Write-Host "=== Jenkins Configuration ===" -ForegroundColor Green

# Wait for Jenkins to be ready
Write-Host "Waiting for Jenkins to be ready..."
do {
    try {
        `$response = Invoke-WebRequest -Uri "http://localhost:$JenkinsPort" -UseBasicParsing -TimeoutSec 5
        if (`$response.StatusCode -eq 200) {
            Write-Host "✅ Jenkins is ready!" -ForegroundColor Green
            break
        }
    } catch {
        Write-Host "Jenkins not ready yet, waiting..." -ForegroundColor Yellow
        Start-Sleep -Seconds 10
    }
} while (`$true)

Write-Host ""
Write-Host "=== Jenkins Setup Instructions ===" -ForegroundColor Cyan
Write-Host "1. Open browser: http://localhost:$JenkinsPort"
Write-Host "2. Get initial admin password from: `$env:USERPROFILE\.jenkins\secrets\initialAdminPassword"
Write-Host "3. Install suggested plugins"
Write-Host "4. Create admin user (username: admin, password: admin123)"
Write-Host "5. Configure Jenkins URL: http://localhost:$JenkinsPort"
Write-Host ""
Write-Host "=== Required Plugins to Install ===" -ForegroundColor Cyan
Write-Host "- Docker Pipeline"
Write-Host "- Docker plugin"
Write-Host "- Pipeline"
Write-Host "- Git"
Write-Host "- JUnit"
Write-Host "- HTML Publisher"
Write-Host "- Parameterized Trigger"
Write-Host "- Credentials Binding"
Write-Host ""
Write-Host "=== Pipeline Configuration ===" -ForegroundColor Cyan
Write-Host "1. Create new Pipeline job: QA-Automation-Pipeline"
Write-Host "2. Configure parameters as per jenkins-setup.md"
Write-Host "3. Use Jenkinsfile from project root"
Write-Host "4. Set repository URL: file:///$ProjectPath"
Write-Host ""
Write-Host "Configuration complete! Follow the setup guide for detailed steps."
"@

Set-Content -Path $configScript -Value $configScriptContent
Write-Host "✅ Jenkins configuration script created: $configScript" -ForegroundColor Green

# Create environment setup script
$envScript = "$jenkinsDir\setup-environment.ps1"
$envScriptContent = @"
# Environment Setup Script
# Run this to set up environment variables and Docker configuration

Write-Host "=== Environment Setup ===" -ForegroundColor Green

# Set environment variables
Write-Host "Setting environment variables..." -ForegroundColor Cyan
[Environment]::SetEnvironmentVariable("MAVEN_OPTS", "-Xmx2048m", "Machine")
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-11", "Machine")

# Enable Docker daemon for external connections (Windows)
Write-Host "Configuring Docker for external connections..." -ForegroundColor Cyan
Write-Host "Note: You may need to manually configure Docker Desktop settings:"
Write-Host "1. Open Docker Desktop"
Write-Host "2. Go to Settings > General"
Write-Host "3. Enable 'Expose daemon on tcp://localhost:2375 without TLS'"
Write-Host "4. Apply and restart Docker Desktop"

# Create Docker network for Jenkins
Write-Host "Creating Docker network for Jenkins..." -ForegroundColor Cyan
docker network create jenkins-network 2>`$null

Write-Host "✅ Environment setup complete!" -ForegroundColor Green
Write-Host "Please restart your terminal to apply environment variables."
"@

Set-Content -Path $envScript -Value $envScriptContent
Write-Host "✅ Environment setup script created: $envScript" -ForegroundColor Green

# Create README for Jenkins directory
$jenkinsReadme = "$jenkinsDir\README.md"
$jenkinsReadmeContent = @"
# Jenkins Setup for QA Automation Project

## Quick Start

1. **Start Jenkins:**
   ```cmd
   start-jenkins.bat
   ```

2. **Configure Jenkins:**
   ```powershell
   .\configure-jenkins.ps1
   ```

3. **Setup Environment:**
   ```powershell
   .\setup-environment.ps1
   ```

4. **Stop Jenkins:**
   ```cmd
   stop-jenkins.bat
   ```

## Access Jenkins

- **URL:** http://localhost:$JenkinsPort
- **Initial Password:** Check `%USERPROFILE%\.jenkins\secrets\initialAdminPassword`

## Files

- `jenkins.war` - Jenkins application
- `start-jenkins.bat` - Start Jenkins server
- `stop-jenkins.bat` - Stop Jenkins server
- `configure-jenkins.ps1` - Configuration guide
- `setup-environment.ps1` - Environment setup

## Troubleshooting

1. **Port already in use:**
   - Change port in start-jenkins.bat
   - Or kill process using port: `netstat -ano | findstr :$JenkinsPort`

2. **Docker connection issues:**
   - Enable Docker daemon for external connections
   - Check Docker Desktop settings

3. **Permission issues:**
   - Run scripts as Administrator
   - Check file permissions

## Next Steps

1. Follow the main setup guide: `..\jenkins-setup.md`
2. Configure Jenkins pipeline job
3. Test the automation framework
4. Set up notifications and monitoring
"@

Set-Content -Path $jenkinsReadme -Value $jenkinsReadmeContent
Write-Host "✅ Jenkins README created: $jenkinsReadme" -ForegroundColor Green

Write-Host ""
Write-Host "=== Setup Complete! ===" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. Navigate to jenkins directory: cd jenkins"
Write-Host "2. Start Jenkins: .\start-jenkins.bat"
Write-Host "3. Configure Jenkins: .\configure-jenkins.ps1"
Write-Host "4. Setup environment: .\setup-environment.ps1"
Write-Host ""
Write-Host "For detailed instructions, see: jenkins-setup.md" -ForegroundColor Yellow
Write-Host ""
Write-Host "Jenkins will be available at: http://localhost:$JenkinsPort" -ForegroundColor Green
