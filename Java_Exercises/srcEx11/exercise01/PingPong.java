package exercise01;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;


public class PingPong {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("PingPong");

		ActorRef p1 = system.actorOf(Props.create(PingPongActor.class), "P1");
		ActorRef p2 = system.actorOf(Props.create(PingPongActor.class), "P2");
		
		/*
		 * indem wir p2 als Sender angeben kann p1 mit getSender darauf
		 * zugreifen
		 */
		p1.tell(new Start(), p2);
	}

	static class Start {
	}

	static class Ping {
		final int count;

		public Ping(int count) {
			this.count = count;
		}
	}

	static class PingPongActor extends UntypedActor {

		public void onReceive(Object message) {
			if (message instanceof Start) {
				handleStart((Start) message);
			} else if (message instanceof Ping) {
				handlePing((Ping) message);
			}
		}

		private void handlePing(Ping msg) {
			System.out.println(getSelf().path().name() + ": count " + msg.count);
			try {
				Thread.sleep((long) (Math.random() * 1000) + 300);
			} catch (InterruptedException e) {
			}
			// TODO Zähler erhöhen und dem Sender zurückschicken
			Ping ping = new Ping(msg.count+1);
			getSender().tell(ping, getSelf());
		}

		private void handleStart(Start message) {
			System.out.println("Starting...");
			Ping ping = new Ping(0);
			// TODO Zähler auf 0 setzen und dem Sender zurückschicken
			getSender().tell(ping, getSelf());
		}
	}
}