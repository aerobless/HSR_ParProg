package exercise02;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchSyncedRaceControl extends AbstractRaceControl {
	CountDownLatch carsReady = new CountDownLatch(NOF_RACE_CARS);
	CountDownLatch raceStarted = new CountDownLatch(1);
	CountDownLatch raceOver = new CountDownLatch(1);
	CountDownLatch waitingForLapOfHonor = new CountDownLatch(NOF_RACE_CARS);

	
	@Override
	protected void waitForAllToBeReady() throws InterruptedException {
		// TODO Auto-generated method stub
		carsReady.await();
	}

	@Override
	public void readyToStart() {
		// TODO Auto-generated method stub
		carsReady.countDown();
	}

	@Override
	protected void giveStartSignal() {
		// TODO Auto-generated method stub
		raceStarted.countDown();
	}

	@Override
	public void waitForStartSignal() throws InterruptedException {
		// TODO Auto-generated method stub
		raceStarted.await();
		
	}

	@Override
	protected void waitForFinishing() throws InterruptedException {
		// TODO Auto-generated method stub
		raceOver.await();
		
	}

	@Override
	public boolean isOver() {
		boolean isRaceOver = false;
		if (raceOver.getCount()==0){
			isRaceOver = true;
		}
		return isRaceOver;
	}

	@Override
	public void passFinishLine() {
		// TODO Auto-generated method stub
		raceOver.countDown();
		waitingForLapOfHonor.countDown();
		
	}

	@Override
	public void waitForLapOfHonor() throws InterruptedException {
		waitingForLapOfHonor.await();
		
	}

}
