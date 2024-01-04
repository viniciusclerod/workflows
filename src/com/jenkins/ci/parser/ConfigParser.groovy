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
            // jobValue.steps.each { skey, svalue ->
            //     Step step = new Step(
            //         name: 'run',
            //         command: "echo OK $skey $svalue"
            //     )
            //     job.steps.add(step);
            // }
            // job.steps = jobValue.steps.collect { stepKey, stepValue ->
            //     Step step = new Step(
            //         name: 'run',
            //         command: "echo OK $stepKey $stepValue"
            //     )
            //     return step
            // }
            job.steps = parseSteps(jobValue.steps)
            return job
        }
        return jobs
    }

    List<Step> parseSteps(def yamlSteps) {
        List<Step> steps = yamlSteps.collect { key, value ->
            // Step step = new Step(name: key, command: value)
            Step step = new Step(
                name: 'run',
                command: "echo OK $key $value"
            )
            return step
        }
        return steps
    }

}
