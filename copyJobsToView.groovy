inst = jenkins.model.Jenkins.instance; 
List apps = ['job1', 'job2', 'job3', 'job4', 'job5', 'job6']
apps.each{appName ->
  def job = inst.getItem("$appName-production")
  def view = Hudson.instance.getView(appName)
  view.add(job)
}
