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
            // jobValue.steps.each { step, svalue ->
            //     Step step = new Step(
            //         name: 'run',
            //         command: "echo OK $skey $svalue"
            //     )
            //     job.steps.add(step);
            // }
            job.steps = jobValue.steps.collect { stepKey ->
                sh("ls -la")
                Step step = new Step(
                    name: 'run',
                    command: "echo OK $stepKey"
                )
                return step
            }
            return job
        }
        return jobs
    }

}
