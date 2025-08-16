# Jenkins Configuration Script
# Run this after Jenkins is started and initial setup is complete

Write-Host "=== Jenkins Configuration ===" -ForegroundColor Green

# Wait for Jenkins to be ready
Write-Host "Waiting for Jenkins to be ready..."
do {
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8080" -UseBasicParsing -TimeoutSec 5
        if ($response.StatusCode -eq 200) {
            Write-Host "✅ Jenkins is ready!" -ForegroundColor Green
            break
        }
    } catch {
        Write-Host "Jenkins not ready yet, waiting..." -ForegroundColor Yellow
        Start-Sleep -Seconds 10
    }
} while ($true)

Write-Host ""
Write-Host "=== Jenkins Setup Instructions ===" -ForegroundColor Cyan
Write-Host "1. Open browser: http://localhost:8080"
Write-Host "2. Get initial admin password from: $env:USERPROFILE\.jenkins\secrets\initialAdminPassword"
Write-Host "3. Install suggested plugins"
Write-Host "4. Create admin user (username: admin, password: admin123)"
Write-Host "5. Configure Jenkins URL: http://localhost:8080"
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
Write-Host "4. Set repository URL: file:///D:\projects\first-project\qa-automation"
Write-Host ""
Write-Host "Configuration complete! Follow the setup guide for detailed steps."
