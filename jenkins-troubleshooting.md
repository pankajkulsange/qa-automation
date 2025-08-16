# Jenkins Pipeline Troubleshooting Guide

## Common Issues and Solutions

### 1. "CreateProcess error=2, The system cannot find the file specified"

**Problem:** Jenkins is trying to execute shell commands that don't exist on Windows.

**Solutions:**
- Use `bat` instead of `sh` for Windows commands
- Use `powershell` for complex PowerShell operations
- Ensure all commands are Windows-compatible

**Example Fix:**
```groovy
// Instead of:
sh 'docker-compose up -d'

// Use:
bat 'docker-compose up -d'
```

### 2. Docker Connection Issues

**Problem:** Jenkins can't connect to Docker daemon.

**Solutions:**
1. **Enable Docker Desktop external connections:**
   - Open Docker Desktop
   - Go to Settings > General
   - Enable "Expose daemon on tcp://localhost:2375 without TLS"
   - Apply and restart Docker Desktop

2. **Check Docker is running:**
   ```powershell
   docker --version
   docker ps
   ```

3. **Test Docker connection:**
   ```powershell
   docker run hello-world
   ```

### 3. Maven Not Found

**Problem:** Jenkins can't find Maven.

**Solutions:**
1. **Install Maven globally:**
   ```powershell
   # Download Maven
   Invoke-WebRequest -Uri "https://archive.apache.org/dist/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.zip" -OutFile "maven.zip"
   
   # Extract to C:\Program Files
   Expand-Archive -Path "maven.zip" -DestinationPath "C:\Program Files"
   
   # Add to PATH
   [Environment]::SetEnvironmentVariable("PATH", $env:PATH + ";C:\Program Files\apache-maven-3.9.5\bin", "Machine")
   ```

2. **Configure Maven in Jenkins:**
   - Go to Manage Jenkins > Global Tool Configuration
   - Add Maven installation
   - Set MAVEN_HOME environment variable

### 4. Java Version Issues

**Problem:** Wrong Java version or JAVA_HOME not set.

**Solutions:**
1. **Check Java version:**
   ```powershell
   java -version
   ```

2. **Set JAVA_HOME:**
   ```powershell
   [Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-11", "Machine")
   ```

3. **Verify in Jenkins:**
   - Go to Manage Jenkins > Configure System
   - Set JAVA_HOME environment variable

### 5. Permission Issues

**Problem:** Jenkins doesn't have permission to access files or run commands.

**Solutions:**
1. **Run Jenkins as Administrator** (for testing only)
2. **Grant permissions to Jenkins user**
3. **Check file permissions on project directory**

### 6. Port Conflicts

**Problem:** Port 8080 (Jenkins) or 4444 (Selenium Grid) is already in use.

**Solutions:**
1. **Change Jenkins port:**
   ```powershell
   java -jar jenkins.war --httpPort=8081
   ```

2. **Kill process using port:**
   ```powershell
   # Find process using port 8080
   netstat -ano | findstr :8080
   
   # Kill process (replace PID with actual process ID)
   taskkill /PID <PID> /F
   ```

### 7. Selenium Grid Connection Issues

**Problem:** Tests can't connect to Selenium Grid.

**Solutions:**
1. **Check if Selenium Grid is running:**
   ```powershell
   docker ps
   ```

2. **Test Grid connection:**
   ```powershell
   Invoke-WebRequest -Uri "http://localhost:4444/wd/hub/status" -UseBasicParsing
   ```

3. **Check network connectivity:**
   ```powershell
   docker network ls
   docker network inspect qa-automation_qa-network
   ```

### 8. Pipeline Script Errors

**Problem:** Jenkinsfile syntax or logic errors.

**Solutions:**
1. **Use the simplified Jenkinsfile:**
   - Copy `Jenkinsfile.simple` content
   - This version is optimized for Windows

2. **Test pipeline syntax:**
   - Go to Jenkins > Manage Jenkins > Pipeline Syntax
   - Use the Snippet Generator to test commands

3. **Check Groovy syntax:**
   - Use an IDE with Groovy support
   - Validate syntax before committing

### 9. Report Generation Issues

**Problem:** Test reports are not generated or not accessible.

**Solutions:**
1. **Check if reports directory exists:**
   ```powershell
   dir target\cucumber-reports
   ```

2. **Verify Maven plugin configuration:**
   - Check `pom.xml` for reporting plugins
   - Ensure output directories are correct

3. **Check Jenkins plugin installation:**
   - Install HTML Publisher plugin
   - Install JUnit plugin

### 10. Environment Variable Issues

**Problem:** Environment variables not available in pipeline.

**Solutions:**
1. **Set environment variables in Jenkins:**
   - Go to Manage Jenkins > Configure System
   - Add global environment variables

2. **Set in pipeline:**
   ```groovy
   environment {
       MAVEN_OPTS = '-Xmx2048m'
       JAVA_HOME = 'C:\\Program Files\\Java\\jdk-11'
   }
   ```

## Quick Fixes

### For Immediate Testing:

1. **Use local execution (no Docker):**
   - Set `USE_DOCKER = false` in pipeline parameters
   - This avoids Docker-related issues

2. **Use simplified pipeline:**
   - Copy content from `Jenkinsfile.simple`
   - This version has fewer potential failure points

3. **Test with minimal parameters:**
   - TAGS: `@smoke`
   - BROWSER: `chrome`
   - HEADLESS: `true`
   - USE_DOCKER: `false`

### Debugging Commands:

```powershell
# Check if all required tools are available
java -version
mvn -version
docker --version
docker-compose --version

# Check if ports are available
netstat -ano | findstr :8080
netstat -ano | findstr :4444

# Test Docker connectivity
docker run hello-world

# Test Selenium Grid
Invoke-WebRequest -Uri "http://localhost:4444/wd/hub/status" -UseBasicParsing

# Check Jenkins logs
Get-Content "$env:USERPROFILE\.jenkins\jenkins.log" -Tail 50
```

## Getting Help

1. **Check Jenkins logs:**
   - Jenkins home: `%USERPROFILE%\.jenkins\`
   - Log file: `jenkins.log`

2. **Check build console output:**
   - Click on build number in Jenkins
   - View "Console Output"

3. **Verify system requirements:**
   - Java 11+
   - Maven 3.6+
   - Docker Desktop
   - Git

4. **Test components individually:**
   - Test Maven build locally first
   - Test Docker commands manually
   - Test Selenium Grid separately

## Recommended Setup Order

1. **Start with local execution** (USE_DOCKER = false)
2. **Verify Maven and Java work**
3. **Test basic pipeline functionality**
4. **Add Docker support gradually**
5. **Configure reporting and notifications**

This approach helps isolate issues and ensures each component works before adding complexity.
