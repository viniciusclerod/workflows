package io.jenkins.plugins.workflows.parser

import io.jenkins.plugins.workflows.parser.StepParser
import io.jenkins.plugins.workflows.model.Job
import io.jenkins.plugins.workflows.model.Configuration

class JobBuilder {

    static void build(def ctx, Configuration config, Map map) {
        map.each { key, value ->
            Job job = new Job(
                name: key,
                environment: value.environment ?: [:],
                steps: StepParser.parse(ctx, config, value.steps) ?: []
            )
            config.jobs[key] = job
        }
    }

}
