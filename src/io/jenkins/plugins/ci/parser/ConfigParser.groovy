package io.jenkins.plugins.ci.parser

import io.jenkins.plugins.ci.helper.MapHelper
import io.jenkins.plugins.ci.model.Command
import io.jenkins.plugins.ci.model.Configuration
import io.jenkins.plugins.ci.model.Job
import io.jenkins.plugins.ci.model.Step

class ConfigParser {

    static Configuration parse(def ctx, def yaml) {
        Configuration config = new Configuration()
        ConfigParser.buildEnvironment(ctx, config, yaml.environment)
        ConfigParser.buildCommands(ctx, config, yaml.commands)
        ConfigParser.buildJobs(ctx, config, yaml.jobs)
        return config
    }

    static void buildEnvironment(def ctx, Configuration config, Map map) {
        config.environment = map ?: [:]
    }

    static void buildCommands(def ctx, Configuration config, Map map) {
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

    static void buildJobs(def ctx, Configuration config, Map map) {
        map.each { key, value ->
            ctx.echo "${key}, ${value}"
            // Job job = new Job(
            //     name: key,
            //     environment: value.environment ?: [:],
            //     steps: ConfigParser.parseSteps(ctx, config, value.steps) ?: []
            // )
            // config.jobs[key] = job
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
            }
            Command command = config.commands.find { it.key == name }?.value
            if (command == null) {
                config.commands[name] = new Command(context: ctx, name: name)
                command = config.commands[name]
            }
            Step step = new Step(command: command, arguments: arguments)
            return step
        }
        return steps as List<Step>
    }

}
