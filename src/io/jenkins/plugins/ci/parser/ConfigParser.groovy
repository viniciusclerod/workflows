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
        Map commands = [:]
        map.each { key, value ->
            ctx.echo "${key}=${value}"
            // List<Step> steps = value.steps ? parseSteps(ctx, value.steps, commands) : []
            List<Step> steps = []
            Command command = new Command(
                name: key,
                parameters: value.parameters,
                // steps: steps
            )
            commands[key] = command
        }
        return commands
        
        // Map builtInCommands = [
        //     run: new Command(name: 'sh')
        // ]
        // return MapHelper.merge(builtInCommands,[:]) as Map<String,Command>
    }

    static List<Step> parseSteps(def ctx, List list, Map<String,Command> commands) {
        List steps = list.collect { item ->
            String key = item.keySet().first()
            Command command = commands.find { it.key == key }?.value ?: new Command(name: key)
            Map arguments = (item[key] instanceof String ? [ script: item[key] ] : item[key]) as Map 
            Step step = new Step(command: command, arguments: arguments)
            return step
        }
        return steps as List<Step>
    }

}
