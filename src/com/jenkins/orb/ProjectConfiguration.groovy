package com.jenkins.orb;

import com.jenkins.orb.docker.DockerConfiguration;

class ProjectConfiguration {
    def dockerfile;
    def projectName;
    def buildNumber;
    DockerConfiguration dockerConfiguration;
    def env;
    def timeout;
}
