@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.parser.ConfigParser

def call(String yamlPath) {
    def yaml = readYaml file: yamlPath
    Configuration config = ConfigParser.parse(yaml, env)
    def closure = buildPipeline(config)
    try {
        closure([:])
    } catch (exc) {
        echo 'Something failed, I should sound the klaxons!'
        throw
    }
}
