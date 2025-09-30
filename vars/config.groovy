@Library('workflows')
import io.jenkins.plugins.workflows.Pipeline

def call(String yamlPath, Map opts = [:]) {

    Pipeline pipe = new Pipeline(yamlPath, opts)
    pipe.execute(this)

}
