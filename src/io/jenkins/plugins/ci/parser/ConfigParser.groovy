package io.jenkins.plugins.ci.parser

import io.jenkins.plugins.ci.helper.MapHelper
import io.jenkins.plugins.ci.model.Command
import io.jenkins.plugins.ci.model.Configuration
import io.jenkins.plugins.ci.model.Step

class ConfigParser {

    static Configuration parse(def ctx, def yaml) {
        Configuration config = new Configuration()
        ConfigParser.buildCommands(ctx, config, yaml.commands)
        config.steps = ConfigParser.parseSteps(ctx, config, yaml.steps)
        return config
    }

    static void buildCommands(def ctx, Configuration config, Map map) {
        // Map builtInCommands = [
        //     run: new Command(name: 'sh')
        // ]
        map.each { key, value ->
            Boolean isLeaf = !(value.steps) as Boolean
            List<Step> steps = isLeaf ? [] : ConfigParser.parseSteps(ctx, config, value.steps)
            Command command = new Command(
                context: isLeaf ? ctx : config,
                name: key,
                parameters: value.parameters,
                steps: steps
            )
            config.commands[key] = command
        }
    }

    static List<Step> parseSteps(def ctx, Configuration config, List list) {
        List steps = list.collect { item ->
            String name
            def arguments
            switch (item) {
                case Map:
                    name = item.keySet().first()
                    arguments = item[name]
                    break
                default:
                    name = item
                    arguments = [:]
            }
            Command command = config.commands.find { it.key == name }?.value
            ctx.echo "step is ${item.getClass()} name=${name} arguments=${arguments} command=${command}"
            if (command == null) {
                command = new Command(context: ctx, name: name)
            }
            Step step = new Step(command: command, arguments: arguments)
            return step
        }
        return steps as List<Step>
    }

}
