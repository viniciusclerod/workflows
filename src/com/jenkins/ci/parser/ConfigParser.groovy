// src/com/jenkins/ci/parser/ConfigParser.groovy
package com.jenkins.ci.parser

import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.commands.*

static String toCamelCase( String text, boolean capitalized = false ) {
    text = text.replaceAll( "(_)([A-Za-z0-9])", { Object[] it -> it[2].toUpperCase() } )
    return capitalized ? capitalize(text) : text
}

class ConfigParser {

    static Configuration parse(def yaml, def env) {
        Configuration config = new Configuration();
        
        config.jobs = parseJobs(yaml.jobs);

        return config;
    }

    static List<Job> parseJobs(def yamlJobs) {
        List<Job> jobs = yamlJobs.collect { key, value ->
            Job job = new Job(name: key)
            job.steps = parseSteps(value.steps)
            return job
        }
        return jobs
    }

    static List<Command> parseSteps(def yamlSteps) {
        List<Command> steps = yamlSteps.collect { key, value ->
            String className = toCamelCase(key, true)
            Command step = this.class.classLoader.loadClass( className, true, false )?.newInstance(name: key, command: value)
            return step
        }
        return steps
    }

}
