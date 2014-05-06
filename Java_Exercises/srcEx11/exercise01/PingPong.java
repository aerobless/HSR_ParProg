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
			// Increase the counter and return a message to the sender.
			Ping ping = new Ping(msg.count+1);
			getSender().tell(ping, getSelf());
			if(msg.count==9){
				//getContext().system().shutdown();
				getContext().stop(getSelf());
			}
		}

		private void handleStart(Start message) {
			System.out.println("Starting...");
			// Set the counter to zero and send a message to the other actor.
			Ping ping = new Ping(0);
			getSender().tell(ping, getSelf());
		}
	}
}