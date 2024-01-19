@Library('jenkins-orb')

def call(String script) {
    def closure = {
        Eval.me(script)
    }
    closure.delegate = this
    return closure.call()
}
