// src/com/jenkins/ci/reference/Command.groovy
package com.jenkins.ci.reference;

class Command {
    String name
    // String script
    // List<Step> steps = []
    Map arguments = [:]
    // String description

    def invoke(context) {
        context.invokeMethod(this.name, this.arguments)
    }
}
