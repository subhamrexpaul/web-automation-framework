pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-11'
    }

    environment {
        BROWSER = 'chrome'
        HEADLESS = 'true'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Checked out branch: ${env.BRANCH_NAME}"
            }
        }

        stage('Build') {
            steps {
                echo 'Compiling project...'
                bat 'mvn clean compile -DskipTests'
            }
        }

        stage('Smoke Tests') {
            steps {
                echo 'Running smoke tests...'
                bat "mvn test -Dbrowser=${BROWSER} -Dheadless=${HEADLESS} -DsuiteXmlFile=src/test/resources/smoke-suite.xml"
            }
            post {
                always {
                    // Archive TestNG results
                    testNG(reportFilenamePattern: '**/testng-results.xml')
                }
            }
        }

        stage('Regression Tests') {
            when {
                branch 'main'
            }
            steps {
                echo 'Running full regression suite...'
                bat "mvn test -Dbrowser=${BROWSER} -Dheadless=${HEADLESS} -DsuiteXmlFile=src/test/resources/regression-suite.xml"
            }
        }
    }

    post {
        always {
            echo 'Archiving test artifacts...'

            // Archive ExtentReports HTML report
            archiveArtifacts artifacts: 'reports/**/*.html', allowEmptyArchive: true

            // Archive screenshots
            archiveArtifacts artifacts: 'screenshots/**/*.png', allowEmptyArchive: true

            // Archive TestNG output
            archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true

            // Publish HTML report
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'AutomationReport.html',
                reportName: 'Automation Test Report'
            ])
        }

        success {
            echo 'All tests passed successfully!'
        }

        failure {
            echo 'Tests failed! Check the report for details.'
            // Uncomment below for email notification:
            // mail to: 'team@company.com',
            //      subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            //      body: "Check console output at ${env.BUILD_URL}"
        }

        cleanup {
            cleanWs()
        }
    }
}
