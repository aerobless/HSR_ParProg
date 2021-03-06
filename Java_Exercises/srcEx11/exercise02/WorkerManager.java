package exercise02;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.japi.Function;

/**
 * Die Klasse WorkerManager nimmt Schedule-Messages entgegen und verteilt die
 * Arbeit auf eine fixe Anzahl Worker-Actors. Der Manager merkt sich dabei,
 * welche Worker gerade beschäftigt sind und welche nichts zu tun haben.
 */
public class WorkerManager extends UntypedActor {

	public static class Schedule {
		private final WorkItem work;

		public Schedule(WorkItem work) {
			this.work = work;
		}

		public WorkItem getWork() {
			return work;
		}
	}

	public static class NoWorkersAvailable {
	}

	/**
	 * Eine Queue der unbeschäftigten Worker.
	 */
	private final Deque<ActorRef> idle = new LinkedList<>();

	/**
	 * Zusätzlich zu den beschäftigten Worker merken wir uns auch den
	 * ursprünglichen "Auftraggeber" des WorkItems, damit wir diesem das
	 * Resultat zurückschicken können:
	 */
	private final Map<ActorRef /* Worker */, ActorRef /* Sender */> working = new HashMap<>();

	public WorkerManager(int numberOfWorkers) {
		//getContext().actorOf(Props.create(Worker.class).withDispatcher("pinned-dispatcher"));
		ActorSystem system = ActorSystem.create("workActors");
		for (int i = 0; i < numberOfWorkers; i++) {
			// TODO Worker Actors erstellen und in der idle Queue einreihen.
			ActorRef worker = system.actorOf(Props.create(Worker.class), "Worker"+i);
			idle.add(worker);			
		}
	}

	/**
	 * The results of this experiment can be seen in
	 * the visual jvm. It is available by entering:
	 * 
	 * 'jvisualvm' into Bash on OS X
	 * 
	 */
	
	public void onReceive(Object message) {
		if (message instanceof Schedule && idle.isEmpty()) {
			// TODO Es sind leider keine Worker verfügbar, das melden wir dem
			// Sender zurück
			getSender().tell("No workers available.", getSelf());
			System.out.println("NO WORKER AVAILABLE");

		} else if (message instanceof Schedule) {
			// TODO Der nächste freie Worker soll die Arbeit erledigen.
			ActorRef worker = idle.remove();
			
			worker.tell(((Schedule) message).getWork(), getSelf());
			working.put(worker, getSender());
			System.out.println("SCHEDULED WORK");

		} else if (message instanceof WorkItemResult) {
			// TODO Die Arbeit wurde erledigt. Wir melden dem ursprünglichen
			// Auftrageber das Resultat zurück
			idle.add(getSender());
			working.get(getSender()).tell(((WorkItemResult) message).getValue(), getSelf());
			System.out.println("WORK RESULT");
		}
	}

	protected void childHasCrashed(ActorRef sender) {
		// Der Worker wurde bereits neu gestartet, wir müssen ihn aber noch
		// zusätzlich von der idle in die working Liste umteilen.
		working.remove(sender);
		idle.add(sender);
	}

	/**
	 * Konfiguration für das Supervisor-Verhalten.
	 * 
	 * Egal was fehlschlägt, wir starten unsere Workers einfach neu
	 * (SupervisorStrategy.restart()). Man könnte hier aber auch Aufgrund der
	 * geworfenene Exception eine andere Strategie implementieren.
	 */
	@Override
	public SupervisorStrategy supervisorStrategy() {
		return new OneForOneStrategy(-1 /* Restart forever */, Duration.Inf(),
				new Function<Throwable, Directive>() {
					@Override
					public Directive apply(Throwable t) {
						childHasCrashed(getSender());
						return SupervisorStrategy.restart();
					}
				});
	}

}