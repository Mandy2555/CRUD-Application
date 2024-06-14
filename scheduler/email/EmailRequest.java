package com.myapp.scheduler.email;

import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class EmailRequest {
	@Email
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String subject ;

	@NotEmpty
	private String body ;
	
	@NotNull
	private ZoneId timeZone;

	@NotNull
	private LocalDateTime DateTime;
	
	
	

	public LocalDateTime getDateTime() {
		return DateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		DateTime = dateTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public ZoneId getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(ZoneId timezone) {
		this.timeZone = timeZone;
	}

}
