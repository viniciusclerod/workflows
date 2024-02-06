package io.jenkins.plugins.workflows.parser

import io.jenkins.plugins.workflows.parser.ActionParser
import io.jenkins.plugins.workflows.model.Configuration
import io.jenkins.plugins.workflows.model.Workflow

class WorkflowBuilder {

    static void build(def ctx, Configuration config, Map map) {
        map.each { key, value ->
            Workflow workflow = new Workflow(
                name: value?.name ?: key,
                actions: ActionParser.parse(ctx, config, value.jobs) ?: []
            )
            config.workflows[key] = workflow
        }
    }

}
