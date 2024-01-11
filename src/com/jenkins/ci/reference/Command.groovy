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
        if (this.steps.isEmpty()) {
            this.context.invokeMethod(this.name, arguments)
        } else {
            this.steps.each { step -> step.call() }
        }
    }
}
