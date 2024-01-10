@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Job
import com.jenkins.ci.reference.Stage
import com.jenkins.ci.reference.Filter

def call(Stage stg, Configuration config) {
    if (shouldRun(stg.filters)) {
        stage("TEST") {
            sh(
                label: "Teste customizado",
                script: "echo $VERSION",
                returnStdout: true
            )
        }
    //     stage(stg.name) {
    //         if (stg.type == 'approval') {
    //             input(message: "Approval is required to proceed.")
    //         } else {
    //             Job job = config.jobs.find { j -> j.name == stg.key }
    //             withEnv(buildEnvironment(job.environment)) {
    //                 job.steps.each { step ->
    //                     if (step.type == 'sh') {
    //                         ansiColor('xterm') {
    //                             sh(
    //                                 label: step.name,
    //                                 script: step.command,
    //                                 returnStdout: true
    //                             )
    //                         }
    //                     }
    //                 }
    //             }
    //         }
    //     }
    }
}
    
boolean shouldRun(Map<String,Filter> filters) {
    if (filters) {
        if (filters.branches && filters.branches.ignore) {
            boolean shouldBeIgnored = (env.BRANCH_NAME =~ filters.branches.ignore).matches()
            return !shouldBeIgnored
        }
        if (filters.branches && filters.branches.only) {
            boolean shouldBeConsiderated = (env.BRANCH_NAME =~ filters.branches.only).matches()
            return shouldBeConsiderated
        }
    }
    return true
}
