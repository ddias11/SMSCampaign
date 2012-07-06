package br.com.campanhasms.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.persistence.transactions.GetNextContactNumber;
import br.com.campanhasms.properties.ApplicationProperties;
import br.com.campanhasms.sms.exception.NoMoreContactsAvailable;
import br.com.campanhasms.sms.service.impl.SMSServiceWrapper;

public class SendMessageJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			SystemPrevayler.execute(new GetNextContactNumber());
			SystemPrevaylerModel systemPrevaylerModel = SystemPrevayler.getSystemPrevaylerModel();
			String textMessage = systemPrevaylerModel.getTextMessage();
			Long currentContact = systemPrevaylerModel.getCurrentContact();
			if (currentContact < 55000000L) {
				throw new NoMoreContactsAvailable();
			}
			String contact = currentContact.toString();
			SMSServiceWrapper.initialize(systemPrevaylerModel.getCOMPort());
			if(Boolean.valueOf(ApplicationProperties.getString("Application.isDebugMode"))){
				textMessage += ";Dbg Msg to " + contact;
				contact = ApplicationProperties.getString("Application.DebugContact");
			}

			SMSServiceWrapper.sendMessage(contact, textMessage);
		} catch (NoMoreContactsAvailable e) {
			// ERROR ON Obtain Next Contact Number
			try {
				arg0.getScheduler().unscheduleJob(arg0.getTrigger().getKey());
			} catch (SchedulerException e1) {
				// ERROR ON unscheduleJog
				e1.printStackTrace();
			}
		} catch (Exception e) {
			// ERROR ON Send Message Method
			e.printStackTrace();
		}

	}

}
