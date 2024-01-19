@Library('jenkins-orb')


def call(String script) {
    // def shell = new GroovyShell()
    // shell.evaluate(script)
    // def classLoader = new GroovyClassLoader()
    // def clazz = classLoader.parseClass(script)
    // clazz.newInstance()
    Eval.me(script)
}
