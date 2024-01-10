// src/com/jenkins/ci/reference/Job.groovy
package com.jenkins.ci.reference

import com.jenkins.ci.reference.Step

class Job {
    String name
    Map environment = [:]
    List<Step> steps = []
}
