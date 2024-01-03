@Library('jenkins-orb')
import orb.ProjectConfiguration;
import orb.parser.ConfigParser;

def call(String yamlPath) {
  def yaml = readYaml file: yamlName;

  // load project's configuration
  ProjectConfiguration projectConfig = ConfigParser.parse(yaml, env);

  def imageName = projectConfig.dockerConfiguration.imageName().toLowerCase();

  // build the image specified in the configuration
  def customImage = docker.build(imageName, "--file ${projectConfig.dockerfile} .");

}