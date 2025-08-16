pipeline {
    agent any
    
    parameters {
        string(name: 'BRANCH', defaultValue: 'main', description: 'Git branch to checkout')
        string(name: 'TAGS', defaultValue: '@smoke', description: 'Cucumber tags to run')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser to run tests on')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
    }
    
    environment {
        MAVEN_OPTS = '-Xmx2048m'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Project checked out successfully from branch: ${params.BRANCH}"
                bat 'dir'
            }
        }
        
        stage('Run Tests') {
            steps {
                script {
                    def cucumberOptions = "--tags '${params.TAGS}' --plugin html:target/cucumber-reports/html --plugin json:target/cucumber-reports/cucumber.json --plugin junit:target/cucumber-reports/junit.xml"
                    
                    echo "Running tests locally..."
                    bat """
                        mvn clean test ^
                            -Dbrowser=${params.BROWSER} ^
                            -Dheadless=${params.HEADLESS} ^
                            -Dcucumber.options="${cucumberOptions}" ^
                            -Dselenium.grid.enabled=false
                    """
                }
            }
        }
        
        stage('Generate Reports') {
            steps {
                bat 'mvn verify'
            }
        }
        
        stage('Archive Reports') {
            steps {
                script {
                    // Archive test reports
                    archiveArtifacts artifacts: 'target/cucumber-reports/**/*', fingerprint: true
                    archiveArtifacts artifacts: 'target/surefire-reports/**/*', fingerprint: true
                    
                    // Archive test results (JUnit XML format)
                    archiveArtifacts artifacts: 'target/cucumber-reports/junit.xml', fingerprint: true
                    
                    // Publish HTML reports
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/cucumber-reports/html',
                        reportFiles: 'index.html',
                        reportName: 'Cucumber HTML Report',
                        reportTitles: 'Cucumber Test Results'
                    ])
                }
            }
        }
    }
    
    post {
        success {
            echo '✅ Build successful!'
            echo '📊 Reports available in target/cucumber-reports/'
        }
        
        failure {
            echo '❌ Build failed!'
            echo 'Check the console output for details.'
        }
        
        unstable {
            echo '⚠️ Build unstable!'
            echo 'Some tests failed but build completed.'
        }
    }
}
