@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.workflow.Stage

def generateStage(stage) {
    return {
        stage(stage.name) {
            sh "echo ${stage}"
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
        stages.each { stage ->
            stage(stage.name) {
                echo "stage.key: ${stage.key}"
                echo "stage.name: ${stage.name}"
            }
        }
        // stages.each { stage ->
        //     generateStage(stage)
        // }
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