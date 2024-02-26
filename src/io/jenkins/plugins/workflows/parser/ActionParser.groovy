package io.jenkins.plugins.workflows.parser

import io.jenkins.plugins.workflows.model.Action
import io.jenkins.plugins.workflows.model.Configuration
import io.jenkins.plugins.workflows.model.Filter

class ActionParser {

    static List<Action> parse(def ctx, Configuration config, List list) {
        List actions = list.collect { it ->
            Action action = null
            switch (it) {
                case Map:
                    String key = it.keySet().first()
                    action = new Action(
                        name: it[key].name ?: key,
                        type: it[key].type ?: 'job',
                        job: config.jobs[key],
                        filters: it[key].filters?.collectEntries { rule, filter ->
                            [(rule): new Filter(
                                only: filter.only ?: null,
                                ignore: filter.ignore ?: null
                            )]
                        } ?: [:],
                        context: ActionParser.getContexts(it[key].context, config)
                    )
                    break
                default:
                    String key = it
                    action = new Action(
                        name: key,
                        type: 'job',
                        job: config.jobs[key]
                    )
            }
            return action
        }
        return actions as List<Action>
    }

    static List getContexts(def value, Configuration config) {
        switch (value) {
            case null:
                return []
            case List:
                return value.collect { it ->
                    config.contexts[it]
                }
            default:
                return [config.contexts[value]]
        }
    }

}
