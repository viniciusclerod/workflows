// src/com/jenkins/ci/parser/ConfigParser.groovy
package com.jenkins.ci.parser

import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.commands.*

class ConfigParser {

    static Configuration parse(def yaml, def env) {
        Configuration config = new Configuration();
        
        config.jobs = parseJobs(yaml.jobs);

        return config;
    }

    static List<Job> parseJobs(def yamlJobs) {
        List<Job> jobs = yamlJobs.collect { key, value ->
            Job job = new Job(name: key)
            job.steps = parseSteps(value.steps)
            return job
        }
        return jobs
    }

    static List<Command> parseSteps(def yamlSteps) {
        List<Command> steps = yamlSteps.collect { key, value ->
            Command step = null
            switch (x) {
                case "run":
                    step = new RunCommand(name: key, command: value)
                    break
            }
            return step
        }
        return steps.findAll {it != null}
    }

}
