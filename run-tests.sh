#!/bin/bash

# QA Automation Test Runner Script
# Usage: ./run-tests.sh [options]

# Default values
TAGS="@smoke"
BROWSER="chrome"
HEADLESS="false"
THREADS="1"
ENV="local"

# Function to display usage
usage() {
    echo "Usage: $0 [options]"
    echo "Options:"
    echo "  -t, --tags TAG        Cucumber tags to run (default: @smoke)"
    echo "  -b, --browser BROWSER Browser to use (default: chrome)"
    echo "  -h, --headless        Run in headless mode"
    echo "  -p, --parallel THREADS Number of parallel threads (default: 1)"
    echo "  -e, --env ENV         Environment (default: local)"
    echo "  -g, --grid            Use Selenium Grid"
    echo "  -c, --clean           Clean target directory before running"
    echo "  --help                Display this help message"
    echo ""
    echo "Examples:"
    echo "  $0                                    # Run smoke tests with default settings"
    echo "  $0 -t @regression -b firefox         # Run regression tests with Firefox"
    echo "  $0 -t @smoke -h -p 4                 # Run smoke tests headless with 4 threads"
    echo "  $0 -t @login -g                      # Run login tests with Selenium Grid"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -t|--tags)
            TAGS="$2"
            shift 2
            ;;
        -b|--browser)
            BROWSER="$2"
            shift 2
            ;;
        -h|--headless)
            HEADLESS="true"
            shift
            ;;
        -p|--parallel)
            THREADS="$2"
            shift 2
            ;;
        -e|--env)
            ENV="$2"
            shift 2
            ;;
        -g|--grid)
            GRID_ENABLED="true"
            shift
            ;;
        -c|--clean)
            CLEAN="true"
            shift
            ;;
        --help)
            usage
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            usage
            exit 1
            ;;
    esac
done

# Display configuration
echo "=== QA Automation Test Runner ==="
echo "Tags: $TAGS"
echo "Browser: $BROWSER"
echo "Headless: $HEADLESS"
echo "Threads: $THREADS"
echo "Environment: $ENV"
echo "Selenium Grid: ${GRID_ENABLED:-false}"
echo "================================"

# Build Maven command
MAVEN_CMD="mvn"

if [[ "$CLEAN" == "true" ]]; then
    MAVEN_CMD="$MAVEN_CMD clean"
fi

MAVEN_CMD="$MAVEN_CMD test"

# Add system properties
MAVEN_CMD="$MAVEN_CMD -Dbrowser=$BROWSER"
MAVEN_CMD="$MAVEN_CMD -Dheadless=$HEADLESS"
MAVEN_CMD="$MAVEN_CMD -Dthreads=$THREADS"
MAVEN_CMD="$MAVEN_CMD -Denv=$ENV"

if [[ "$GRID_ENABLED" == "true" ]]; then
    MAVEN_CMD="$MAVEN_CMD -Dselenium.grid.enabled=true"
fi

# Add Cucumber options
MAVEN_CMD="$MAVEN_CMD -Dcucumber.options=\"--tags '$TAGS'\""

echo "Running: $MAVEN_CMD"
echo ""

# Execute Maven command
eval $MAVEN_CMD

# Check exit code
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Tests completed successfully!"
    echo "📊 Reports available in target/cucumber-reports/"
else
    echo ""
    echo "❌ Tests failed!"
    exit 1
fi
