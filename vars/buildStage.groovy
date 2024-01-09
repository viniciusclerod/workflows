@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Job
import com.jenkins.ci.reference.Stage

def call(Stage stg, Configuration config) {
    if (shouldRun(['branches': stg.branches])) {
        echo "should run"
    }
    if (stg.branches == null || (env.BRANCH_NAME =~ stg.branches).matches()) {
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

boolean shouldRun(Map filters) {
    boolean hasNoFilters = (filters == null)
    if (hasNoFilters) return true
    boolean hasMatchBranches = (filters.branches == null) || (env.BRANCH_NAME =~ filters.branches).matches()
    return hasMatchBranches
}