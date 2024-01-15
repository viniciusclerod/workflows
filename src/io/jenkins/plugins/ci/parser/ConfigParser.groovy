package io.jenkins.plugins.ci.parser

import io.jenkins.plugins.ci.model.Configuration
import io.jenkins.plugins.ci.model.Command
import io.jenkins.plugins.ci.model.Step
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

    static List<Step> parseSteps(def ctx, def map, def commands) {
        List steps = map.collect { name, value ->
            ctx.echo "map=${map} name=${name} value=${value}"
            Command command = commands.find { it.key == name }?.value
                ?: new Command(name: name)
            Map arguments = (value instanceof String ? [ script: value ] : value) as Map 
            Step step = new Step(
                command: command,
                arguments: arguments
            )
            return step
        }
        // List steps = [
        //     new Step(
        //         command: commands.run,
        //         arguments: [
        //             label: "Hello from run command",
        //             script: "echo Hello"
        //         ]
        //     )
        // ]
        return steps as List<Step>
    }

}
