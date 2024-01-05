// src/com/jenkins/ci/parser/ConfigParser.groovy
package com.jenkins.ci.parser

import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.commands.Command
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
            job.steps = jobValue.steps.collect { step ->
                String key = step.keySet().first()
                switch(key) {
                    case 'run':
                        Command command = new Command(
                            type: 'sh',
                            name: step[key].containsKey('name') ? step[key].name : 'Shell Script',
                            command: step[key].containsKey('command') ? step[key]?.command : step[key]
                        )
                        return command
                }
                return null
            }.findAll { it != null }
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
                    name: it
                )
            } else {
                String key = it.keySet().first()
                stage = new Stage(
                    key: key,
                    name: it[key].name ?: key,
                    type: it[key].type ?: null,
                    branches: it[key].branches ?: null
                )
            }
            return stage
        }
        return stages
    }

}
