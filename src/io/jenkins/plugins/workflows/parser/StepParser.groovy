package io.jenkins.plugins.workflows.parser

import io.jenkins.plugins.workflows.model.Command
import io.jenkins.plugins.workflows.model.Configuration
import io.jenkins.plugins.workflows.model.Step

class StepParser {

    static List<Step> parse(def ctx, Configuration config, List list) {
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
