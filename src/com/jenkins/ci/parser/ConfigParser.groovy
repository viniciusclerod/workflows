// src/com/jenkins/ci/parser/ConfigParser.groovy
package com.jenkins.ci.parser

import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Job
import com.jenkins.ci.reference.Command
import com.jenkins.ci.reference.Stage
import com.jenkins.ci.reference.Filter

class ConfigParser {

    static Configuration parse(def context, def yaml, def env) {
        context.sh "echo TESTE DE CONTEXTO"
        Configuration config = new Configuration();
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
            job.steps = jobVal.steps.collect { step ->
                String key = step.keySet().first()
                switch(key) {
                    case 'run':
                        Command command = new Command(
                            type: 'sh',
                            name: 'Shell Script'
                        )
                        if (step.run instanceof String) {
                            command.command = step.run
                        } else {
                            command.name = step.run.name ?: command.name
                            command.command = step.run.command
                        }
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
                    filters: [:]
                )
                it[key].filters.each { rule, filter ->
                    switch(rule) {
                        case 'pull_request':
                            stage.filters[rule] = filter as Boolean
                        break
                        case 'branches':
                            stage.filters[rule] = new Filter(
                                only: filter.only ?: null,
                                ignore: filter.ignore ?: null
                            )
                        break
                    }
                    
                }
            }
            return stage
        }
        return stages
    }

}
