package io.jenkins.plugins.workflows.parser

import io.jenkins.plugins.workflows.parser.StepParser
import io.jenkins.plugins.workflows.model.Command
import io.jenkins.plugins.workflows.model.Configuration
import io.jenkins.plugins.workflows.model.Step

class CommandBuilder {

    static void build(def ctx, Configuration config, Map map) {
        map.each { key, value ->
            Boolean isLeaf = !(value.steps) as Boolean
            List<Step> steps = isLeaf ? [] : StepParser.parse(ctx, config, value.steps)
            Command command = new Command(
                ctx: ctx,
                context: isLeaf ? ctx : config,
                name: key,
                parameters: value.parameters,
                steps: steps
            )
            config.commands[key] = command
        }
    }

}
