@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Command
import com.jenkins.ci.parser.ConfigParser

def call(String yamlPath) {
    def yaml = readYaml file: yamlPath

    List<Command> commands = [
        run: new Command(context: this, name: 'sh')
    ]
    commands.run.call([
        label: 'Test Command Invocation',
        script: 'echo INVOKED!',
        returnStdout: true
    ])

    // Configuration config = ConfigParser.parse(this, yaml, env)
    // buildPipeline(config)
}
