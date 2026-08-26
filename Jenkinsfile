pipeline {
    agent {label 'docker-agent'}
    environment {
        DB_HOST = 'aqa-postgres'
        DB_PORT = '5432'
        DB_NAME = 'aqa_db'
        DB_USERNAME = 'postgres_user'
        DB_PASSWORD = credentials('DB_PASSWORD')
    }
    stages {
        stage('regression suite run') {
            steps {
                sh 'mvn clean test -Dsurefire.suiteXmlFile=src/test/resources/test-suites/regression.xml -Dselenide.headless=true'
            }
        }
    }
}