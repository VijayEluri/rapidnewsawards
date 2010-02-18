package org.rapidnewsawards.server.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rapidnewsawards.server.DAO;
import org.rapidnewsawards.server.MakeDataServlet;
import org.rapidnewsawards.shared.Edition;
import org.rapidnewsawards.shared.Link;
import org.rapidnewsawards.shared.User;


import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;

public class DAOTest extends RNATest {

	@Before
	public void setUp() throws Exception {
        super.setUp();
        MakeDataServlet.makeData(2, 30 * 60 * MakeDataServlet.ONE_SECOND);
	}

	@Test
	public void testEditions() {
		Edition e = DAO.instance.getCurrentEdition("Journalism");
		assertNotNull(e);
		LinkedList<User> users = DAO.instance.findUsersByEdition(e);
		assertEquals(users.size(), 3);
		assertNotNull(e.getUsers());
		assertEquals(true, e.getUsers().size() > 0);
		assertEquals(true, e.getUsers().size() == users.size());
	}
	
	// TODO disallow voting in expired editions
	

	@Test
	public void testFindUserByUsername () {
		try {
			User mg = DAO.instance.findUserByUsername("megangarber");
			assertNotNull(mg);
			assertEquals(mg.getUsername(), "megangarber");
		} catch (EntityNotFoundException e) {
			fail("error in filling ref");
		}
	}

	
	@Test
	public void testVote() {
		try {
			User mg = DAO.instance.findUserByUsername("megangarber");
			Link l = DAO.instance.findOrCreateLinkByURL("http://example.com");
			Link l3 = DAO.instance.findOrCreateLinkByURL("http://example2.com");
			DAO.instance.voteFor(mg, l);
			assertTrue(DAO.instance.hasVoted(mg, l));
			DAO.instance.voteFor(mg, l3);
			assertTrue(DAO.instance.hasVoted(mg, l3));
			assertTrue(DAO.instance.hasVoted(mg, l));
			Link l2 = DAO.instance.findOrCreateLinkByURL("http://bad.com");
			assertFalse(DAO.instance.hasVoted(mg, l2));
		} catch (EntityNotFoundException e) {
			fail("error in filling ref");
		}
	}

	@Test
	public void testFollow() {
		try {
			User mg = DAO.instance.findUserByUsername("megangarber");
			User jny2 = DAO.instance.findUserByUsername("jny2");
			DAO.instance.follow(mg, jny2);
			assertTrue(DAO.instance.isFollowing(mg, jny2, null));
		} catch (EntityNotFoundException e) {
			fail("error in filling ref");
		}
	}

	
}