#!groovy

node {

  stage ('checkout') {
    checkout scm
  }

  stage ('test') {
    dir ( 'service' ) {
      sh './gradlew --no-daemon --console=plain clean test'
    }
  }

  stage ('build') {
    dir ( 'service' ) {
      sh './gradlew --no-daemon --console=plain build'
    }
  }

  stage ('archive') {
    archiveArtifacts artifacts: 'service/build/libs/mod-directory-*'
  }

  // step([$class: 'ArtifactArchiver', artifacts: 'build/libs/*.jar', fingerprint: true])
  // 
  // stage 'reports'
  // step([$class: 'JUnitResultArchiver', testResults: 'build/test-results/*.xml'])
}
