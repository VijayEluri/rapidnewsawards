package org.rapidnewsawards.messages;

import java.io.Serializable;
import java.util.LinkedList;

public class RecentSocials implements Serializable {
	private static final long serialVersionUID = 1L;
	public int numEditions;
	public EditionMessage edition;
	public boolean isNext;
	public boolean isCurrent;
	public boolean isStoryList = false;
	public boolean isNetworkList = true;
	public String order = "recent";
	public LinkedList<SocialInfo> list;
}
