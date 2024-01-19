@Library('jenkins-orb')
import io.jenkins.plugins.ci.Pipeline

def call(String yamlPath) {

    Pipeline pipe = new Pipeline(yamlPath)
    pipe.execute(this)

}
