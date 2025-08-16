# QA Automation Framework

A complete, runnable QA automation project built with Java 21, Maven, Selenium 4, Cucumber, TestNG, and Docker.

## 🚀 Features

- **Java 21** with Maven build system
- **Selenium 4** for web automation
- **Cucumber** with TestNG for BDD testing
- **Docker Compose** for containerized execution
- **Selenium Standalone Chrome** for browser automation
- **Jenkins declarative pipeline** with manual triggers
- **Comprehensive reporting** (HTML, JSON, JUnit XML)
- **Parallel test execution** support
- **Configuration management** via properties and environment variables

## 📋 Prerequisites

- Java 21 JDK
- Maven 3.9+
- Docker and Docker Compose
- Jenkins (for CI/CD pipeline)

## 🛠️ Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd qa-automation
```

### 2. Configuration
The framework uses `src/main/resources/config.properties` for configuration:

```properties
# Application Configuration
app.url=https://www.saucedemo.com
app.username=standard_user
app.password=secret_sauce

# Browser Configuration
browser.default=chrome
browser.headless=false
```

### 3. Local Development

#### Run Tests Locally
```bash
# Run all tests
mvn clean test

# Run with specific tags
mvn clean test -Dcucumber.options="--tags @smoke"

# Run with specific browser
mvn clean test -Dbrowser=firefox

# Run in headless mode
mvn clean test -Dheadless=true

# Run with parallel execution
mvn clean test -Dthreads=4
```

#### Run with Docker Compose
```bash
# Start Selenium Grid and run tests
docker-compose up

# Run only Selenium Grid (for manual testing)
docker-compose up selenium-hub chrome firefox

# Run tests in Maven container
docker-compose run maven mvn clean test
```

### 4. Jenkins Pipeline

The project includes a Jenkins declarative pipeline (`Jenkinsfile`) with the following parameters:

- **BRANCH**: Git branch to checkout (default: main)
- **TAGS**: Cucumber tags to run (default: @smoke)
- **BROWSER**: Browser to run tests on (chrome, firefox, edge)
- **ENV**: Environment to test against (local, staging, production)
- **THREADS**: Number of parallel threads (default: 1)
- **HEADLESS**: Run browser in headless mode (default: true)

#### Jenkins Setup
1. Create a new Jenkins pipeline job
2. Point to the `Jenkinsfile` in your repository
3. Configure the job to build with parameters
4. Run the pipeline manually with your desired parameters

## 📁 Project Structure

```
qa-automation/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── pages/           # Page Object Models
│   │   │   └── utils/           # Utility classes
│   │   └── resources/
│   │       ├── config.properties # Configuration
│   │       └── logback.xml      # Logging configuration
│   └── test/
│       ├── java/
│       │   ├── runners/         # Test runners
│       │   └── steps/           # Cucumber step definitions
│       └── resources/
│           └── features/        # Cucumber feature files
├── target/                      # Build output and reports
├── docker-compose.yml          # Docker services configuration
├── Jenkinsfile                 # Jenkins pipeline
├── pom.xml                     # Maven configuration
├── testng.xml                  # TestNG configuration
└── README.md                   # This file
```

## 🧪 Test Scenarios

### Login Functionality
- ✅ Successful login with valid credentials
- ✅ Login with invalid credentials
- ✅ Login with empty fields
- ✅ Login with locked out user
- ✅ Login with problem user
- ✅ Login with performance glitch user

### Inventory Page
- ✅ Verify inventory page elements
- ✅ Add items to cart
- ✅ View shopping cart
- ✅ Logout functionality

## 📊 Reports

After test execution, reports are generated in the `target/` directory:

- **HTML Reports**: `target/cucumber-reports/html/`
- **JSON Reports**: `target/cucumber-reports/cucumber.json`
- **JUnit XML**: `target/cucumber-reports/junit.xml`
- **Test Logs**: `target/test.log`

## 🔧 Configuration Options

### System Properties
```bash
-Dbrowser=chrome          # Browser type
-Dheadless=true          # Headless mode
-Denv=staging            # Environment
-Dthreads=4              # Parallel threads
-Dselenium.grid.enabled=true  # Use Selenium Grid
-Dselenium.grid.url=http://localhost:4444/wd/hub
```

### Environment Variables
```bash
export BROWSER=firefox
export HEADLESS=true
export ENV=production
export THREADS=2
export SELENIUM_GRID_ENABLED=true
```

## 🐳 Docker Services

The `docker-compose.yml` includes:

- **Maven Container**: Runs tests with Java 21 and Maven
- **Selenium Hub**: Central hub for browser management
- **Chrome Node**: Chrome browser container
- **Firefox Node**: Firefox browser container

## 🚀 Quick Start

1. **Local Testing**:
   ```bash
   mvn clean test -Dcucumber.options="--tags @smoke"
   ```

2. **Docker Testing**:
   ```bash
   docker-compose up
   ```

3. **Jenkins Pipeline**:
   - Trigger pipeline with parameters
   - Select tags: `@smoke`
   - Choose browser: `chrome`
   - Set threads: `2`
   - Enable headless: `true`

## 📝 Cucumber Tags

- `@smoke`: Smoke tests (critical functionality)
- `@regression`: Regression tests (comprehensive testing)
- `@login`: Login-related tests
- `@inventory`: Inventory page tests
- `@ui`: UI element verification tests

## 🔍 Troubleshooting

### Common Issues

1. **WebDriver Issues**:
   - Ensure WebDriverManager dependencies are resolved
   - Check browser compatibility

2. **Docker Issues**:
   - Verify Docker and Docker Compose are running
   - Check port availability (4444, 4442, 4443)

3. **Jenkins Issues**:
   - Ensure Jenkins has Docker access
   - Check workspace permissions

### Logs
- Check `target/test.log` for detailed test execution logs
- Review Docker container logs: `docker-compose logs`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Add your changes
4. Run tests locally
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
