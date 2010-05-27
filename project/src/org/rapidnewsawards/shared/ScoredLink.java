package org.rapidnewsawards.shared;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class ScoredLink {
	@Id
	public Long id;

	public Key<Edition> edition;
	public Key<Link> link;
	public int score;
	
	public ScoredLink() {}
	
	public ScoredLink(Key<Edition> edition, Key<Link> link, int score) {
		this.edition = edition;
		this.link = link;
		this.score = score;
	}
}