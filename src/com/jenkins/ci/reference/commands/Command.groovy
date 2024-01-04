// src/com/jenkins/ci/reference/commands/Command.groovy
package com.jenkins.ci.reference.commands;

interface Command {
    String name
    String command
    def execute()
}
