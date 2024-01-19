@Library('jenkins-orb')

def call(String script) {
    echo "${this.echo}"
    echo evaluate(script)
    // def closure = {
    //     return Eval.me(script)
    // }
    // closure.delegate = this
    // closure.resolveStrategy = Closure.DELEGATE_FIRST
    // return closure.call()
}
