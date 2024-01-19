@Library('jenkins-orb')


def call(String script) {
    def binding = new Binding()
    binding.delegate = this
    Eval.me(script, binding)
}
