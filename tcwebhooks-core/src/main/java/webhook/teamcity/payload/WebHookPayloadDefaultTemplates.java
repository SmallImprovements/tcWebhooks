package webhook.teamcity.payload;

import java.util.Map;
import java.util.TreeMap;

public class WebHookPayloadDefaultTemplates {

	public static final String HTML_BUILDSTATUS_TEMPLATE = "buildStatusHtml";
	public static final String TEXT_TEMPLATE = "text";
	public static final String MESSAGE_TEMPLATE = "message";

	public static final String DEFAULT_HTML_BUILDSTATUS_TEMPLATE =
			"<span class=\"tcWebHooksMessage\">" +
            "<a href=\"${rootUrl}/project.html?projectId=${projectId}\">${projectName}</a>" +
			 " :: <a href=\"${rootUrl}/viewType.html?buildTypeId=${buildTypeId}\">${buildName}</a> # " +
			 "<a href=\"${rootUrl}/viewLog.html?buildTypeId=${buildTypeId}&buildId=${buildId}\"><strong>${buildNumber}</strong></a>" +
			 " - <strong>${buildResult}</strong>" +
             " </span>";

	public static Map<String,String> getDefaultEnabledPayloadTemplates(){
		Map<String,String> mT = new TreeMap<String, String>();
		mT.put(HTML_BUILDSTATUS_TEMPLATE, DEFAULT_HTML_BUILDSTATUS_TEMPLATE);
		return mT;
	}
}