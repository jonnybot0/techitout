@BaseScript CustomEndpointDelegate delegate

def priorityManager = ComponentAccessor.getComponent(PriorityManager)
def baseUrls = ComponentAccessor.getComponent(JiraBaseUrls)
ComponentManager.instance

priority(
        httpMethod: "POST", groups: ["jira-administrators"]
) {MultivaluedMap queryParams, String body ->

    def mapper = new ObjectMapper()
    def bean = mapper.readValue(body, PriorityJsonBean)
    assert bean.name // must provide priority name
    assert bean.description // must provide priority description
    assert bean.iconUrl // must provide priority icon url
    assert bean.statusColor // must provide priority statusColor

    Priority priority
    try {
        priority = priorityManager.createPriority(bean.name, bean.description, bean.iconUrl, bean.statusColor)
    } catch (e) {
        return Response.serverError().entity([error: e.message]).build();
    }

    return Response.created(new URI("/rest/api/2/priority/${priority.id}")).build();
}