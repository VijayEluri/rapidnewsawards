package org.rapidnewsawards.messages;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.rapidnewsawards.core.Periodical;
import org.rapidnewsawards.core.User;


/* User Interface Info Package
 * 
 */
public class SocialInfo {
	public User editor;
	public User judge;
	/*
	 * follow (true) or unfollow
	 */
	public boolean on;
	private Date time;
	private String timeStr;

	public SocialInfo(User e, User j, boolean on, Date time) { 
		this.on = on; 
		this.editor = e; 
		this.judge = j;
		this.setTime(time);
	}
		
	public SocialInfo() {}

	public void setTime(Date time) {
		this.time = time;
		this.timeStr = (Periodical.timeFormat(time));
	}

	public Date getTime() {
		return time;
	}

	public String getTimeStr() {
		return timeStr;
	};
}
