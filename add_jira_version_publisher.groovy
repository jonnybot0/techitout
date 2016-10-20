/*
constructed the map below by using this script, then manually correcting errors:
def prodJobs = Hudson.instance.items.findAll{it.name.contains('-production')}.collectEntries{[(it.name): "G${it.name[0..2].toUpperCase()}"]}
println prodJobs
*/
def jobsToProjects = ['happy-app-production':'HAPP', 
    'snappy-app-production':'SNAP',
    'crappy-app-production':'CRAP',
    'flappy-app-production':'FLAP',
    'scrappy-app-production':'SCRAP',
    'trappy-app-production':'TRAP',
    'tappy-app-production':'TAPP']

def prodJobs = Hudson.instance.items.findAll{
    it.name.contains('keyword')
}

prodJobs.each{ job ->
    println "Adding jira version creator to job $job.name"
    job.addPublisher(
        new JiraVersionCreator('1.0.$BUILD_NUMBER', jobsToProjects[job.name])
    )
    println "Adding release notifier for $job.name"
    job.addPublisher(
        new JiraReleaseVersionUpdater(jobsToProjects[job.name], '1.0.$BUILD_NUMBER')
    )
    job.save()
}
