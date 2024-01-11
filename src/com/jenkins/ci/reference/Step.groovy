// src/com/jenkins/ci/reference/Step.groovy
package com.jenkins.ci.reference

class Step {
    String type
    String name
    Command command
    def arguments
    def call() {
        command.call()
    }
}
