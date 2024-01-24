package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Job

class Action {

    String name
    String type
    Job job
    Map filters

    def execute() {
        this.job.execute()
    }

}