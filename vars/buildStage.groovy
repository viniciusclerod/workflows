@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Job
import com.jenkins.ci.reference.Stage
import com.jenkins.ci.reference.Filter

def call(Stage stg, Configuration config) {
    if (shouldRun(stg.filters)) {
        stage(stg.name) {
            if (stg.type == 'approval') {
                input(message: "Approval is required to proceed.")
            } else {
                Job job = config.jobs.find { j -> j.name == stg.key }
                withEnv(buildEnvironment(job.environment)) {
                    job.steps.each { step ->
                        if (step.type == 'sh') {
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
    
Boolean shouldRun(Map filters) {
    echo "BRANCH_NAME=${env.BRANCH_NAME}\nCHANGE_BRANCH=${env.CHANGE_BRANCH}\nCHANGE_ID=${env.CHANGE_ID}"
    if (filters) {
        if (filters.pull_request) {
            // Boolean hasPrOpened = (env.CHANGE_ID != null)
            // if (!hasPrOpened) return false
            if (filters.branches && filters.branches.ignore) {
                Boolean shouldBeIgnored = (env.CHANGE_BRANCH =~ filters.branches.ignore).matches()
                return !shouldBeIgnored
            }
            if (filters.branches && filters.branches.only) {
                Boolean shouldBeConsiderated = (env.CHANGE_BRANCH =~ filters.branches.only).matches()
                return shouldBeConsiderated
            }
        } else {
            if (filters.branches && filters.branches.ignore) {
                Boolean shouldBeIgnored = (env.BRANCH_NAME =~ filters.branches.ignore).matches()
                return !shouldBeIgnored
            }
            if (filters.branches && filters.branches.only) {
                Boolean shouldBeConsiderated = (env.BRANCH_NAME =~ filters.branches.only).matches()
                return shouldBeConsiderated
            }
        }
    }
    return true
}
