@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Command
import com.jenkins.ci.parser.ConfigParser

def call(String yamlPath) {
    def yaml = readYaml file: yamlPath
    Configuration config = ConfigParser.parse(this, yaml, env)
    // buildPipeline(config)

    Map commands = [
        run: new Command(
            context: config.commands,
            name: 'sh',
            parameters: [
                name: [                        
                    type: String,
                    default: "Hello World"
                ],
                command: [                        
                    type: String,
                    default: "echo Hello"
                ]
            ],
            steps: [
                sh: new Step(
                    name: 'sh',
                    arguments: [
                        label: "<< parameters.name >>",
                        script: "echo Hello"
                    ],
                    command: new Command(
                        context: context,
                        name: 'sh',
                    )
                )
                
            ]
        )
    ]
    command.run.call([
        name: "Teste",
        command: "echo Teste"
    ])
}
