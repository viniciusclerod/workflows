package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Command

class Step {

    def ctx // TODO: REMOVE

    Command command
    def arguments

    def execute(def parameters = null, def ctx = null) {
        def arguments = this.arguments
        if (parameters) {
            if (ctx) this.ctx = ctx // TODO: REMOVE
            // if (this.ctx) this.ctx.echo "BEGIN arguments=${arguments} parameters=${parameters}" // TODO: REMOVE
            arguments = this.parseArguments([ parameters: parameters ], arguments)
            // if (this.ctx) this.ctx.echo "END arguments=${arguments} parameters=${parameters}" // TODO: REMOVE
        }
        this.command.execute(arguments)
    }

    def parseArguments(def context, def arguments) {
        switch (arguments) {
            case Map:
                return arguments.collectEntries { it ->
                    def value = it.value instanceof String ? this.parseArgument(context, it.value) : it.value
                    // if (this.ctx) this.ctx.echo "context=${context} it.value=${it.value} value=${value}" // TODO: REMOVE
                    return [(it.key): value]
                }
            case String:
                return this.parseArgument(context, arguments)
            default:
                return arguments
        }
    }

    def parseArgument(def context, String text) {
        return text.replaceAll(/<<\s*([\S]+)\s*>>/) { match ->
            def keyList = match[1].split("\\.")
            def findValueFromMap = { map, keys ->
                if (this.ctx) this.ctx.echo "map=${map}\nkeys=${keys}\nmap[keys[0]]=${map[keys[0]]}" // TODO: REMOVE
                if (keys.size() == 1) return map[keys[0]]
                else return findValueFromMap(map[keys[0]], keys[1..-1])
            }
            return findValueFromMap(context, keyList)
        }
    }

}