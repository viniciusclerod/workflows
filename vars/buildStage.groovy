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
    
boolean shouldRun(Map<String,Filter> filters) {
    if (filters) {
        echo "shouldRun has filters=${filters}"
        if (filters.branches != null) {
            echo "shouldRun has filters.branches=${filters.branches}"
        }
        if (filters.branches != null && filters.branches.ignore != null) {
            echo "shouldRun has filters.branches.ignore=${filters.branches.ignore}"
            boolean shouldBeIgnored = (env.BRANCH_NAME =~ filters.branches.ignore).matches()
            echo "!shouldBeIgnored=${!shouldBeIgnored}"
            return !shouldBeIgnored
        }
        if (filters.branches != null && filters.branches.only != null) {
            echo "shouldRun has filters.branches.only=${filters.branches.only}"
            boolean shouldBeConsiderated = (env.BRANCH_NAME =~ filters.branches.only).matches()
            echo "!shouldBeConsiderated=${!shouldBeConsiderated}"
            return shouldBeConsiderated
        }
    }
    echo "shouldRun=true"
    return true
}
