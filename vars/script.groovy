@Library('jenkins-orb')

def call(String script) {
    return Eval.x(this, script)
}
