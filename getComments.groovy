import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def cField = customFieldManager.getCustomFieldObjectByName("custom_field_test")

def issueManager = ComponentAccessor.getIssueManager()


def issue = issueManager.getIssueByCurrentKey("TES-548")

def issueValue = ComponentAccessor.getCommentManager().getComments(issue)

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

return prueba