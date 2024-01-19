@Library('jenkins-orb')

def call(String script) {
    def methods = this.getClass().getMethods().collect { it.name }
    echo "${this.getClass()}\n${methods}"
    def closure = {
        return Eval.me(script)
    }
    closure.delegate = this
    return closure.call()
}
