//Update Git target


import hudson.plugins.git.GitSCM
import hudson.plugins.git.BranchSpec
def prodJobs = Hudson.instance.items.findAll{
    it.name.contains('job')
}

prodJobs.each{ job ->
    def scm = job.scm
    def newScm = new GitSCM(scm.userRemoteConfigs,
            [new BranchSpec('new-master')],
            scm.doGenerateSubmoduleConfigurations,
            scm.submoduleCfg,
            scm.browser,
            scm.gitTool,
            scm.extensions)
    job.scm = newScm
    job.save()
}

