@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job

def call(Configuration config) {
    return { variables ->
        List<Job> jobs = config.jobs
        jobs.each { job ->
            stage(job.name) {
                job.steps.each { command ->
                    echo command.execute()
                }
            }
        }
    
    }
}
