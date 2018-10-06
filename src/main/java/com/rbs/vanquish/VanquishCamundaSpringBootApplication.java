package com.rbs.vanquish;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Permissions.ALL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
//import javax.jms.Message;
import org.springframework.messaging.Message;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.StrongUuidGenerator;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.camunda.bpm.spring.boot.starter.event.PreUndeployEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import com.rbs.vanquish.framework.bpm.config.VanquishConfigManager;
import com.rbs.vanquish.framework.bpm.constant.VanquishConstants;
import com.rbs.vanquish.framework.bpm.message.MessageDelegateImpl;


@SpringBootApplication
@EnableAutoConfiguration
@EnableProcessApplication
public class VanquishCamundaSpringBootApplication implements CommandLineRunner{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VanquishCamundaSpringBootApplication.class);
	
	@Autowired
	private ProcessEngine processEngine;
	
	
	//private RuntimeService runtimeService;
	
	//@Autowired
	//private MessageDelegateImpl mssageDelegateImpl;
	
	//@Autowired
	//private VanquishApplicationStatusProvider vanquishApplicationStatusProvider;
	
	@Autowired
	private ApplicationContext appContext;

	public static void main(String[] args) {
		SpringApplication.run(VanquishCamundaSpringBootApplication.class, args);
	}
	
	private String getDateTimePrefix() {
		DateFormat loDatTimFormat = new SimpleDateFormat("dd:MM:yyyy-HH:mm:ss:SSSS-");
		Calendar cal = Calendar.getInstance();
        String lsID = loDatTimFormat.format(cal.getTime());
        String lsNewID = new StrongUuidGenerator().getNextId();
        lsID = lsID + lsNewID;
        return lsID;
	}//eof getDateTimePrefix
	
	
	@EventListener
	private void processPostDeploy(PostDeployEvent event) throws InterruptedException {	
		//startBusinessProcess();
	}//eof processPostDeploy
	
	private void startBusinessProcess() {
		try 
		{
			boolean flag = false;
			//String lsBusinessKey = "1234589ghcve23468dfssgdfsgfwteftw324374y357y";
			String lsBusinessKey1 = getDateTimePrefix() +"-ff";
			Map<String,Object> loVariableParamMap1 = new HashMap<String,Object>();
			loVariableParamMap1.put("BusinessKey", lsBusinessKey1);
			String lsProcessID1 = exceuteprocess("A23D12", "34t7u56tui", new Random().nextInt(7000), flag, lsBusinessKey1);
			
			
			String lsBusinessKey2 = getDateTimePrefix() + "-ss";
			Map<String,Object> loVariableParamMap2 = new HashMap<String,Object>();
			loVariableParamMap2.put("BusinessKey", lsBusinessKey2);
			String lsProcessID2 = exceuteprocess("csdgsdvg", "dbhshsbd343r36", new Random().nextInt(8000), flag, lsBusinessKey2);
			
			
			String lsBusinessKey3 = getDateTimePrefix() + "-tt";
			Map<String,Object> loVariableParamMap3 = new HashMap<String,Object>();
			loVariableParamMap3.put("BusinessKey", lsBusinessKey3);
			String lsProcessID3 = exceuteprocess("vcbxvcxhvc", "dbnshsgd25234352", new Random().nextInt(9000), flag, lsBusinessKey3);
			
			logMessage("lsProcessID1 -> "+lsProcessID1);
			logMessage("lsProcessID2 -> "+lsProcessID2);
			logMessage("lsProcessID3 -> "+lsProcessID3);
			
			Thread.sleep(60000);
			
			
			
			Message<String> loTextMessage = new GenericMessage<String>(lsBusinessKey1);
			//wrapDelegateAndExecuteMessageWithBusinessKey("VANQUISH_ENGINE.GENERIC.FILE.INBOUND.QUEUE.DEV", loTextMessage, lsBusinessKey1);
			
			loTextMessage = null;
			loTextMessage = new GenericMessage<String>(lsBusinessKey2);
			//wrapDelegateAndExecuteMessageWithBusinessKey("VANQUISH_ENGINE.GENERIC.FILE.INBOUND.QUEUE.DEV", loTextMessage, lsBusinessKey2);
			
			loTextMessage = null;
			loTextMessage = new GenericMessage<String>(lsBusinessKey3);
			wrapDelegateAndExecuteMessageWithBusinessKey("VANQUISH_ENGINE.GENERIC.FILE.INBOUND.QUEUE.DEV", loTextMessage, lsBusinessKey3);
		} 
		catch (Exception aException) 
		{
			aException.printStackTrace();
		}
		finally {
			//vanquishApplicationStatusProvider.setRunningStatus(true);
		}
	}//eof startBusinessProcess
	
	@Transactional
	private void wrapDelegateAndExecuteMessageWithBusinessKey(String aQueueName, Message<String> loTextMessage, String aBusinessKey)
	{
		try 
		{
			VanquishConfigManager loVanquishConfigManager = VanquishConfigManager.getInstance();
			String lsMessageName = loVanquishConfigManager.getMessageNameForQueue(aQueueName);
			logMessage(" aQueueName         -> "+aQueueName);
			logMessage(" lsMessageName      -> "+lsMessageName);
			logMessage(" aBusinessKey       -> "+aBusinessKey);
			MessageDelegateImpl loTestMessageDelegate = new MessageDelegateImpl(processEngine);
			loTestMessageDelegate.delegateAndExecuteMessageWithBusinessKey(lsMessageName,loTextMessage, aBusinessKey);
		} 
		catch (Exception aException) 
		{
			// TODO Auto-generated catch block
			aException.printStackTrace();
		}
	}//eof wrapDelegateAndExecuteMessageWithBusinessKey
	
	
	
	@EventListener
	public void onPreUndeploy(PreUndeployEvent event) {
		//vanquishApplicationStatusProvider.setRunningStatus(false);
	}
	
	@Transactional
	private String exceuteprocess(String aKey1, String aKey2, Integer aKey3, Boolean aKey4, String aBusinessKey) {
		
		Map<String,Object> loVariables = new HashMap<String,Object>();
	    loVariables.put(VanquishConstants.PROCESS_COMPLETED, "");
	    loVariables.put("duplicate", aKey4);
	    loVariables.put("BusinessKey", aBusinessKey);
	    loVariables.put(VanquishConstants.UNIQUE_REFERENCE_KEY, aBusinessKey);
	    
		//ProcessInstance loProcessInstance =  runtimeService.startProcessInstanceByKey("payment_processing_workflow", loVariables);
	    
	    setupUser();
	    ProcessInstance loProcessInstance =  processEngine.getRuntimeService().startProcessInstanceByKey("payment_processing_workflow_new", aBusinessKey, loVariables);
	    String lsProcessID = loProcessInstance.getProcessInstanceId();
	    		
	    logMessage("New Process Created with ID -> "+lsProcessID);
	    return lsProcessID;
	}//eof exceuteprocess
	
	/**
	 * 
	 */
	private void setupUser() {
	if (processEngine.getIdentityService().createUserQuery().userId("CamundaAdmin").count() == 0) 
	{
		User user = processEngine.getIdentityService().newUser("CamundaAdmin");
		user.setFirstName("Lithesh");
		user.setLastName("Anargha");
		user.setPassword("CamundaAdmin");
		user.setEmail("Lithesh.Anargha@rbs.co.uk");
		processEngine.getIdentityService().saveUser(user);

		Group group = processEngine.getIdentityService().newGroup(Groups.CAMUNDA_ADMIN);
		group.setName("Administrators");
		group.setType(Groups.GROUP_TYPE_SYSTEM);
		processEngine.getIdentityService().saveGroup(group);

		for (Resource resource : Resources.values()) 
		{
			Authorization auth = processEngine.getAuthorizationService().createNewAuthorization(AUTH_TYPE_GRANT);
		    auth.setGroupId(Groups.CAMUNDA_ADMIN);
		    auth.addPermission(ALL);
		    auth.setResourceId(ANY);
		    auth.setResource(resource);
		    processEngine.getAuthorizationService().saveAuthorization(auth);
		 }
		 processEngine.getIdentityService().createMembership("CamundaAdmin", Groups.CAMUNDA_ADMIN);
    }//eof if
	}//eof setupUser
	
	public void stopProcess() {
		logMessage("Shutting Down!");
	}
	
	
	
	@Override
    public void run(String... args) throws Exception {

        String[] beans = appContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        for (String bean : beans) {
            //System.out.println(bean);
        }
        startBusinessProcess();
    }//eof run
	
	
	 private static void logMessage(String lsMessage) {
		 LOGGER.info(lsMessage);
	 }


}
