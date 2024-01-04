@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.workflow.Stage

def call(Configuration config) {
    return { variables ->
        // List<Job> jobs = config.jobs
        // jobs.each { job ->
        //     generateJob(job)
        // }
        List<Stage> stgs = config.workflow
        stgs.each { stg ->
            // if (stg.branches == null || (env.BRANCH_NAME =~ stg.branches).matches()) {
            stage(stg.name) {
                Job job = config.jobs.find { j -> j.name == stg.key }
                job.steps.each { step ->
                    sh step.command
                }
            }
            // }
        }
    }
}

// if ((env.BRANCH_NAME =~ '^((?!develop|master|release).)*$').matches()) {
//     stage("Deploy"){
//         echo 'Deployed release to QA'
//     }
// }