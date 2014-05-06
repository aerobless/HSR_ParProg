package exercise03;

import org.eclipse.jetty.websocket.api.Session;


public class Messages {

	public static class Join {
		final Session session;

		public Join(Session session) {
			this.session = session;
		}
	}

	public static class IncomingMessage {
		final String text;

		public IncomingMessage(String text) {
			this.text = text;
		}
	}

	public static class OutgoingMessage {
		final String text;

		public OutgoingMessage(String text) {
			this.text = text;
		}
	}

	public static class Quit {
	}
}
