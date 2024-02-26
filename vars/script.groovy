@Library('workflows')

def call(String script) {
    try {
        sh(
            label: 'Debug Evaluate',
            script: "echo ${script}"
        )
        evaluate(script)
    } catch (Exception e) {
        sh(
            label: 'Evaluate Error',
            script: "echo 'Error: ${e.message}'"
        )
    }
}
