package org.rapidnewsawards.server.test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.rapidnewsawards.server.DAO;
import org.rapidnewsawards.server.MakeDataServlet;
import org.rapidnewsawards.server.Perishable;
import org.rapidnewsawards.server.PerishableFactory;
import org.rapidnewsawards.shared.Edition;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import org.rapidnewsawards.shared.Name;

// Here we are testing the case of a periodical whose final edition is current.
public class FinalEditionCurrentTest extends RNATest {

	public static ArrayList<Perishable> mockPs = new ArrayList<Perishable>();
	static int currentEdition = 1;
	static int numEditions = 3;

	public static class RNAModule extends AbstractModule {
		@Override 
		protected void configure() {}

		@Provides
		PerishableFactory fact() {
			abstract class PF implements PerishableFactory {}
			PerishableFactory pF = new PF() {
				public Perishable create(Date end) {
					Perishable mockP = createMock(Perishable.class);
					if (currentEdition < numEditions)
						// called by findPeriodicalByName
						expect(mockP.isExpired()).andReturn(true);
					else {
						// this is the last edition, and it is current
						// called by findPeriodicalByName
						expect(mockP.isExpired()).andReturn(false);
					}
					replay(mockP);
					FinalEditionCurrentTest.mockPs.add(mockP);
					currentEdition++;
					return mockP;
				}
			};
			return pF;
		}
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		org.rapidnewsawards.server.Config.injector = Guice.createInjector(new RNAModule());
		MakeDataServlet.makeData(numEditions, 60 * MakeDataServlet.ONE_SECOND, null);
	}

	@Test
	public void testEditions() {
		Edition e = DAO.instance.getCurrentEdition(Name.JOURNALISM);
		for(Perishable p : mockPs)
			verify(p);
		assertNotNull(e);
		assertEquals(e.number, numEditions - 1);
	}


}


