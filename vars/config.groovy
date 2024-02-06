@Library('workflows')
import io.jenkins.plugins.workflows.Pipeline

def call(String yamlPath) {

    Pipeline pipe = new Pipeline(yamlPath)
    pipe.execute(this)

}
