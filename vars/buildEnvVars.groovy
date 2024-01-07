@Library('jenkins-orb')

def call(Map environment, boolean global = false) {
    echo environment.getClass().getName()
    if (environment.isEmpty()) {
        echo "environment is empty"
    } else {
        echo "environment is not empty"
    }
    // if (! environment || ! environment.isEmpty()) return [:]
    String output = sh(
        label: "Preparing environment variables",
        script: environment.collect { k, v -> "$k=$v && echo $k=\$$k"}.join('\n'),
        returnStdout: true
    ).trim()
    return ['MOCK=value']
    // return output.split('\n').collect { it ->
    //     if (global) {
    //         def (k, v) = it.split('=', 2)
    //         env.setProperty(k, v)
    //     }
    //     return it
    // }
}
