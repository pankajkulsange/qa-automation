pipeline {
    agent any
    
    parameters {
        string(name: 'BRANCH', defaultValue: 'main', description: 'Git branch to checkout')
        string(name: 'TAGS', defaultValue: '@smoke', description: 'Cucumber tags to run (e.g., @smoke, @regression)')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser to run tests on')
        choice(name: 'ENV', choices: ['local', 'staging', 'production'], description: 'Environment to test against')
        string(name: 'THREADS', defaultValue: '1', description: 'Number of parallel threads')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
    }
    
    environment {
        MAVEN_OPTS = '-Xmx2048m'
        SELENIUM_GRID_URL = 'http://selenium-hub:4444/wd/hub'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Setup Selenium Grid') {
            steps {
                script {
                    // Start Selenium Grid containers
                    sh '''
                        docker-compose up -d selenium-hub chrome firefox
                        echo "Waiting for Selenium Grid to be ready..."
                        while ! curl -s http://localhost:4444/wd/hub/status | grep -q '"ready":true'; do
                            sleep 5
                        done
                        echo "Selenium Grid is ready!"
                    '''
                }
            }
        }
        
        stage('Run Tests') {
            steps {
                script {
                    // Set Cucumber options based on parameters
                    def cucumberOptions = "--tags '${params.TAGS}' --plugin html:target/cucumber-reports/html --plugin json:target/cucumber-reports/cucumber.json --plugin junit:target/cucumber-reports/junit.xml"
                    
                    // Run tests in Maven container
                                         sh '''
                         docker run --rm \
                             --network qa-automation_qa-network \
                             -v ${WORKSPACE}:/app \
                             -v maven-repo:/root/.m2 \
                             -e MAVEN_OPTS="-Xmx2048m" \
                             -e SELENIUM_GRID_URL="http://selenium-hub:4444/wd/hub" \
                             maven:3.9.5-openjdk-11 \
                            sh -c "
                                cd /app &&
                                mvn clean test \
                                    -Dbrowser=${BROWSER} \
                                    -Denv=${ENV} \
                                    -Dheadless=${HEADLESS} \
                                    -Dthreads=${THREADS} \
                                    -Dcucumber.options='${cucumberOptions}' \
                                    -Dselenium.grid.url=${SELENIUM_GRID_URL} \
                                    -Dselenium.grid.enabled=true
                            "
                    '''
                }
            }
        }
        
        stage('Generate Reports') {
            steps {
                script {
                    // Generate additional reports if needed
                                         sh '''
                         docker run --rm \
                             --network qa-automation_qa-network \
                             -v ${WORKSPACE}:/app \
                             -v maven-repo:/root/.m2 \
                             maven:3.9.5-openjdk-11 \
                            sh -c "
                                cd /app &&
                                mvn verify
                            "
                    '''
                }
            }
        }
        
        stage('Archive Reports') {
            steps {
                // Archive test reports
                archiveArtifacts artifacts: 'target/cucumber-reports/**/*', fingerprint: true
                archiveArtifacts artifacts: 'target/surefire-reports/**/*', fingerprint: true
                
                // Publish test results
                publishTestResults testResultsPattern: 'target/cucumber-reports/junit.xml'
            }
        }
        
        stage('Cleanup') {
            steps {
                script {
                    // Stop Selenium Grid containers
                    sh 'docker-compose down'
                }
            }
        }
    }
    
    post {
        always {
            // Always cleanup containers
            script {
                sh 'docker-compose down || true'
            }
            
            // Publish test results even if build fails
            publishTestResults testResultsPattern: 'target/cucumber-reports/junit.xml'
        }
        
        success {
            echo 'Build successful!'
        }
        
        failure {
            echo 'Build failed!'
        }
        
        unstable {
            echo 'Build unstable!'
        }
    }
}
