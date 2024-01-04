// src/com/jenkins/ci/reference/commands/RunCommand.groovy
package com.jenkins.ci.reference.commands;

import com.jenkins.ci.reference.commands.Command;

class RunCommand implements Command {
    String name
    String command
    def execute() {
        return "sh ${this.command}"
    }
}
