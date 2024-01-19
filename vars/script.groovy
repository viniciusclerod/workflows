@Library('jenkins-orb')

def call(String script) {
    echo "${this} ${this.getClass()}"
    def closure = {
        return Eval.me(script)
    }
    closure.delegate = this
    return closure.call()
}
