@Library('jenkins-orb')

def call(Map environment, boolean global = false) {
    if (! environment || ! environment.isEmpty()) return [:]
    String output = sh(
        label: "Preparing environment variables",
        script: environment.collect { k, v -> "$k=$v && echo $k=\$$k"}.join('\n'),
        returnStdout: true
    ).trim()
    return output.split('\n').collect { it ->
        if (global) {
            def (k, v) = it.split('=', 2)
            env.setProperty(k, v)
        }
        return it
    }
}
