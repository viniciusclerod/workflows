// src/com/jenkins/ci/parser/ConfigParser.groovy
package com.jenkins.ci.parser

import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job

class ConfigParser {

    static Configuration parse(def yaml, def env) {
        Configuration config = new Configuration();
        
        config.jobs = parseJobs(yaml.jobs);

        return config;
    }

    static def parseJobs(def yamlJobs) {
        List<Job> jobs = yamlJobs.collect { k, v ->
            Job job = new Job(name: k)
            v.each {
                job.steps.add(it);
            }
            return step
        }
        return jobs
    }
}
