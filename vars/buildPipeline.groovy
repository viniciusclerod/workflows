@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.workflow.Stage

def call(Configuration config) {
    return { variables ->
        List<Stage> stgs = config.workflow
        stgs.each { stg ->
            if (stg.branches == null || (env.BRANCH_NAME =~ stg.branches).matches()) {
                stage(stg.name) {
                    if (stg.type == 'approval') {
                        input(message: "Approval is required to proceed.")
                    } else {
                        Job job = config.jobs.find { j -> j.name == stg.key }
                        job.steps.each { step ->
                            sh script: step.command, returnStdout: true
                        }
                    }
                }
            }
        }
    }
}
