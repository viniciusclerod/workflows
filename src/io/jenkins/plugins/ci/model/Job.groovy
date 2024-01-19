package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Step

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
