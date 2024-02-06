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
                        } ?: [:]
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

}
