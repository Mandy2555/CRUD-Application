package com.myapp.scheduler.email;

import java.nio.charset.StandardCharsets;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage; 

@Component
public class EmailJob  extends QuartzJobBean{
	
	@Autowired
	private JavaMailSender  mailSender ;
	
	@Autowired
	private MailProperties mailProperties;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
		JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
		String subject = jobDataMap.getString("subject");
		String body = jobDataMap.getString("body");
		String recipientEmail = jobDataMap.getString("email");
		
		sendMail(mailProperties.getUsername(),recipientEmail,subject , body);
	}
	private void sendMail(String fromEmail, String toEmail , String subject , String body) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message,StandardCharsets.UTF_8.toString());
			messageHelper.setSubject(subject);
			messageHelper.setText(body);
			messageHelper.setFrom(fromEmail);
			messageHelper.setTo(toEmail);
			mailSender.send(message);
		} catch (MessagingException ex) {
			// TODO: handle exception
			System.out.println(ex); 
		}
	}
}

