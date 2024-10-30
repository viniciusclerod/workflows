package io.jenkins.plugins.workflows.helper

class BuiltInHelper {

    static Map options = [
        timeout: [ time: 1, unit: 'HOURS']
    ]

    static Map environment = [
        'PROJECT_REPONAME': '$(git config --local remote.origin.url | sed -n \'s#.*/\\([^.]*\\)\\.git#\\1#p\')',
        'PROJECT_USERNAME': '$(git config --local remote.origin.url | sed -n \'s#.*[:|/]\\([^/]*\\)/[^.]*\\.git#\\1#p\')',
        'COMMIT_SHA1': '$(git rev-parse HEAD)',
        'COMMIT_SHA1_SHORT': '$(git rev-parse --short HEAD)'
    ]

}
