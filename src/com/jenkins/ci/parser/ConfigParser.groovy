// src/com/jenkins/ci/parser/ConfigParser.groovy
package com.jenkins.ci.parser

import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Job
import com.jenkins.ci.reference.Step
import com.jenkins.ci.reference.Stage
import com.jenkins.ci.reference.Filter

class ConfigParser {

    static Configuration parse(def context, def yaml, def env) {
        Configuration config = new Configuration(context)
        config.environment = parseEnvironment(yaml.environment)
        config.jobs = parseJobs(yaml.jobs)
        config.workflow = parseWorkflow(yaml.workflow)
        return config
    }

    static List<Job> parseEnvironment(def yamlEnvironment) {
        Map environment = yamlEnvironment ?: [:]
        return environment
    }

    static List<Job> parseJobs(def yamlJobs) {
        List<Job> jobs = yamlJobs.collect { jobKey, jobVal ->
            Job job = new Job(name: jobKey)
            job.environment = jobVal.environment ?: [:]
            job.steps = jobVal.steps.collect { it ->
                String key = it.keySet().first()
                switch(key) {
                    case 'run':
                        Step step = new Step(
                            type: 'sh',
                            name: 'Shell Script'
                        )
                        if (it[run] instanceof String) {
                            step.command = step.run
                        } else {
                            step.name = it[run].name ?: step.name
                            step.command = it[run].command
                        }
                        return step
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
                    filters: [:]
                )
                it[key].filters.each { rule, filter ->
                    stage.filters[rule] = new Filter(
                        only: filter.only ?: null,
                        ignore: filter.ignore ?: null
                    )
                }
            }
            return stage
        }
        return stages
    }

}
