package exercise03;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.PoisonPill;
import akka.actor.UntypedActor;
import exercise03.Messages.IncomingMessage;
import exercise03.Messages.OutgoingMessage;
import exercise03.Messages.Quit;

/**
 * Repräsentiert einen Teilnehmer im Chat und hält die Session mit dem Browser.
 */
public class ChatUser extends UntypedActor {

	private final Session session;

	public ChatUser(Session session) {
		this.session = session;
	}

	public void onReceive(Object message) throws Exception {
		if (message instanceof IncomingMessage) {
			handleIncomingMessage((IncomingMessage) message);
		} else if (message instanceof OutgoingMessage) {
			handleOutgoingMessage((OutgoingMessage) message);
		} else if (message instanceof Quit) {
			handleQuit();
		}
	}

	/**
	 * Beim Beenden senden wir den anderen Chat-Teilnehmern eine Nachricht und
	 * beenden den Actor mittels einer PoisonPill.
	 */
	private void handleQuit() {
		ActorSelection selection = getChatUsers();
		selection.tell((new OutgoingMessage("User quit...")), ActorRef.noSender());
		getSelf().tell(PoisonPill.getInstance(), getSelf());
		System.out.println("dead");
	}

	/**
	 * Beim Empfang einer OutgoingMessage senden wir den Text zurück an den
	 * Browser.
	 */
	private void handleOutgoingMessage(OutgoingMessage message)
			throws IOException {
		if (session.isOpen()) {
			session.getRemote().sendString(message.text);
		}
	}

	/**
	 * Eine IncomingMessage verteilen wir mittels ActorSelection /user/room/* an
	 * alle anderen Teilnehmer im Chat.
	 */
	private void handleIncomingMessage(IncomingMessage message) {
		ActorSelection selection = getChatUsers();
		selection.tell(new OutgoingMessage(message.text), getSelf());
	}

	private ActorSelection getChatUsers() {
		return ChatRoom.system.actorSelection("/user/room/*");
	}
}