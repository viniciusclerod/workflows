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
        List<Stage> stages = yamlWorkflow.collect { it ->
            Stage stage = null
            if (it instanceof String) {
                stage = new Stage(
                    key: it,
                    name: 'Simple'
                )
            } else {
                stage = new Stage(
                    key: it,
                    name: 'Complex'
                )
                // stage = new Stage(
                //     key: key,
                //     name: value.name ?: key,
                //     type: value.type ?: null,
                //     branches: value.branches ?: null
                // )
            }
            return stage
        }
        return stages
    }

}
