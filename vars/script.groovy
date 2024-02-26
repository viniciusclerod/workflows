@Library('workflows')

def call(String script) {
    try {
        echo "${script}"
        evaluate(script)
    } catch (Exception e) {
        sh(
            label: 'Evaluate Error',
            script: "echo 'Error: ${e.message}'"
        )
    }
}
