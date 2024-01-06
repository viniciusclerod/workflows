// src/com/jenkins/ci/reference/jobs/Job.groovy
package com.jenkins.ci.reference.jobs

import com.jenkins.ci.reference.commands.Command

class Job {
    String name
    HashMap environment = []
    List<Command> steps = []
}
