package io.jenkins.plugins.ci.parser

import io.jenkins.plugins.ci.helper.MapHelper
import io.jenkins.plugins.ci.model.*

class ConfigParser {

    static Configuration parse(def ctx, def yaml) {
        Configuration config = new Configuration()
        ConfigParser.buildEnvironment(ctx, config, yaml.environment)
        ConfigParser.buildCommands(ctx, config, yaml.commands)
        ConfigParser.buildJobs(ctx, config, yaml.jobs)
        ConfigParser.buildWorkflows(ctx, config, yaml.workflows)
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
            Job job = new Job(
                name: key,
                environment: value.environment ?: [:],
                steps: ConfigParser.parseSteps(ctx, config, value.steps) ?: []
            )
            config.jobs[key] = job
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

    static void buildWorkflows(def ctx, Configuration config, Map map) {
        map.each { key, value ->
            Workflow workflow = new Workflow(
                name: value?.name ?: key,
                actions: ConfigParser.parseActions(ctx, config, value.jobs) ?: []
            )
            config.workflows[key] = workflow
        }
    }

    static List<Action> parseActions(def ctx, Configuration config, List list) {
        List actions = list.collect { it ->
            Action action = null
            switch (it) {
                case Map:
                    String key = it.keySet().first()
                    action = new Action(
                        name: it[key].name ?: key,
                        type: it[key].type ?: 'job',
                        job: config.jobs[key],
                        filters: it[key].filters?.collectEntries { rule, filter ->
                            [(rule): new Filter(
                                only: filter.only ?: null,
                                ignore: filter.ignore ?: null
                            )]
                        } ?: [:]
                    )
                    break
                default:
                    String key = it
                    action = new Action(
                        name: key,
                        type: 'job',
                        job: config.jobs[key]
                    )
            }
            return action
        }
        return actions as List<Action>
    }

}
