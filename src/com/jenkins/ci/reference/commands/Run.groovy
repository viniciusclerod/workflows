// src/com/jenkins/ci/reference/commands/Run.groovy
package com.jenkins.ci.reference.commands;

import com.jenkins.ci.reference.commands.Command;

class Run implements Command {
    String name
    String command
    def execute() {
        sh command
    }
}
