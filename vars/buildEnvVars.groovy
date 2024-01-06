@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.workflow.Stage

def call(def environment, boolean global = false) {
    // env.PROJECT=$(git config --local remote.origin.url|sed -n 's#.*/\([^.]*\)\.git#\1#p')
    // env.TAG=$(git rev-parse --short HEAD)
    String output = sh(
        label: "Load environment variables",
        script: environment.collect { k, v -> "$k=$v && echo $k=\$$k"}.join('\n'),
        returnStdout: true
    ).trim()
    output.eachLine { it -> echo "$it" }
    return output.split('\n').collect { it ->
        if (global) {
            def (k, v) = it.split('=', 2)
            env.setProperty(k, v)
        }
        return it
    }
    // echo "FOO $FOO ${env.FOO}"
    // return environment.collect { k, v -> "${k}=${v}" }
    // return environment.collect { k, v ->
    //     env.setProperty(k, v sh(script: "${envKey}=${envVal} && echo \$${envKey}", returnStdout: true))
    //     echo "[${envKey}] ${env.getProperty(envKey)}"
    // }
    
    // job.environment.each { envKey, envVal ->
    //     env.setProperty(envKey, sh(script: "${envKey}=${envVal} && echo \$${envKey}", returnStdout: true))
    //     echo "[${envKey}] ${env.getProperty(envKey)}"
    // }
}
