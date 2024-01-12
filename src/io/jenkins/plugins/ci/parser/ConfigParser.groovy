package io.jenkins.plugins.ci.parser

import io.jenkins.plugins.ci.model.Configuration
import io.jenkins.plugins.ci.model.Command

class ConfigParser {

    static Configuration parse(def ctx, def yaml) {
        Configuration config = new Configuration()
        config.commands = parseCommands(ctx, yaml.commands)
        return config
    }


    static Map<String,Command> parseCommands(def ctx, Map map) {
        Map<String,Command> commands = map.collectEntries { name, value ->
            Command command = new Command([
                name: name,
                description: value.description,
                parameters: value.parameters ?: [:],
                steps: value.steps ?: []
            ])
            return ["${name}": command]
        } ?: [:]
        return commands
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
