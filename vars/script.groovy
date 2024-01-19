@Library('jenkins-orb')

@Grab('org.codehaus.groovy:groovy-all:2.4.7')
import groovy.lang.GroovyClassLoader

def call(String script) {
    // def shell = new GroovyShell()
    // shell.evaluate(script)
    def classLoader = new GroovyClassLoader()
    def clazz = classLoader.parseClass(script)
    clazz.newInstance()
}
