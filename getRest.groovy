import groovy.json.JsonSlurper;
import groovy.json.StreamingJsonBuilder;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue
import org.apache.commons.codec.binary.Base64;

def baseURL = "https://apitest691.herokuapp.com/api/";

def endpoint = "comment";

URL url;

url = new URL(baseURL + endpoint);

HttpURLConnection connection = url.openConnection() as HttpURLConnection;
connection.requestMethod = "GET";
connection.setRequestProperty("Content-Type", "application/json");
connection.setRequestProperty("Accept", "application/json");
connection.doOutput = true;
connection.connect();

def json = null;

try {
	json = new JsonSlurper().parse(connection.getInputStream())
	return json;
} catch(all) {
	log.error("Failure on REST GET to ${endpoint}\n${connection.getResponseMessage()}")
}
