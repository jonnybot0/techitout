/**
 * Using this to copy jobs, based on advice from
 * http://jenkins-ci.361315.n4.nabble.com/How-to-Copy-Clone-a-Job-via-Groovy-td4097412.html
 */

(1..6).collect{"job$it"}.each{ appName->
    inst = jenkins.model.Jenkins.instance;
    def newJob = inst.copy(inst.getItem(appName), "$appName-test");
    newJob.save()
    //inst.getItem(appName).delete() //delete the job instead
}
