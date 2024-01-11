@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Step
import com.jenkins.ci.reference.Command
import com.jenkins.ci.parser.ConfigParser

def call(String yamlPath) {
    def yaml = readYaml file: yamlPath
    // Configuration config = ConfigParser.parse(this, yaml, env)
    // buildPipeline(config)

    // List<Command> commands = [
    //     run: new Command(
    //         name: 'sh',
    //         parameters: [
    //             name: [                        
    //                 type: String,
    //                 default: "Hello World"
    //             ],
    //             command: [                        
    //                 type: String,
    //                 default: "echo Hello"
    //             ]
    //         ],
    //         steps: [
    //             sh: new Step(
    //                 name: 'sh',
    //                 arguments: [
    //                     label: "<< parameters.name >>",
    //                     script: "echo Hello"
    //                 ],
    //                 command: new Command(
    //                     context: this,
    //                     name: 'sh'
    //                 )
    //             )
                
    //         ]
    //     )
    // ]
    // commands.run.call([
    //     name: "Teste",
    //     command: "echo Teste"
    // ])

    // Step step = new Step(
    //     name: 'sh',
    //     arguments: [
    //         label: "<< parameters.name >>",
    //         script: "echo Hello"
    //     ],
    //     command: new Command(
    //         context: this,
    //         name: 'sh'
    //     )
    // )
    // step.call()

    // Command command = new Command(
    //     context: this,
    //     name: 'sh'
    // )
    // command.call([
    //     label: "Hello",
    //     script: "echo Hello"
    // ])

    this.invokeMethod('sh', [
        label: "Hello Invocation",
        script: "echo Hello"
    ])

    class Command {
        String name
        def call(def context, Map arguments = [:]) {
            context.invokeMethod(this.name, arguments)
        }
    }

    Command command = new Command(
        context: this,
        name: 'sh'
    )
    command.call(this, [
        label: "Hello Command",
        script: "echo Hello"
    ])
}
