@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.workflow.Stage

HashMap builtInEnvVars = [
    'PROJECT_REPONAME': '$(git config --local remote.origin.url | sed -n \'s#.*/\([^.]*\)\.git#\1#p\')'
]

def call(Configuration config) {
    buildEnvVars(builtInEnvVars, true)
    buildEnvVars(config.environment, true)
    return { variables ->
        List<Stage> stgs = config.workflow
        stgs.each { stg ->
            if (stg.branches == null || (env.BRANCH_NAME =~ stg.branches).matches()) {
                stage(stg.name) {
                    if (stg.type == 'approval') {
                        input(message: "Approval is required to proceed.")
                    } else {
                        Job job = config.jobs.find { j -> j.name == stg.key }
                        withEnv(buildEnvVars(job.environment)) {
                            job.steps.each { step ->
                                if (step.type == 'sh') {
                                    sh script: step.command, returnStdout: true, label: step.name
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
