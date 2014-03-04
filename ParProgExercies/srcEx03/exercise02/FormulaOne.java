package exercise02;

public class FormulaOne {
	public static void main(String[] args) throws InterruptedException {
		new MonitorSyncedRaceControl().runRace();
		new CountDownLatchSyncedRaceControl().runRace(); //Exercise 2a
	}
}
