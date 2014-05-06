package exercise02;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import exercise02.WorkerManager.NoWorkersAvailable;

public class Calculator extends UntypedActor {

	/**
	 * Message um dem Calculator mitzuteilen, dass er ein neues WorkItem
	 * schedulen soll.
	 */
	static class Calculate {
	}

	private final ActorRef manager;

	public Calculator(ActorRef manager) {
		this.manager = manager;
	}

	public void onReceive(Object message) {
		if (message instanceof Calculate) {
			handleCalculate();
		} else if (message instanceof WorkItemResult) {
			handleWorkItemResult((WorkItemResult) message);
		} else if (message instanceof NoWorkersAvailable) {
			handleNoWorkersAvailable();
		}
	}

	private void handleNoWorkersAvailable() {
		System.out.println("Could not schedule work item, no workers available.");
	}

	private void handleWorkItemResult(WorkItemResult result) {
		System.out.println(result.getDescription() + " " + result.getValue());
	}

	private void handleCalculate() {
		manager.tell(new WorkerManager.Schedule(new WorkItem()), getSelf());
	}
}