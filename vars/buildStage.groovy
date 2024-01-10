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
    // echo "BRANCH_NAME=${env.BRANCH_NAME}\nCHANGE_BRANCH=${env.CHANGE_BRANCH}\nCHANGE_ID=${env.CHANGE_ID}"
    Boolean proceed = true
    if (filters) {
        if (env.CHANGE_ID != null) {
            if (filters.pull && filters.pull.ignore) {
                Boolean abort = (env.CHANGE_BRANCH =~ filters.pull.ignore).matches()
                echo "proceed=false\nabort=${abort}\nCHANGE_BRANCH=${env.CHANGE_BRANCH}\nfilters.pull.ignore=${filters.pull.ignore}"
                if (abort) return false
            }
            if (filters.pull && filters.pull.only) {
                proceed &= (env.CHANGE_BRANCH =~ filters.pull.only).matches()
                echo "proceed=${proceed}\nCHANGE_BRANCH=${env.CHANGE_BRANCH}\nfilters.pull.only=${filters.pull.only}"
            }
        } else {
            if (filters.branches && filters.branches.ignore) {
                Boolean abort = (env.BRANCH_NAME =~ filters.branches.ignore).matches()
                echo "proceed=false\nabort=${abort}\nBRANCH_NAME=${env.BRANCH_NAME}\nfilters.branches.ignore=${filters.branches.ignore}"
                if (abort) return false
            }
            if (filters.branches && filters.branches.only) {
                proceed &= (env.BRANCH_NAME =~ filters.branches.only).matches()
                echo "proceed=${proceed}\nBRANCH_NAME=${env.BRANCH_NAME}\nfilters.branches.only=${filters.branches.only}"
            }
        }
    }
    return proceed
}
