// src/com/jenkins/ci/reference/Command.groovy
package com.jenkins.ci.reference

import com.jenkins.ci.reference.Step

class Command {
    def context
    String name
    List<Step> steps = []
    // Map parameters = [:]
    // String description

    def call(arguments) {
        if (this.steps) {
            this.steps.each { step -> step.call() }
        } else {
            this.context.invokeMethod(this.name, arguments)
        }
    }
}
