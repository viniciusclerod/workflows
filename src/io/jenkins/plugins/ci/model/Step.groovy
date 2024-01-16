package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Command

class Step {

    Command command
    def arguments

    def execute() {
        this.command.execute(this.arguments)
    }

}