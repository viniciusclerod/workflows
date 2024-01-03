// src/com/jenkins/orb/parser/ConfigParser.groovy
package com.jenkins.orb.parser;

import com.jenkins.orb.ProjectConfiguration;
import com.jenkins.orb.docker.DockerConfiguration;

class ConfigParser {

    private static String LATEST = 'latest';
    private static Integer DEFAULT_TIMEOUT = 600;   // 600 seconds

    static ProjectConfiguration parse(def yaml, def env) {
        ProjectConfiguration config = new ProjectConfiguration();

        config.buildNumber = env.BUILD_ID;

        // parse the execution steps
        config.steps = parseSteps(yaml.steps);

        // load the dockefile
        config.dockerfile = parseDockerfile(yaml.config);

        // load the project name
        config.projectName = parseProjectName(yaml.config);

        config.env = env;

        config.docker = new DockerConfiguration(config: config);

        config.timeout = yaml.timeout ?: DEFAULT_TIMEOUT;

        return config;
    }

    static def parseSteps(def yamlSteps) {
        List<Step> steps = yamlSteps.collect { k, v ->
            Step step = new Step(name: k)

            // a step can have one or more commands to execute
            v.each {
                step.commands.add(it);
            }
            return step
        }
        return new Steps(list: steps);
    }

    static def parseDockerfile(def config) {
        if (!config || !config["dockerfile"]) {
            return "Dockerfile";
        }

        return config["dockerfile"];
    }

    static def parseProjectName(def config) {
        if (!config || !config["project_name"]) {
            return "jenkins-project";
        }

        return config["project_name"];
    }
}
