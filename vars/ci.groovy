@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Command
import com.jenkins.ci.parser.ConfigParser

def call(String yamlPath) {
    def yaml = readYaml file: yamlPath
    
    Command command = new Command(name: 'sh', arguments: [
        label: 'Test Command Invocation',
        script: 'echo INVOKED!',
        returnStdout: true
    ])
    command.invoke(this)

    Configuration config = ConfigParser.parse(this, yaml, env)
    buildPipeline(config)
}
