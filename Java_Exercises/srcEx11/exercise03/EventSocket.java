package exercise03;

import static akka.pattern.Patterns.ask;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.util.Timeout;
import exercise03.Messages.Join;

/**
 * Der EventSocket übersetzt die eingehenden Events des Sockets in Nachrichten
 * an den Actor.
 */
public class EventSocket extends WebSocketAdapter {

	private final static Timeout TIMEOUT = new Timeout(Duration.create(5,
			TimeUnit.SECONDS));
	private ActorRef actor;

	@Override
	public void onWebSocketConnect(Session session) {
		super.onWebSocketConnect(session);

		try {
			/*
			 * Da der Socket kein Actor ist, warten wir mit ask auf die Antwort
			 * (die ActorRef) und auf das Resultat des Futures.
			 */
			Future<Object> future = ask(ChatRoom.room, new Join(session), TIMEOUT);
			actor = (ActorRef) Await.result(future, TIMEOUT.duration());
		} catch (Exception e) {
		}
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		// TODO Nachricht an actor senden.
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		//TODO Quit an actor senden.
	}
}
