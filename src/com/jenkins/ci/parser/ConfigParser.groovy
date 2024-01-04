// src/com/jenkins/ci/parser/ConfigParser.groovy
package com.jenkins.ci.parser

import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.steps.Step

class ConfigParser {

    static Configuration parse(def yaml, def env) {
        Configuration config = new Configuration();
        
        config.jobs = parseJobs(yaml.jobs);

        return config;
    }

    static List<Job> parseJobs(def yamlJobs) {
        List<Job> jobs = yamlJobs.collect { jobKey, jobValue ->
            Job job = new Job(name: jobKey)
            job.steps = jobValue.steps.collect { stepKey ->
                Step step = null
                if (stepKey.containsKey('run')) {
                    step = new Step(
                        name: 'run',
                        command: stepKey.run
                    )
                }
                return step
            }
            return job
        }
        return jobs
    }

}
