@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Command
import com.jenkins.ci.parser.ConfigParser

def call(String yamlPath) {
    def yaml = readYaml file: yamlPath

    // Map commands = [
    //     run: new Command(context: this, name: 'sh')
    // ]
    // commands.run.call([
    //     label: 'Test Command Invocation',
    //     script: 'echo INVOKED!',
    //     returnStdout: true
    // ])

    Configuration config = ConfigParser.parse(this, yaml, env)

    config.commands.run.call([
        label: 'Test Command Invocation',
        script: 'echo INVOKED!',
        returnStdout: true
    ])

    // buildPipeline(config)
}
