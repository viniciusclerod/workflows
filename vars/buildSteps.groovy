@Library('jenkins-orb')
import com.jenkins.orb.ProjectConfiguration
import com.jenkins.orb.steps.Step;

def call(ProjectConfiguration config, def dockerImage) {
    return { variables ->
        List<Step> stepList = config.steps.list
        def links = variables.collect { k, v -> "--link ${v.id}:${k}" }.join(" ")
        dockerImage.inside(links) {
            stepList.each { step ->
                stage(step.name) {
                    step.commands.each { command ->
                        sh command
                    }
                }
            }
        }
    }
}
