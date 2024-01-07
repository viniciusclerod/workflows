// src/com/jenkins/ci/reference/Configuration.groovy
package com.jenkins.ci.reference

import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.workflow.Stage

class Configuration {
    Map environment = [:]
    List<Job> jobs = []
    List<Stage> workflow = []
}
