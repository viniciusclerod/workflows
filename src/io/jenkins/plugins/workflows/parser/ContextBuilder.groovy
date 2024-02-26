package io.jenkins.plugins.workflows.parser

import io.jenkins.plugins.workflows.model.Configuration
import io.jenkins.plugins.workflows.model.Context
import io.jenkins.plugins.workflows.model.Credential

class ContextBuilder {

    static void build(def ctx, Configuration config, Map map) {
        map.each { key, list ->
            Context context = new Context(
                name: key,
                environment: ContextBuilder.extractEnvironment(list),
                credentials: ContextBuilder.extractCredentials(list)
            )
            config.contexts[key] = context
        }
    }

    static Map extractEnvironment(List list) {
        Map map = list
            .findAll { it.type == 'environment' }
            .collectEntries { it.variables } as Map
        return map ?: [:]
    }

    static Map extractCredentials(List list) {
        List credentials = list
            .findAll { it.type == 'credential' }
            .collect { it -> 
                String type = it.keySet().find { it != 'type' }
                return new Credential(
                    type: type,
                    parameters: it[type] as Map
                )
            }
        return credentials ?: []
    }

}
