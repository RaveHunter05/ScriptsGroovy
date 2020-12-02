import groovy.json.JsonSlurper;
import groovy.json.StreamingJsonBuilder;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;

// Get last comment

def commentManager = ComponentAccessor.getCommentManager();

def issueManager = ComponentAccessor.getIssueManager()

def issue = issueManager.getIssueByCurrentKey("TES-548")

def issueValue = commentManager.getLastComment(issue);

def prueba = []

issueValue.each{x->
   def body = x.getBody()
   def author = x.getAuthorFullName()
   def updatedDate = x.getUpdated()
   def contactValue = "no phone found"
   def contact = (body =~ /\d{10}/).findAll()
   def realBody  = (body =~ /([^*]+)/)[0]
    contact.each{y->
    	if(y.length() > 0){contactValue = y}
    }
    
    def testLol = []
   
   prueba << [
        "body":realBody[0], 
        "author":author , 
        "contact_phone":contactValue,
    	"updated_date": updatedDate
    ]
	
}


// From here we have all the code for the api call

def baseURL = "https://apitest691.herokuapp.com/api/";

def endpoint = "comment";



/* 
	def body_req = [
    "body": prueba[0].body,
    "author": prueba[0].author,
    "contact_phone": prueba[0].contact_phone,
    "updated_date": prueba[0].updated_date
]
*/



prueba.each{y->
    URL url;

	url = new URL(baseURL + endpoint);
    HttpURLConnection connection = url.openConnection() as HttpURLConnection;
    connection.requestMethod = "POST";
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Accept", "application/json");
    connection.doOutput = true;
	connection.outputStream.withWriter("UTF-8") { new StreamingJsonBuilder(it, y) }
	connection.connect();
    def json = null;

    try {
        json = new JsonSlurper().parse(connection.getInputStream())
        return json;
    } catch(all) {
        log.error("Failure on REST GET to ${endpoint}\n${connection.getResponseMessage()}")
    }
}


