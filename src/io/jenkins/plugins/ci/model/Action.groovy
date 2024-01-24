package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Job
import io.jenkins.plugins.ci.model.Filter

class Action {

    String name
    String type
    Job job
    Map<String,Filter> filters

    def execute() {
        this.job.execute()
    }

}