@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.workflow.Stage

// def generateJob(key, config) {
//     Job job = config.jobs.find { it -> it.name == key }
//     echo "Job: ${job.name}"
    
//             // job.steps.each { step ->
//             //     sh step.command
//             // }
//     // return {
//     //     echo "Job: ${job.name}"
//     // }
// }

def call(Configuration config) {
    return { variables ->
        // List<Job> jobs = config.jobs
        // jobs.each { job ->
        //     generateJob(job)
        // }
        List<Stage> stgs = config.workflow
        stgs.each { stg ->
            stage(stg.name) {
                echo "Stage: ${stg.key}"
                Job job = config.jobs.find { it -> it.name == key }
                echo "Job: ${job} ${job.name}"
            }
        }
    }
}
            // if (stg.branches == null || (env.BRANCH_NAME =~ stg.branches).matches()) {
            // }

// def generateJob(job) {
//     return {
//         stage(job.name) {
//             job.steps.each { step ->
//                 sh step.command
//             }
//         }
//     }
// }

// if ((env.BRANCH_NAME =~ '^((?!develop|master|release).)*$').matches()) {
//     stage("Deploy"){
//         echo 'Deployed release to QA'
//     }
// }