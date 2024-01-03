// src/com/jenkins/orb/steps/Steps.groovy
package com.jenkins.orb.steps;

class Steps {
    List<Step> list;

    def getVar(def dockerImage) {
        return "buildSteps"
    }
}
