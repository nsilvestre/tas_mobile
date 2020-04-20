pipeline {
	  agent any
	  //triggers{ cron('H/5 * * * *') }
	  options {
        buildDiscarder(logRotator(numToKeepStr:'10'))
        timestamps()
        timeout(time: 90, unit: 'MINUTES')
	  }
	  stages {
		//Run the maven build
		stage('Build') {
		  steps {
			script {
				slackSend color: "good", message: "Job: ${env.JOB_NAME} with Build Number ${env.BUILD_NUMBER} - # Started #"
				if (isUnix()) {
					sh 'mvn test -Dcucumber.options="--glue com/company/test/steps/web --tags @working --tags ~@broken src/test/java/com/company/test/features/web/facebookLogin.feature"'
				} else {
					bat 'mvn test -Dcucumber.options="--glue com/company/test/steps/web --tags @working --tags ~@broken src/test/java/com/company/test/features/web/facebookLogin.feature"'
				}	
			}	
		  }
		}
	  }
	  post {
		   always {
				script {
				   archiveArtifacts 'target\\**'
				   junit 'target\\*.xml'
				   cucumber fileIncludePattern: 'cucumber.json', jsonReportDirectory: 'target/', sortingMethod: 'ALPHABETICAL'
				   livingDocs featuresDir: 'target/'
				   perfReport compareBuildPrevious: true, errorFailedThreshold: 0, errorUnstableThreshold: 0, failBuildIfNoResultFile: false, modePerformancePerTestCase: true, sourceDataFiles: 'target\\*.xml', modeThroughput: false
				   
				   //Send notification mail
				   mail body: "Run ${JOB_NAME}-#${BUILD_NUMBER} ${currentBuild.currentResult}. \nTo get more details, visit the build results page: \n${BUILD_URL}.",
				   cc: '',
				   bcc: '',
				   from: '',
				   replyTo: '',
				   subject: "${JOB_NAME} ${BUILD_NUMBER}",
				   to: 'test.automation2019@gmail.com'
				}
		   }
		   success {
				slackSend color: "good", message: "Job: ${env.JOB_NAME} with Build Number ${env.BUILD_NUMBER} was Successful. # Ended #"
		   }
		   failure {
				slackSend color: "danger", message: "Job: ${env.JOB_NAME} with Build Number ${env.BUILD_NUMBER} was Failed. # Ended #"
		   }
		   unstable {
				slackSend color: "warning", message: "Job: ${env.JOB_NAME} with Build Number ${env.BUILD_NUMBER} was Unstable. # Ended #"
		   }
		   aborted {
				slackSend color: "danger", message: "Job: ${env.JOB_NAME} with Build Number ${env.BUILD_NUMBER} was Aborted. # Ended #"
		   }
	  }
}	