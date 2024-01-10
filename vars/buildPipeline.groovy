@Library('jenkins-orb')
import com.jenkins.ci.helpers.MapHelper
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Stage

def call(Configuration config) {
    ansiColor('xterm') {
        sh(
            label: "Shell Script in Background",
            script: "BUILD_ID=dontKillMe nohup echo HELLO > /dev/null 2>&1 &",
            returnStdout: true
        )
        sh(
            label: "Shell Script not in Background",
            script: "echo HELLO",
            returnStdout: true
        )
        // Built-in environment variables
        buildEnvironment(MapHelper.merge([
            'PROJECT_REPONAME': '$(git config --local remote.origin.url | sed -n \'s#.*/\\([^.]*\\)\\.git#\\1#p\')',
            'COMMIT_SHA1': '$(git rev-parse HEAD)',
            'COMMIT_SHA1_SHORT': '$(git rev-parse --short HEAD)'
        ], config.environment), true)
        // Build stages
        List<Stage> stgs = config.workflow
        stgs.each { stg -> buildStage(stg, config) }
    }
}
