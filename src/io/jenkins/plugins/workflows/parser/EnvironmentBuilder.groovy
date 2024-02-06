package io.jenkins.plugins.workflows.parser

import io.jenkins.plugins.workflows.model.Configuration

class EnvironmentBuilder {

    static void build(def ctx, Configuration config, Map map) {
        config.environment = map ?: [:]
    }

}
