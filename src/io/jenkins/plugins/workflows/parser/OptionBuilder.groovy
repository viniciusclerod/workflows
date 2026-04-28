package io.jenkins.plugins.workflows.parser

import io.jenkins.plugins.workflows.helper.MapHelper
import io.jenkins.plugins.workflows.model.Configuration

class OptionBuilder {

    static void build(def ctx, Configuration config, Map map) {
        config.options = MapHelper.merge(config.options, map ?: [:])
    }

}
