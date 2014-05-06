package exercise03;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import exercise03.Messages.Join;
import exercise03.Messages.OutgoingMessage;

/**
 * Der ChatRoom Actor verwaltet das Actor System und erstellt neue ChatUser
 * Actoren.
 */
public class ChatRoom extends UntypedActor {

	static final ActorSystem system;
	static final ActorRef room;

	static {
		system = ActorSystem.create("chat");
		room = system.actorOf(Props.create(ChatRoom.class), "room");
	}

	public void onReceive(Object message) throws Exception {
		if (message instanceof Join) {
			handleJoin((Join) message);
		}
	}

	/**
	 * Als Antwort auf eine Join-Message erstellt der ChatRoom einen neuen
	 * ChatUser Actor und schickt dessen ActorRef als antwort zurück. Die
	 * Jetty-Session wird dabei dem neuen Actor mitgegeben, damit dieser darüber
	 * Nachrichten an den Browser schicken kann.
	 * 
	 * Zudem wird allen Actors /user/room/* eine Nachricht geschickt, dass ein
	 * neuer User den Chat betreten hat.
	 */
	private void handleJoin(Join join) {
		system.actorSelection("/user/room/*").tell(
				new OutgoingMessage("User joined..."), getSelf());

		ActorRef userActor = getContext().actorOf(
				Props.create(ChatUser.class, join.session));
		getSender().tell(userActor, getSelf());
		System.out.println("here");
	}
}
