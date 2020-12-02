import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import groovy.xml.MarkupBuilder
import com.atlassian.jira.project.Project

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def cField = customFieldManager.getCustomFieldObjectByName("custom_field_test")

def keyValue = context.projectKey.toString()

def projectObject = ComponentAccessor.getProjectManager().getProjectByCurrentKey(keyValue)
def issuesIds = ComponentAccessor.getIssueManager().getIssueIdsForProject(projectObject.id)

def issueManager = ComponentAccessor.getIssueManager()


def fin = """
	<section class="section-fields">
	<h1> Values from custom fields ${keyValue} </h1>
    <hr/>
    <table class="scriptField">
    	<tr>
        	<th> Issue </th>
            <th> Value </th>
        </tr>
"""


    issuesIds.each {issueId ->
    def issueKey = ComponentAccessor.getIssueManager().getIssueObject(issueId)
    def issue = issueManager.getIssueByCurrentKey(issueKey.toString())
    def value = issue.getCustomFieldValue(cField).toString()
    if(value == 'null'){
   		return
    }else{
        fin += '<tr>'
        fin += '<td>' + issue + '</td>'
        fin += '<td>' + value + '</td>'
        fin += '</tr>'
    }
}

fin += '</table> </section>'

fin += '''
	<style>
    	th{
        	background-color:#0258FA;
            color: #fff
        }
        .scriptField, .scriptField *{
                border: 1px solid black;
            }

            .scriptField{
                border-collapse: collapse;
              	margin: 1rem 2rem;
            }
            .section-fields{
            	margin: 1rem 2rem;
            }
            td, th{
            	padding: 1rem;
            }
    </style>
'''

writer.write(fin)