package io.jenkins.plugins.workflows.model

import io.jenkins.plugins.workflows.model.Step

class Job {

    String name
    Map environment = [:]
    List<Step> steps = []

    def execute() {
        this.steps.each { step ->
            step.execute()
        }
    }

}
