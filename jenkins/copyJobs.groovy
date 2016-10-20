/** 
* Using this to copy jobs, based on advice from 
* http://jenkins-ci.361315.n4.nabble.com/How-to-Copy-Clone-a-Job-via-Groovy-td4097412.html
*/
import org.xml.sax.InputSource
import javax.xml.transform.sax.SAXSource

inst = jenkins.model.Jenkins.instance;
def starterJob = inst.getItem("job1")
(2..6).collect{"job$it"}.each{ appName->
    def newJob = inst.copy(starterJob, appName);

    //Update the git config for right repo

    //Get the XML config
    String xmlConfig = newJob.getConfigFile().asString()

    //Change the part of the XML config that has the repo
    String newConfig = xmlConfig.replaceAll('jonnybot0/tweet-pute.git',
                "jonnybot0/${appName.toLowerCase()}.git")

    //Perform black magic to convert the string to XML storage object
    def is = new InputSource(
            new ByteArrayInputStream(newConfig.getBytes())
    )
    def source = new SAXSource(is)
    newJob.updateByXml(source)
    newJob.save()
    //inst.getItem(appName).delete() //delete the job instead
}
