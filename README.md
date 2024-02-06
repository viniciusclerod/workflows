# Workflows Jenkins Plugin

This a Jenkins library built to make it easier to configure pipelines without necessarily knowing about Jenkinsfile syntax.

When using this library, your Jenkinsfile should look something like this:

```groovy
@Library('workflows') _
config '.jenkins/config.yml'
```
It basically loads the library, clones the target repository and calls `config` to make it's magic.
As an argument, `config` receives the path to a configuration yaml file.

This file looks something like this:

```yaml
environment:
  VERSION: $(jq -r .version $CONTEXT/package.json)

commands:
  build_image:
    parameters:
      image:
        description: Name of image to build
        type: string
      label:
        default: Docker Build
        description: Specify a custom step name for this command, if desired
        type: string
      tag:
        default: $COMMIT_SHA1
        description: Image tag, defaults to the value of $COMMIT_SHA1
        type: string
    steps:
      - sh:
          label: <<parameters.label>>
          script: |
            #!/usr/bin/env bash
            docker build --tag=<<parameters.image>>:<<parameters.tag>>
jobs:
  build:
    environment:
      DOCKER_BUILDKIT: 1
    steps:
      - build_image:
          label: Build Docker Image
          image: $PROJECT_REPONAME
          tag: $VERSION

workflows:
  deployment:
    jobs:
      - build:
          name: Build
          filters:
            pull:
              only: ^(feature|release|hotfix)/.*
            branches:
              only: ^(feature|release|hotfix)/.*
```