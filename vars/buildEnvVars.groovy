@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.workflow.Stage

def call(def environment, boolean global = false) {
    // env.PROJECT=$(git config --local remote.origin.url|sed -n 's#.*/\([^.]*\)\.git#\1#p')
    // env.TAG=$(git rev-parse --short HEAD)
    String output = sh(
        label: "Load environment variables",
        script: environment.collect { k, v -> "${k}=${v}"}.join('\n'),
        returnStdout: true
    ).trim()
    echo "$output"
    return environment.collect { k, v -> "$k=${env.getProperty(k)}" }
    // return environment.collect { k, v ->
    //     env.setProperty(k, v sh(script: "${envKey}=${envVal} && echo \$${envKey}", returnStdout: true))
    //     echo "[${envKey}] ${env.getProperty(envKey)}"
    // }
    
    // job.environment.each { envKey, envVal ->
    //     env.setProperty(envKey, sh(script: "${envKey}=${envVal} && echo \$${envKey}", returnStdout: true))
    //     echo "[${envKey}] ${env.getProperty(envKey)}"
    // }
}
