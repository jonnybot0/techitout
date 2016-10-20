/**
 * Copy all jenkins jobs to a particular view
 */
import hudson.model.ListView

inst = jenkins.model.Jenkins.instance;
List jobs = ['job1', 'job2', 'job3', 'job4', 'job5', 'job6']
jobs.each{appName ->
    def job = inst.getItem("$appName")
    def testJob = inst.getItem("$appName-test")
    def view = new ListView(appName)
    inst.addView(view)
    view.add(job)
    view.add(testJob)
    view.save()
}
