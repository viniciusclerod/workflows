@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job

def call(Configuration config) {
    return { variables ->
        List<Job> jobs = config.jobs
        jobs.each { job ->
            generateJob(job)
        }
    
    }
}

def generateJob(job) {
    return {
        stage(job.name) {
            job.steps.each { step ->
                sh step.command
            }
        }
    }
}