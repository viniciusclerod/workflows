package io.jenkins.plugins.workflows.parser

import io.jenkins.plugins.workflows.model.Configuration
import io.jenkins.plugins.workflows.parser.ContextBuilder
import io.jenkins.plugins.workflows.parser.CommandBuilder
import io.jenkins.plugins.workflows.parser.EnvironmentBuilder
import io.jenkins.plugins.workflows.parser.JobBuilder
import io.jenkins.plugins.workflows.parser.WorkflowBuilder

class ConfigurationParser {

    static Configuration parse(def ctx, def yaml) {
        Configuration config = new Configuration()
        ContextBuilder.build(ctx, config, yaml.environment)
        EnvironmentBuilder.build(ctx, config, yaml.environment)
        CommandBuilder.build(ctx, config, yaml.commands)
        JobBuilder.build(ctx, config, yaml.jobs)
        WorkflowBuilder.build(ctx, config, yaml.workflows)
        return config
    }

}
