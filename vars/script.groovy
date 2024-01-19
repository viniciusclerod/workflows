@Library('jenkins-orb')


def call(String script) {
    def result = Eval.me(script)
    echo result
}
