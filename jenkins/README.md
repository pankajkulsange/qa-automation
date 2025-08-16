# Jenkins Setup for QA Automation Project

## Quick Start

1. **Start Jenkins:**
   `cmd
   start-jenkins.bat
   `

2. **Configure Jenkins:**
   `powershell
   .\configure-jenkins.ps1
   `

3. **Setup Environment:**
   `powershell
   .\setup-environment.ps1
   `

4. **Stop Jenkins:**
   `cmd
   stop-jenkins.bat
   `

## Access Jenkins

- **URL:** http://localhost:8080
- **Initial Password:** Check %USERPROFILE%\.jenkins\secrets\initialAdminPassword

## Files

- jenkins.war - Jenkins application
- start-jenkins.bat - Start Jenkins server
- stop-jenkins.bat - Stop Jenkins server
- configure-jenkins.ps1 - Configuration guide
- setup-environment.ps1 - Environment setup

## Troubleshooting

1. **Port already in use:**
   - Change port in start-jenkins.bat
   - Or kill process using port: 
etstat -ano | findstr :8080

2. **Docker connection issues:**
   - Enable Docker daemon for external connections
   - Check Docker Desktop settings

3. **Permission issues:**
   - Run scripts as Administrator
   - Check file permissions

## Next Steps

1. Follow the main setup guide: ..\jenkins-setup.md
2. Configure Jenkins pipeline job
3. Test the automation framework
4. Set up notifications and monitoring
