package io.jenkins.plugins.workflows.model

import io.jenkins.plugins.workflows.helper.MapHelper
import io.jenkins.plugins.workflows.model.Job
import io.jenkins.plugins.workflows.model.Filter
import io.jenkins.plugins.workflows.model.Context

class Action {

    String name
    String type
    Job job
    Map<String,Filter> filters
    List context

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

    Map getContextEnvironment() {
        Map environment = [:]
        this.context.each { it ->
            environment = MapHelper.merge(environment, it.environment)
        }
        return environment
    }

    List getContextCredentials() {
        List credentials = []
        this.context.each { it ->
            credentials.addAll(it.credentials)
        }
        return credentials
    }

}
