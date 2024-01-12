package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Command
import io.jenkins.plugins.ci.model.Step

class Configuration {
    Map<String,Command> commands = [:]
    List<Step> steps = []
}
