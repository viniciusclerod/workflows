package io.jenkins.plugins.ci.model

class Step {

    Command command
    def arguments

    def execute() {
        this.command.execute(this.arguments)
    }

}