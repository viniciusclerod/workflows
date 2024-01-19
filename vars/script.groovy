@Library('jenkins-orb')

def call(String script) {
    echo "${this}"
    def closure = {
        Eval.me(script)
    }
    closure.delegate = this
    return closure.call()
}
