@Grab(group='org.codehaus.groovy.modules.http-builder',
            module='http-builder',
            version='0.7.2' )
import groovyx.net.http.*
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.TEXT

Console cons = System.console()
String username = new String(cons.readLine("[%s]",["User Name: "] as Object[]))
String password = new String(cons.readPassword("[%s]",["Password: "] as Object[]))

def artifactory = new HTTPBuilder("https://jfrog.myplace.com/artifactory/api/")
artifactory.auth.basic username, password

//Artifactory has its own custom response type, even though it's still JSON
//This will tell Groovy's HTTPBuilder library to use the normal JSON parser for it
artifactory.parser.'application/vnd.org.jfrog.artifactory.search.ArtifactUsageResult+json' = artifactory.parser.'application/json'
def time = (new Date() - 365).time

List objects = []
artifactory.get(path: "search/usage", 
            query: [notUsedSince: time, repos: 'grails-core-cache,grails-plugins-cache']
    ) { response, json ->
    objects = json."results"
}

println "Found ${objects.size()} objects that haven't been downloaded in a year."
def artifactoryStoreUrl = 'https://jfrog.myplace.com/artifactory/api/storage/'
def artifactoryStore = new HTTPBuilder(artifactoryStoreUrl)
//Gotta do it again for the artifactoryStore. Notably, this is a different response type.
artifactoryStore.parser.'application/vnd.org.jfrog.artifactory.storage.FileInfo+json' = artifactoryStore.parser.'application/json'
artifactoryStore.auth.basic username, password


objects.each{ object ->
    println "Will delete $object"
    def uri = object.uri
    String path = uri - artifactoryStoreUrl
    artifactoryStore.get(path: path) { resp, json ->
        println """Deleting object at $json.path from $json.repo.
                   This object was created $json.created"""
        println "It had a size of $json.size and checksums $json.checksums"
        def hitMe = new HTTPBuilder(json.downloadUri)
        hitMe.auth.basic username, password
        hitMe.request DELETE, { request ->
            response.success = { response, reader ->
                println "Response status is: $response.status"
                println "Response status line: $response.statusLine"
            }
        }
    }
}
