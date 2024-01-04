@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job

def generateJob(job) {
    return {
        stage(job.name) {
            job.steps.each { step ->
                sh step.command
            }
        }
    }
}

def call(Configuration config) {
    return { variables ->
        List<Job> jobs = config.jobs
        jobs.each { job ->
            generateJob(job)
            // stage(job.name) {
            //     job.steps.each { step ->
            //         sh step.command
            //     }
            // }
        }
    
    }
}
