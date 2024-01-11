// src/com/jenkins/ci/parser/ConfigParser.groovy
package com.jenkins.ci.parser

import com.jenkins.ci.helpers.MapHelper
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Job
import com.jenkins.ci.reference.Step
import com.jenkins.ci.reference.Command
import com.jenkins.ci.reference.Stage
import com.jenkins.ci.reference.Filter

class ConfigParser {

    static Configuration parse(def context, def yaml, def env) {
        Configuration config = new Configuration(context)
        config.environment = parseEnvironment(yaml.environment)
        config.commands = parseCommands(context, yaml.commands)
        config.jobs = parseJobs(context,yaml.jobs, config.commands)
        config.workflow = parseWorkflow(yaml.workflow)
        return config
    }

    static Map parseEnvironment(def yamlEnvironment) {
        Map environment = yamlEnvironment ?: [:]
        return environment
    }

    static Map parseCommands(def context, def yamlCommands) {
        Map builtInCommands = [
            run: new Command(
                context: context,
                name: 'sh',
                // steps: [new Step(
                //     name: 'sh',
                //     arguments: [
                //         script:
                //     ]
                // )]
            )
        ]
        // Map commands = yamlCommands.collectEntries { key, value ->
        //     return ["${key}": Command(context: context, name: 'sh')]
        // } ?: [:]
        return MapHelper.merge(builtInCommands, [:])
    }

    static List<Job> parseJobs(def context, def yamlJobs, Map commands) {
        List<Job> jobs = yamlJobs.collect { jobKey, jobVal ->
            Job job = new Job(name: jobKey)
            job.environment = jobVal.environment ?: [:]
            jobVal.steps.each { it ->
                String name = it.keySet().first()
                Map arguments = it[name] instanceof String ?
                    [script: it[name]] : it[name] as Map
                Step step = new Step(
                    name: name,
                    command: commands[name],
                    arguments: arguments
                )
                context.echo "arguments: ${arguments}"
                
                job.steps.add(step)
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
