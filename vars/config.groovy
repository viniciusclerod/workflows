@Library('jenkins-orb')
import com.jenkins.orb.*

def call(String yamlPath) {
  def yaml = readYaml file: yamlPath

  Foo foo = new Foo()
  foo.buildNumber = env.BUILD_ID;
  
  sh "echo ${foo.buildNumber}"

  // load project's configuration
  // ProjectConfiguration projectConfig = ConfigParser.parse(yaml, env)

  // def imageName = projectConfig.dockerConfiguration.imageName().toLowerCase()

  // build the image specified in the configuration
  // def customImage = docker.build(imageName, "--file ${projectConfig.dockerfile} .")

  // println "customImage: ${customImage}"
}
