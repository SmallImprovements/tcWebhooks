package webhook.teamcity.extension.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SProject;
import webhook.teamcity.BuildState;
import webhook.teamcity.TeamCityIdResolver;
import webhook.teamcity.payload.WebHookPayload;
import webhook.teamcity.settings.WebHookConfig;
import webhook.teamcity.settings.WebHookProjectSettings;

public class ProjectWebHooksBean {
	String projectId;
	Map<String, WebhookConfigAndBuildTypeListHolder> webHookList;
	
	
	public static ProjectWebHooksBean build(WebHookProjectSettings projSettings, SProject project, Collection<WebHookPayload> registeredPayloads){
		ProjectWebHooksBean bean = new ProjectWebHooksBean();
		List<SBuildType> projectBuildTypes = TeamCityIdResolver.getOwnBuildTypes(project);
		
		bean.projectId = TeamCityIdResolver.getInternalProjectId(project);
		bean.webHookList = new LinkedHashMap<String, WebhookConfigAndBuildTypeListHolder>();

		/* Create a "new" config with blank stuff so that clicking the "new" button has a bunch of defaults to load in */
		WebHookConfig newBlankConfig = new WebHookConfig("", true, new BuildState().setAllEnabled(), null, true, true, null);
		newBlankConfig.setUniqueKey("new");
		/* And add it to the list */
		addWebHookConfigHolder(bean, projectBuildTypes, newBlankConfig, registeredPayloads);
		
		/* Iterate over the rest of the webhooks in this project and add them to the json config */ 
		for (WebHookConfig config : projSettings.getWebHooksAsList()){
			addWebHookConfigHolder(bean, projectBuildTypes, config, registeredPayloads);
		}
		
		return bean;
		
	}
	
	public static ProjectWebHooksBean build(WebHookProjectSettings projSettings, SBuildType sBuildType, SProject project, Collection<WebHookPayload> registeredPayloads){
		ProjectWebHooksBean bean = new ProjectWebHooksBean();
		List<SBuildType> projectBuildTypes = TeamCityIdResolver.getOwnBuildTypes(project);
		Set<String> enabledBuildTypes = new HashSet<String>();
		enabledBuildTypes.add(sBuildType.getBuildTypeId());
		
		bean.projectId = TeamCityIdResolver.getInternalProjectId(project);
		bean.webHookList = new LinkedHashMap<String, WebhookConfigAndBuildTypeListHolder>();
		
		/* Create a "new" config with blank stuff so that clicking the "new" button has a bunch of defaults to load in */
		WebHookConfig newBlankConfig = new WebHookConfig("", true, new BuildState().setAllEnabled(), null, false, false, enabledBuildTypes);
		newBlankConfig.setUniqueKey("new");
		/* And add it to the list */
		addWebHookConfigHolder(bean, projectBuildTypes, newBlankConfig, registeredPayloads);
		
		/* Iterate over the rest of the webhooks in this project and add them to the json config */ 
		for (WebHookConfig config : projSettings.getBuildWebHooksAsList(sBuildType)){
			addWebHookConfigHolder(bean, projectBuildTypes, config, registeredPayloads);
		}
		
		return bean;
		
	}


	private static void addWebHookConfigHolder(ProjectWebHooksBean bean,
			List<SBuildType> projectBuildTypes, WebHookConfig config, Collection<WebHookPayload> registeredPayloads) {
		WebhookConfigAndBuildTypeListHolder holder = new WebhookConfigAndBuildTypeListHolder(config, registeredPayloads);
		for (SBuildType sBuildType : projectBuildTypes){
			holder.addWebHookBuildType(new WebhookBuildTypeEnabledStatusBean(
													sBuildType.getBuildTypeId(), 
													sBuildType.getName(), 
													config.isEnabledForBuildType(sBuildType)
													)
										);
		}
		bean.webHookList.put(holder.uniqueKey, holder);
	}
}
