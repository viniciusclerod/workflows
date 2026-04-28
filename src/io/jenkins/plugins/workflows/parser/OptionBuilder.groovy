package io.jenkins.plugins.workflows.parser

import io.jenkins.plugins.workflows.model.Configuration

class OptionBuilder {

    static void build(def ctx, Configuration config, Map map) {
        config.options = map ?: [:]
    }

}
