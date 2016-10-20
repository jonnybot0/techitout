def sourcejob = Hudson.instance.items.find{it.name='jobWithNewJDK'}
def jdk = sourcejob.getJDK()

def otherJobs = Hudson.instance.items.findAll {
    (it.name.contains('-production') || it.name.contains('-development')) 
            && it.name != ('jobWithNewJDK')   //exclude fixed job
            && !it.name.contains('restart')   //exclude restart task
            && !it.name.contains('deploy')    //exclude deployment task
}

otherJobs.each{
  println "Setting JDK for $it.name to $jdk"
  it.setJDK(jdk)
  it.save()
}
