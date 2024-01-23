package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Stage

class Workflow {

    String name
    List<Stage> stages = []

    def execute() {
        this.stages.each { stage ->
            stage.execute()
        }
    }

}
