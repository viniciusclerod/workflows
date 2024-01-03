package orb;

import orb.docker.DockerConfiguration;

class ProjectConfiguration {
    def dockerfile;
    def projectName;
    def buildNumber;
    DockerConfiguration dockerConfiguration;
    def env;
    def timeout;
}
