/**
* This script depends on the JIRA Plugin. 
* https://wiki.jenkins-ci.org/display/JENKINS/JIRA+Plugin
*/
import hudson.plugins.jira.JiraIssueUpdater
import hudson.plugins.jira.JiraVersionCreator
import hudson.plugins.jira.JiraReleaseVersionUpdater

def destinationJobs = Hudson.instance.items.findAll{
    it.name.contains('keyword')
}

destinationJobs.each{ job ->
    println "Adding JIRA notifier for $job.name"
    job.addPublisher(new JiraIssueUpdater())
    job.save()
}
