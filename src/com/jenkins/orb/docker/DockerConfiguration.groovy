// src/com/jenkins/orb/docker/DockerConfiguration.groovy
package com.jenkins.orb.docker;

import com.jenkins.orb.ProjectConfiguration;

class DockerConfiguration {

    ProjectConfiguration config;

    def getImageName() {
        "${getReference()}:${getTag()}".toLowerCase();
    }

    def getBaseName() {
        "${config.projectName}".toLowerCase();
    }

    def getReference() {
        def env = config.env;
        "${getBaseName()}-${env.BRANCH_NAME}".toLowerCase();
    }

    def getTag() {
        def env = config.env;
        "${env.BUILD_ID}".toLowerCase();
    }
}
