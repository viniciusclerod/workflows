package io.jenkins.plugins.ci.parser

import io.jenkins.plugins.ci.model.Configuration

class ConfigParser {

    static Configuration parse(def ctx, def yaml) {
        Configuration config = new Configuration()
        config.commands = yaml.commands as Map
        return config
    }

}
