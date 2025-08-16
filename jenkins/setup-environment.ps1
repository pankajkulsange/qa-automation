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
docker network create jenkins-network 2>$null

Write-Host "✅ Environment setup complete!" -ForegroundColor Green
Write-Host "Please restart your terminal to apply environment variables."
