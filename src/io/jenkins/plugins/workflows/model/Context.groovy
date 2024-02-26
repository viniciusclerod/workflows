package io.jenkins.plugins.workflows.model

import io.jenkins.plugins.workflows.model.Credential

class Context {

    String name
    List<Credential> credentials = []
    Map environment = [:]

}
