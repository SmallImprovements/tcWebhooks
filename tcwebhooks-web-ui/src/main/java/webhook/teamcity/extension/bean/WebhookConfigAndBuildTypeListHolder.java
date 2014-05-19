package webhook.teamcity.extension.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import webhook.teamcity.BuildStateEnum;
import webhook.teamcity.payload.WebHookPayload;
import webhook.teamcity.settings.WebHookConfig;

public class WebhookConfigAndBuildTypeListHolder {
	public String url;
    public String extraParameters;
	public String uniqueKey; 
	public boolean enabled;
	public String payloadFormat;
	public String payloadFormatForWeb = "Unknown";
	public List<StateBean> states = new ArrayList<StateBean>();
	public boolean allBuildTypesEnabled;
	public boolean subProjectsEnabled;
	private List<WebhookBuildTypeEnabledStatusBean> builds = new ArrayList<WebhookBuildTypeEnabledStatusBean>();
	private String enabledEventsListForWeb;
	private String enabledBuildsListForWeb;
	
	public WebhookConfigAndBuildTypeListHolder(WebHookConfig config, Collection<WebHookPayload> registeredPayloads) {
		url = config.getUrl();
        extraParameters = "";
        Map<String,String> params = config.getParams();
        for (String key : params.keySet()) {
            extraParameters += key + "=" + params.get(key) + "\n";
        }
        extraParameters = extraParameters.trim();
		uniqueKey = config.getUniqueKey();
		enabled = config.getEnabled();
		payloadFormat = config.getPayloadFormat();
		setEnabledEventsListForWeb(config.getEnabledListAsString());
		setEnabledBuildsListForWeb(config.getBuildTypeCountAsFriendlyString());
		allBuildTypesEnabled = config.isEnabledForAllBuildsInProject();
		subProjectsEnabled = config.isEnabledForSubProjects();
		for (BuildStateEnum state : config.getBuildStates().getStateSet()){
			states.add(new StateBean(state.getShortName(), config.getBuildStates().enabled(state)));
		}
		for (WebHookPayload payload : registeredPayloads){
			if (payload.getFormatShortName().equals(payloadFormat)){
				this.payloadFormatForWeb = payload.getFormatDescription();
			}
		}
	}

	public List<WebhookBuildTypeEnabledStatusBean> getBuilds() {
		return builds;
	}
	
	public String getEnabledBuildTypes(){
		StringBuilder types = new StringBuilder();
		for (WebhookBuildTypeEnabledStatusBean build : getBuilds()){
			if (build.enabled){
				types.append(build.buildTypeId).append(",");
			}
		}
		return types.toString();
		
	}

	public void setBuilds(List<WebhookBuildTypeEnabledStatusBean> builds) {
		this.builds = builds;
	}
	
	
	public void addWebHookBuildType(WebhookBuildTypeEnabledStatusBean status){
		this.builds.add(status);
	}

	public String getEnabledEventsListForWeb() {
		return enabledEventsListForWeb;
	}

	public void setEnabledEventsListForWeb(String enabledEventsListForWeb) {
		this.enabledEventsListForWeb = enabledEventsListForWeb;
	}

	public String getEnabledBuildsListForWeb() {
		return enabledBuildsListForWeb;
	}

	public void setEnabledBuildsListForWeb(String enabledBuildsListForWeb) {
		this.enabledBuildsListForWeb = enabledBuildsListForWeb;
	}

}
