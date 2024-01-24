package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Command
import io.jenkins.plugins.ci.model.Step
import io.jenkins.plugins.ci.model.Workflow

class Configuration {
    
    Map environment = [:]
    Map<String,Command> commands = [:]
    Map<String,Job> jobs = [:]
    Map<String,Workflow> workflows = [:]

}
