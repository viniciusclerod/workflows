// src/com/jenkins/ci/reference/Configuration.groovy
package com.jenkins.ci.reference

import com.jenkins.ci.reference.jobs.Job

class Configuration {
    List<Job> jobs
    List<Stage> workflow
}
