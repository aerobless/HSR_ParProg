package exercise03;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


/**
 * Der EventServer erstellt die Servlets zur Bereitstellung der HTML-Dateien
 * sowie den Endpunkt für den Websocket.
 * 
 * Nach dem Start ist der Chat unter http://localhost:8080 erreichbar.
 */
public class EventServer {
	public static void main(String[] args) {
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);
		server.addConnector(connector);

		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setWelcomeFiles(new String[] { "srcEx11/exercise03/chat.html" });
		resourceHandler.setResourceBase(".");

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler, context });

		server.setHandler(handlers);
	
		// Add a websocket to a specific path spec
		ServletHolder holderEvents = new ServletHolder("ws-events",
				EventServlet.class);
		context.addServlet(holderEvents, "/events/*");

		try {
			server.start();
			server.dump(System.err);
			server.join();
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}
}
