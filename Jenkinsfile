#!groovy

node {
  stage 'checkout' {
    checkout scm
  }

  stage 'test' {
    dir("service") {
      sh './gradlew clean test'
    }
  }

  stage 'build' {
    dir("service") {
      sh './gradlew build'
    }
  }

  stage 'archive' {
    archiveArtifacts artifacts: 'service/build/libs/mod-directory-*'
  }

  // step([$class: 'ArtifactArchiver', artifacts: 'build/libs/*.jar', fingerprint: true])
  // 
  // stage 'reports'
  // step([$class: 'JUnitResultArchiver', testResults: 'build/test-results/*.xml'])
}
