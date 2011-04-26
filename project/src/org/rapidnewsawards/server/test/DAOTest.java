package org.rapidnewsawards.server.test;

import org.junit.Before;
import org.junit.Test;
import org.rapidnewsawards.server.DAO;
import org.rapidnewsawards.server.MakeDataServlet;
import org.rapidnewsawards.server.TitleGrabber;
import org.rapidnewsawards.shared.Edition;
import org.rapidnewsawards.shared.Link;
import org.rapidnewsawards.shared.Name;
import org.rapidnewsawards.shared.User;

public class DAOTest extends RNATest {

	// todo
	// make bigger, easier to read
	// don't require case sensitive editor logins
	// test w/explorer
	// make top story submitters / voters into user links
	// test judge joins
	// no self follows
	// improve vote interactivity
	// add support checkbox
	// don't spend first edition, etc
	// user link style
	// single story page
	// make killdata admin
	// send time remaining from server
	// forbid voting / joining dead periodical
	// don't display future editions
	// add time to display vote history
	// break transitions into smaller tasks
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		MakeDataServlet.makeData(2, 30 * 60 * MakeDataServlet.ONE_SECOND, null);
	}

	void verifyEdition() {
		Edition e = DAO.instance.editions.getCurrentEdition(Name.AGGREGATOR_NAME);
		assertNotNull(e);
	}

	@Test
	public void testEditions() {
		verifyEdition();
	}

	// TODO disallow voting in expired editions


	@Test
	public void testUser() {
		User mg = getUser(null);
		
		final String displayName = mg.getDisplayName();
		assertEquals(displayName, "ohthatmeg");
		assertNotNull(mg);
	}

	@Test
	public void testParser() {
		final String title = "<Title>abc</Title>";
		assertEquals(TitleGrabber.tryGrab(title), "abc");
		assertEquals(TitleGrabber.tryGrab(" <Title>\n j \n </Title> "), 
				"j");
	}
	
	@Test
	public void testVote() {
		User mg = getUser(null);
		Edition e = DAO.instance.editions.getCurrentEdition(Name.AGGREGATOR_NAME);
		
		Link l = DAO.instance.createLink("http://example.com", "title", mg.getKey());
		Link l3 = DAO.instance.createLink("http://example2.com",  "title", mg.getKey());
		DAO.instance.voteFor(mg, e, l, true);
		assertTrue(DAO.instance.hasVoted(mg, e, l));
		DAO.instance.voteFor(mg, e, l3, true);
		assertTrue(DAO.instance.hasVoted(mg, e, l3));
		assertTrue(DAO.instance.hasVoted(mg, e, l));
		Link l2 = DAO.instance.createLink("http://bad.com",  "title", mg.getKey());
		assertFalse(DAO.instance.hasVoted(mg, e, l2));
	}


}
