# Jenkins Setup Guide for QA Automation Project

## Prerequisites
- Java 21 (already installed)
- Docker and Docker Compose (already installed)
- Git (for version control)

## Step 1: Install Jenkins

### Option 1: Using Jenkins WAR file (Recommended for local development)

1. **Download Jenkins WAR file:**
   ```powershell
   # Create jenkins directory
   mkdir jenkins
   cd jenkins
   
   # Download Jenkins WAR file
   Invoke-WebRequest -Uri "https://get.jenkins.io/war-stable/latest/jenkins.war" -OutFile "jenkins.war"
   ```

2. **Start Jenkins:**
   ```powershell
   # Start Jenkins on port 8080
   java -jar jenkins.war --httpPort=8080
   ```

3. **Access Jenkins:**
   - Open browser: http://localhost:8080
   - Get initial admin password from console output or from: `%USERPROFILE%\.jenkins\secrets\initialAdminPassword`

### Option 2: Using Docker (Alternative)

```powershell
# Run Jenkins in Docker
docker run -d -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts
```

## Step 2: Initial Jenkins Setup

1. **Unlock Jenkins:**
   - Enter the initial admin password from the console output

2. **Install Suggested Plugins:**
   - Click "Install suggested plugins"
   - Wait for installation to complete

3. **Create Admin User:**
   - Username: `admin`
   - Password: `admin123` (or your preferred password)
   - Full name: `Administrator`
   - Email: `admin@example.com`

4. **Configure Jenkins URL:**
   - URL: `http://localhost:8080`

## Step 3: Install Required Plugins

Go to **Manage Jenkins** > **Manage Plugins** > **Available** and install:

### Essential Plugins:
- **Docker Pipeline** - For Docker integration
- **Docker plugin** - Docker support
- **Pipeline** - Pipeline support
- **Git** - Git integration
- **GitHub** - GitHub integration (if using GitHub)
- **JUnit** - Test reporting
- **HTML Publisher** - HTML report publishing
- **Parameterized Trigger** - Parameter support
- **Credentials Binding** - Credentials management

### Optional Plugins:
- **Blue Ocean** - Modern UI for pipelines
- **Timestamper** - Timestamp in console output
- **Workspace Cleanup** - Clean workspace after build

## Step 4: Configure Docker Integration

1. **Install Docker on Jenkins:**
   - Go to **Manage Jenkins** > **Manage Nodes and Clouds**
   - Click **Configure Clouds**
   - Add **Docker** cloud
   - Configure Docker host: `tcp://localhost:2375` or `unix:///var/run/docker.sock`

2. **Configure Docker Credentials:**
   - Go to **Manage Jenkins** > **Manage Credentials**
   - Add Docker registry credentials if needed

## Step 5: Create Jenkins Pipeline Job

### Method 1: Using Jenkinsfile (Recommended)

1. **Create New Pipeline Job:**
   - Click **New Item**
   - Enter name: `QA-Automation-Pipeline`
   - Select **Pipeline**
   - Click **OK**

2. **Configure Pipeline:**
   - **General** tab:
     - ✅ **This project is parameterized**
     - Add parameters:
       - **String Parameter**: `BRANCH` (default: `main`)
       - **String Parameter**: `TAGS` (default: `@smoke`)
       - **Choice Parameter**: `BROWSER` (choices: `chrome,firefox,edge`)
       - **Choice Parameter**: `ENV` (choices: `local,staging,production`)
       - **String Parameter**: `THREADS` (default: `1`)
       - **Boolean Parameter**: `HEADLESS` (default: `true`)

   - **Pipeline** tab:
     - **Definition**: Pipeline script from SCM
     - **SCM**: Git
     - **Repository URL**: `file:///D:/projects/first-project/qa-automation`
     - **Branch Specifier**: `*/${BRANCH}`
     - **Script Path**: `Jenkinsfile`

3. **Save Configuration**

### Method 2: Direct Pipeline Script

1. **Create New Pipeline Job:**
   - Click **New Item**
   - Enter name: `QA-Automation-Pipeline`
   - Select **Pipeline**
   - Click **OK**

2. **Configure Pipeline:**
   - **General** tab: Same as Method 1
   - **Pipeline** tab:
     - **Definition**: Pipeline script
     - Copy the content from `Jenkinsfile` into the script area

## Step 6: Configure Global Tools

Go to **Manage Jenkins** > **Global Tool Configuration**:

1. **Maven Configuration:**
   - Name: `Maven-3.9.5`
   - Install automatically: ✅
   - Version: `3.9.5`

2. **JDK Configuration:**
   - Name: `JDK-11`
   - JAVA_HOME: `C:\Program Files\Java\jdk-11` (adjust path as needed)

## Step 7: Configure System Settings

Go to **Manage Jenkins** > **Configure System**:

1. **Global Properties:**
   - ✅ **Environment variables**
   - Add:
     - `MAVEN_OPTS`: `-Xmx2048m`
     - `JAVA_HOME`: `C:\Program Files\Java\jdk-11`

2. **Docker Settings:**
   - Docker host: `tcp://localhost:2375` or `unix:///var/run/docker.sock`

## Step 8: Test the Pipeline

1. **Build with Parameters:**
   - Click **Build with Parameters**
   - Set parameters:
     - BRANCH: `main`
     - TAGS: `@smoke`
     - BROWSER: `chrome`
     - ENV: `local`
     - THREADS: `1`
     - HEADLESS: `true`

2. **Monitor Build:**
   - Click on the build number
   - View **Console Output** for real-time logs

## Step 9: Access Test Reports

After successful build:

1. **Cucumber Reports:**
   - Click on build number
   - Click **Cucumber Reports** (if plugin installed)

2. **HTML Reports:**
   - Click on build number
   - Click **HTML Report** (if configured)

3. **Test Results:**
   - Click on build number
   - Click **Test Results**

## Troubleshooting

### Common Issues:

1. **Docker Connection Issues:**
   ```powershell
   # Enable Docker daemon for external connections
   # Edit Docker Desktop settings or configure Docker daemon
   ```

2. **Permission Issues:**
   ```powershell
   # Run Jenkins with appropriate permissions
   # Ensure Docker access for Jenkins user
   ```

3. **Port Conflicts:**
   ```powershell
   # Change Jenkins port if 8080 is busy
   java -jar jenkins.war --httpPort=8081
   ```

4. **Maven Issues:**
   - Ensure Maven is properly configured
   - Check JAVA_HOME environment variable

### Useful Commands:

```powershell
# Start Jenkins
java -jar jenkins.war --httpPort=8080

# Stop Jenkins
# Press Ctrl+C in the console

# View Jenkins logs
Get-Content "%USERPROFILE%\.jenkins\jenkins.log"

# Clean Jenkins workspace
Remove-Item "%USERPROFILE%\.jenkins\workspace\*" -Recurse -Force
```

## Next Steps

1. **Configure Webhooks** (if using GitHub/GitLab)
2. **Set up Email Notifications**
3. **Configure Build Triggers**
4. **Set up Backup Strategy**
5. **Configure Security Settings**

## Security Considerations

1. **Change default admin password**
2. **Configure user roles and permissions**
3. **Enable CSRF protection**
4. **Configure authentication (LDAP, etc.)**
5. **Regular security updates**

## Support

For issues:
1. Check Jenkins logs
2. Review pipeline console output
3. Verify Docker and Maven configurations
4. Check network connectivity
5. Review Jenkins documentation
