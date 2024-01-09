@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Job
import com.jenkins.ci.reference.Stage
import com.jenkins.ci.reference.Filters

def call(Stage stg, Configuration config) {
    shouldRun(stg.filters) && stage(stg.name) {
        if (stg.type == 'approval') {
            input(message: "Approval is required to proceed.")
        } else {
            Job job = config.jobs.find { j -> j.name == stg.key }
            withEnv(buildEnvironment(job.environment)) {
                job.steps.each { step ->
                    if (step.type == 'sh') {
                        ansiColor('xterm') {
                            sh(
                                label: step.name,
                                script: step.command,
                                returnStdout: true
                            )
                        }
                    }
                }
            }
        }
    }
}

boolean shouldRun(Filters filters) {
    if (filters) {
        if (filters.branches) {
            boolean hasMatchBranches = (env.BRANCH_NAME =~ filters.branches).matches()
            return hasMatchBranches
        }
    }
    return false
}
