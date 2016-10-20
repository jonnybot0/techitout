/** 
* Using this to copy jobs, based on advice from 
* http://jenkins-ci.361315.n4.nabble.com/How-to-Copy-Clone-a-Job-via-Groovy-td4097412.html
*/

(1..6).collect{"job$it"}.each{ appName->
    inst = jenkins.model.Jenkins.instance; 
    def newJob = inst.copy(inst.getItem("starter"), appName); 

    //Update the git config for right repo

    //Get the XML config
    String xmlConfig = newJob.getConfigFile().asString()

    //Change the part of the XML config that has the repo
    String newConfig = xmlConfig.replaceAll('project/repo.git',
                "stmis/${appName.toLowerCase()}.git")

    //Perform black magic to convert the string to XML storage object
    def is = new InputSource(new ByteArrayInputStream(newConfig.getBytes()))
    def source = new SAXSource(is)
    newJob.updateByXml(source)
    newJob.save()
}
