@Library('jenkins-orb')
import com.jenkins.orb.ProjectConfiguration

def call(ProjectConfiguration config) {
    def reference = config.docker.getReference();
    try {
        sh "docker images --filter 'reference=${reference}*' --format \"{{.Tag}} {{.Repository}}:{{.Tag}}\" | sort -n | sed '\$d' | awk '{ print \$2 }' | xargs --no-run-if-empty docker rmi"
    } catch(ignored) {
        // this would make the entire popeline fail. We don't want that
        println ignored
    }
}
