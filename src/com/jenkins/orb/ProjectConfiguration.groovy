// src/com/jenkins/orb/ProjectConfiguration.groovy
package com.jenkins.orb

import com.jenkins.orb.docker.DockerConfiguration;
import com.jenkins.orb.steps.Steps;

class ProjectConfiguration {
    def projectName;
    def dockerfile;
    Steps steps;
    def buildNumber;
    DockerConfiguration docker;
    def env;
    def timeout;
}
