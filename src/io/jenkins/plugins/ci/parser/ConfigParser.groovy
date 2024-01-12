package io.jenkins.plugins.ci.parser

import io.jenkins.plugins.ci.model.Configuration
import io.jenkins.plugins.ci.model.Command
import io.jenkins.plugins.ci.helper.MapHelper

class ConfigParser {

    static Configuration parse(def ctx, def yaml) {
        Configuration config = new Configuration()
        config.commands = parseCommands(ctx, yaml.commands)
        config.steps = parseSteps(ctx, yaml.steps, config.commands)
        return config
    }

    static Map<String,Command> parseCommands(def ctx, Map map) {
        Map builtInCommands = [
            run: new Command(name: 'sh')
        ]
        return MapHelper.merge(builtInCommands,[:]) as Map<String,Command>
    }

    static Map<String,Step> parseSteps(def ctx, Map map, Map<String,Command> commands) {
        Map steps = [
            new Step(
                command: commands.run
                arguments: [
                    label: "Hello from run command",
                    script: "echo Hello"
                ]
            )
        ]
        return step as Map<String,Step>
    }

    // static Command buildCommand(Map map) {
    //     Command command = new Command([
    //         name: name,
    //         parameters: value.parameters ?: [:],
    //         steps: value.steps ?: []
    //     ])
    //     return command
    // }

}
