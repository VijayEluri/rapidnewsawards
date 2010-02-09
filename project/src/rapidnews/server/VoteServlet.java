package rapidnews.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.EntityNotFoundException;

import rapidnews.shared.Link;
import rapidnews.shared.Reader;

public class VoteServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(VoteServlet.class.getName());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Reader r = null;

		try {
			r = DAO.instance.findReaderByUsername(request.getParameter("username"), false);
		} catch (EntityNotFoundException e1) {
			assert(false); // only thrown when fillrefs = true
		}
		
		if (r == null) {
			out.println("No such voter");
			return;
		}
		
		String url = request.getParameter("href");
		
		// TODO broken on some complex hrefs
		Link l = DAO.instance.findOrCreateLinkByURL(url);
		
		try	{
			DAO.instance.voteFor(r, l);
		}
		// TODO handle malformed urls
		catch (IllegalArgumentException e) {
			log.warning("BAD VOTE: " + r.getUsername() + ", " + url);
			out.println("vote already counted");	
			return;
		}

		log.info("VOTE: " + r.getUsername() + ", " + url);
		out.println("vote counted");
		
	}
}
