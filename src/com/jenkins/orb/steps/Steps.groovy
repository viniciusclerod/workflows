// src/com/jenkins/orb/steps/Steps.groovy
package com.jenkins.orb.steps;

import com.jenkins.orb.steps.Step;

class Steps {
    List<Step> list;

    def getVar(def dockerImage) {
        return "buildSteps"
    }
}
