// src/com/jenkins/ci/reference/Job.groovy
package com.jenkins.ci.reference

import com.jenkins.ci.reference.Command

class Job {
    String name
    Map environment = [:]
    List<Command> steps = []
}
