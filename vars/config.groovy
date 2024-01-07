@Library('jenkins-orb')
import com.jenkins.orb.ProjectConfiguration
import com.jenkins.orb.parser.ConfigParser

// TODO: DEPRECATED
def call(String yamlPath) {
  def yaml = readYaml file: yamlPath
  
  // load project's configuration
  ProjectConfiguration config = ConfigParser.parse(yaml, env)

  def imageName = config.docker.getImageName()

  // build the image specified in the configuration
  def customImage = docker.build(imageName, "--file ${config.dockerfile} .")

  // adds the last step of the build.
  def closure = buildSteps(config, customImage);

  // we execute the top level closure so that the cascade starts.
  try {
      closure([:]);
  } finally{
      deleteDockerImages(config);
  }

}
