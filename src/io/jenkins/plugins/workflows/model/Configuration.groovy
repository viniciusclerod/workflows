package io.jenkins.plugins.workflows.model

import io.jenkins.plugins.workflows.helper.BuiltInHelper
import io.jenkins.plugins.workflows.model.Context
import io.jenkins.plugins.workflows.model.Command
import io.jenkins.plugins.workflows.model.Credential
import io.jenkins.plugins.workflows.model.Step
import io.jenkins.plugins.workflows.model.Workflow


class Configuration {
    
    Map options = BuiltInHelper.options
    Map environment = [:]
    Map<String,Context> contexts = [:]
    Map<String,Command> commands = [:]
    Map<String,Job> jobs = [:]
    Map<String,Workflow> workflows = [:]

}
