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

// Get comments

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def cField = customFieldManager.getCustomFieldObjectByName("custom_field_test")

def issueManager = ComponentAccessor.getIssueManager()


def issue = issueManager.getIssueByCurrentKey("TES-548")

def issueValue = ComponentAccessor.getCommentManager().getComments(issue)

def prueba = []
def margarita = []
issueValue.each{x->
   def body = x.getBody()
   def author = x.getAuthorFullName()
   def updatedDate = x.getUpdated()
   def contactValue = 'not phone founded'
   def contact = (body =~ /\d{10}/).findAll()
   def email = 'no email founded'
   def foundEmail = (body =~ /[a-zA-Z0-9]*\@[a-zA-Z0-9]*\.[a-zA-Z]*/).findAll()
   
   def realBody  = (body =~ /([^*]+)/)[0]
    
    contact.each{y->
    	if(y.length() > 0){contactValue = y}
    }
    
    foundEmail.each{z->
        if(z.length() > 0){email = z}
    }
    
	
    if(email != 'no email founded' || contactValue != 'not phone founded'){
        prueba << [
            "body":realBody[0], 
            "author":author , 
            "contact_phone":contactValue,
            "contact_email": email,
            "issue_name": issue.toString(),
            "updated_date": updatedDate
    	]
    }
}

// From here we have all the code for the api call

def baseURL = "https://apitest691.herokuapp.com/api/";

def endpoint = "comment";



def body_req = [
    "body": "body xd",
    "author": "Lord Valdomero",
    "contact_phone": "25421380"
]



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


//Regex: (.|\n)*?(?=.*:)


