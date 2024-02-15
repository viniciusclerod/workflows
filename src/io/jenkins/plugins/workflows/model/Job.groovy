package io.jenkins.plugins.workflows.model

import io.jenkins.plugins.workflows.model.Step

class Job {

    String name
    Map environment = [:]
    List<Step> steps = []

    def execute(def ctx) {
        ctx.echo "job=(${this}) ${this.properties}"
        this.steps.each { step ->
            ctx.echo "step=(${step}) ${step.properties}"
            step.execute()
        }
    }

}
