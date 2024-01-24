package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Action

class Workflow {

    String name
    List<Action> actions = []

    def execute() {
        this.actions.each { action ->
            action.execute()
        }
    }

}
