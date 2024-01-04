@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.workflow.Stage

def generateJob(key, config) {
    Job job = config.jobs.find { it -> it.name == key }
    echo "JOB: ${job.name}" 
    
            // job.steps.each { step ->
            //     sh step.command
            // }
    // return {
    //     stage(stg.name) {
    //         echo "key: ${stg.key}"
    //         echo "name: ${stg.name}"
    //     }
    // }
}

def generateStage(stg, config) {
    return {
        stage(stg.name) {
            echo "Stage: ${stg.key}"
            generateJob(stg.key, config)
        }
    }
}

def call(Configuration config) {
    return { variables ->
        // List<Job> jobs = config.jobs
        // jobs.each { job ->
        //     generateJob(job)
        // }
        List<Stage> stages = config.workflow
        stages.each { stg ->
            if (stg.branches == null || (env.BRANCH_NAME =~ stg.branches).matches()) {
                generateStage(stg, config)
            }
        }
    }
}

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