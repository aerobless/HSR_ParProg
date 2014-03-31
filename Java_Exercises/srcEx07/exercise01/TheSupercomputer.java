package exercise01;

import java.util.Arrays;
import java.util.Observable;

public class TheSupercomputer extends Observable {

	private enum StatusType {
		BORED, STARTING, CONFUSED, ERROR, RECOVERING_FROM_ERROR, OVERHEATED, CALCULATING
	}

	private static final int ITERATIONS = 5;
	StatusType status = StatusType.BORED;
	private int dotsCount = 0;

	public String calculateUltimateAnswerToTheUltimateQuestionOfLifeTheUniverseAndEverything() {
		try {
			int firstPart = calculateFirstPart();
			int secondPart = calculateSecondPart();
			int modulo = calculateModulo();
			status = StatusType.BORED;
			setChanged();
			notifyObservers();
			return Integer.toString(firstPart * secondPart % modulo);

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private int calculateModulo() throws InterruptedException {
		status = StatusType.OVERHEATED;
		doSleepLoop();
		status = StatusType.CALCULATING;
		doSleepLoop();
		return 53;
	}

	private int calculateSecondPart() throws InterruptedException {
		status = StatusType.CONFUSED;
		doSleepLoop();
		doSleepLoop();
		status = StatusType.ERROR;
		doSleepLoop();
		doSleepLoop();
		status = StatusType.RECOVERING_FROM_ERROR;
		doSleepLoop();
		status = StatusType.CALCULATING;
		doSleepLoop();
		return 41;
	}

	private int calculateFirstPart() throws InterruptedException {
		status = StatusType.STARTING;
		doSleepLoop();
		status = StatusType.CALCULATING;
		doSleepLoop();
		return 23;
	}

	private void doSleepLoop() throws InterruptedException {
		for (int i = 0; i < ITERATIONS; i++) {
			Thread.sleep(200);
			dotsCount %= ITERATIONS;
			dotsCount++;
			setChanged();
			notifyObservers();
		}
	}

	public String getStatus() {
		return status.name() + makeDotStr();
	}

	private String makeDotStr() {
		char[] chars = new char[dotsCount];
		Arrays.fill(chars, '.');
		return new String(chars);
	}
}
