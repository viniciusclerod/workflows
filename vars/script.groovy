@Library('jenkins-orb')

def call(String script) {
    echo "${this}"
    def closure = {
        return Eval.me(script)
    }
    closure.delegate = this
    return closure.call()
}
