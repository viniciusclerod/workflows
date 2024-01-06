@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.workflow.Stage

def call(Configuration config) {
    // env.PROJECT=$(git config --local remote.origin.url|sed -n 's#.*/\([^.]*\)\.git#\1#p')
    // env.TAG=$(git rev-parse --short HEAD)

    return { variables ->
        List<Stage> stgs = config.workflow
        stgs.each { stg ->
            if (stg.branches == null || (env.BRANCH_NAME =~ stg.branches).matches()) {
                stage(stg.name) {
                    if (stg.type == 'approval') {
                        input(message: "Approval is required to proceed.")
                    } else {
                        Job job = config.jobs.find { j -> j.name == stg.key }
                        // SET ENV VARS
                        def envVars = job.environment ? buildEnvVars(job.environment) : []
                        
                        // sh(
                        //     label: "Load environment variables",
                        //     script: job.environment.collect { envKey, envVal -> "${envKey}=${envVal}"}.join('\n'),
                        //     returnStdout: true
                        // )

                        // job.environment.collect { envKey, envVal ->
                        //     env.setProperty(envKey, sh(script: "${envKey}=${envVal} && echo \$${envKey}", returnStdout: true))
                        //     echo "[${envKey}] ${env.getProperty(envKey)}"
                        // }

                        // job.environment.each { envKey, envVal ->
                        //     env.setProperty(envKey, sh(script: "${envKey}=${envVal} && echo \$${envKey}", returnStdout: true))
                        //     echo "[${envKey}] ${env.getProperty(envKey)}"
                        // }
                        // echo "${job.environment}"
                        // envVars = job.environment.collect { envKey, envVal -> "${envKey}=${envVal}"}

                        withEnv(envVars) {
                            job.steps.each { step ->
                                if (step.type == 'sh') {
                                    sh script: step.command, returnStdout: true, label: step.name
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
