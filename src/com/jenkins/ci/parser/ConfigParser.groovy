// src/com/jenkins/ci/parser/ConfigParser.groovy
package com.jenkins.ci.parser

import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.steps.Step
import com.jenkins.ci.reference.workflow.Stage

class ConfigParser {

    static Configuration parse(def yaml, def env) {
        Configuration config = new Configuration();
        config.jobs = parseJobs(yaml.jobs);
        config.workflow = parseWorkflow(yaml.workflow);
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

    static List<Job> parseWorkflow(def yamlWorkflow) {
        List<Stage> stages = yamlWorkflow.collect { key, value ->
            Stage stage = null
            if (value != null) {
                stage = new Stage(
                    key: key,
                    type: value.type ?: null,
                    name: value.name ?: key,
                    branches: value.branches ?: null
                )
            } else {
                stage = new Stage(
                    key: key,
                    name: 'Unnamed Stage'
                )
            } 
            return stage
        }
        return stages
    }

}
