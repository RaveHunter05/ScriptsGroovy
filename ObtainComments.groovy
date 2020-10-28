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
   prueba << x.getBody()
}

return prueba