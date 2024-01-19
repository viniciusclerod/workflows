@Library('jenkins-orb')
import groovy.lang.GroovyShell

def call(String code) {
    def shell = new GroovyShell()
    shell.evaluate(code)
}
