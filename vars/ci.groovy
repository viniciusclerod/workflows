@Library('jenkins-orb')
// import com.jenkins.ci.reference.Configuration
// import com.jenkins.ci.reference.Step
// import com.jenkins.ci.reference.Command
// import com.jenkins.ci.parser.ConfigParser
import io.jenkins.plugins.yaml-ci.Pipeline

// class Command {
//     String name
//     def call(def context, Map arguments = [:]) {
//         context.invokeMethod(this.name, arguments)
//     }
// }

def call(String yamlPath) {
    
    Pipeline pipe = new Pipeline()
    pipe.execute()

    // def yaml = readYaml file: yamlPath
    // Configuration config = ConfigParser.parse(this, yaml, env)
    // buildPipeline(config)

    // this.invokeMethod('sh', [
    //     label: "Hello Invocation",
    //     script: "echo Hello"
    // ])

    // Command command = new Command(name: 'sh')
    // command.call(this, [
    //     label: "Hello Command",
    //     script: "echo Hello"
    // ])
}
