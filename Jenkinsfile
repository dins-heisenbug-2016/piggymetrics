def builds = [:]
def componentTests = [:]
def projects = [
        'account-service',
        'auth-service',
        'config',
        'gateway',
        'monitoring',
        'notification-service',
        'registry',
        'statistics-service'
]
def unitTestsFailed = false
def componentTestsFailed = false

def gradleBuilder

for (int i = 0; i < projects.size(); i++) {
    def project = projects[i]

    builds["Build ${project}"] = {
        stage("Build ${project}") {
            try {
                gradleBuilder.inside() {
                    sh "gradle --project-cache-dir=${pwd()}/${project}/.gradle ${project}:build --info"
                }
            } catch (Exception ex) {
                unitTestsFailed = true
            }
        }
    }

    componentTests["Component tests for ${project}"] = {
        docker.image('java:8').inside() {
            sh "echo \"Component tests for ${project}\""
        }
    }
}

node {
    ws("${pwd()}/${java.util.UUID.randomUUID()}") {
            stage('Checkout') {
                checkout scm
                stash name: 'sources'
            }

            gradleBuilder = docker.build('gradle_builder', 'jenkins/gradle-builder')

            stage('Build') {
                parallel builds
            }

            stage("Push images") {
                sh "echo Push images"
            }

            stage('Component tests') {
                try {
                    gradleBuilder.inside() {
                        sh "gradle --project-cache-dir=${pwd()}/account-service-component-tests/.gradle account-service-component-tests:test --info"
                    }
                } catch (Exception ex) {
                    componentTestsFailed = true
                }
            }

            stage('Integration tests') {
                sh "echo \"Integration tests\""
            }

            stage('Publish test reports') {
                sh "[ -d ${pwd()}/build/allure-results ] || mkdir -p ${pwd()}/build/allure-results"
                for (int i = 0; i < projects.size(); i++) {
                    def project = projects[i]
                    sh "([ -d ${pwd()}/${project}/build/allure-results ] && cp -r ${pwd()}/${project}/build/allure-results/. ${pwd()}/build/allure-results) || echo \"Allure results not found for ${project}\""
                }

                sh "([ -d ${pwd()}/account-service-component-tests/build/allure-results ] && cp -r ${pwd()}/account-service-component-tests/build/allure-results/. ${pwd()}/build/allure-results) || echo \"Allure results not found for account-service-component-tests\""

                def allureBuilder = docker.build('allure_builder', 'jenkins/allure-builder')
                allureBuilder.inside() {
                    sh "allure generate ${pwd()}/build/allure-results -o ${pwd()}/build/allure-reports"
                }

                publishHTML (target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: "${pwd()}/build/allure-reports",
                        reportFiles: 'index.html',
                        reportName: "Allure Report"
                ])

                junit allowEmptyResults: true, testResults: '**/build/test-results/test/*.xml'
            }

            stage('Release') {
                def release = input(message: "Do you want to release this build? ${unitTestsFailed ? "Unit" : ""}" +
                        " ${componentTestsFailed && unitTestsFailed ? " and Component" : componentTestsFailed ? "Component" : ""}" +
                        " tests failed",
                    parameters: [[$class      : 'BooleanParameterDefinition',
                                  defaultValue: false,
                                  description : 'Ticking this box will do a release',
                                  name        : 'Release']])

                if (release) {
                    stage("Iterate version") {
                        sh "echo \"Iterate version\""
                    }

                    stage("Create docker image tag") {
                        sh "echo \"Create docker image tag\""
                    }

                    stage("Push docker image tag") {
                        sh "echo \"Push docker image tag\""
                    }

                    stage("Deploy on production") {
                        sh "echo \"Push docker image tag\""
                    }
                }
            }

            stage('Done') {
                def description = ""
                if(unitTestsFailed) {
                    description = "Unit tests failed!"
                    currentBuild.result = 'FAILURE'
                } else if (componentTestsFailed) {
                    if(unitTestsFailed) {
                        description = "Unit and component tests failed!"
                    } else {
                        description = "Component tests failed!"
                    }
                    currentBuild.result = 'FAILURE'
                } else {
                    currentBuild.description = "YOU ROCK!"
                    currentBuild.result = 'SUCCESS'
                }
                currentBuild.description = description
            }

    }
}