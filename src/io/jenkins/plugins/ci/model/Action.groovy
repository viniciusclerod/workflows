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

    Boolean shouldRun(def env) {
        Boolean proceed = true
        if (this.filters) {
            if (env.CHANGE_ID != null) {
                if (this.filters.pull && this.filters.pull.ignore) {
                    Boolean abort = (env.CHANGE_BRANCH =~ this.filters.pull.ignore).matches()
                    if (abort) return false
                }
                if (this.filters.pull && this.filters.pull.only) {
                    proceed &= (env.CHANGE_BRANCH =~ this.filters.pull.only).matches()
                }
            } else {
                if (this.filters.branches && this.filters.branches.ignore) {
                    Boolean abort = (env.BRANCH_NAME =~ this.filters.branches.ignore).matches()
                    if (abort) return false
                }
                if (this.filters.branches && this.filters.branches.only) {
                    proceed &= (env.BRANCH_NAME =~ this.filters.branches.only).matches()
                }
            }
        }
        return proceed
    }

}
