package exercise02;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarriersSyncedRaceControl extends AbstractRaceControl {
	CyclicBarrier carsReady = new CyclicBarrier(NOF_RACE_CARS);
	CyclicBarrier raceStarted = new CyclicBarrier(NOF_RACE_CARS);
	CyclicBarrier raceOver = new CyclicBarrier(NOF_RACE_CARS);
	CyclicBarrier waitingForLapOfHonor = new CyclicBarrier(NOF_RACE_CARS);


	@Override
	protected void waitForAllToBeReady() throws InterruptedException {

	}

	@Override
	public void readyToStart() {
		try {
			carsReady.await();
		} catch (BrokenBarrierException anEx) {
			// TODO Auto-generated catch block
			anEx.printStackTrace();
		} catch (InterruptedException anEx) {
			// TODO Auto-generated catch block
			anEx.printStackTrace();
		}
	}

	@Override
	protected void giveStartSignal() {
		try {
			raceStarted.await();
		} catch (BrokenBarrierException anEx) {
			// TODO Auto-generated catch block
			anEx.printStackTrace();
		} catch (InterruptedException anEx) {
			// TODO Auto-generated catch block
			anEx.printStackTrace();
		}
		
	}

	@Override
	public void waitForStartSignal() throws InterruptedException {
		
	}

	@Override
	protected void waitForFinishing() throws InterruptedException {
		// TODO Auto-generated method stub

		
	}

	@Override
	public boolean isOver() {
		boolean isOver = false;
		if(raceOver.getNumberWaiting()==0){
			isOver = true;
		}
		return isOver;
	}

	@Override
	public void passFinishLine() {
		// TODO Auto-generated method stub
		try {
			raceOver.await();
			waitingForLapOfHonor.await();
		} catch (BrokenBarrierException anEx) {
			// TODO Auto-generated catch block
			anEx.printStackTrace();
		} catch (InterruptedException anEx) {
			// TODO Auto-generated catch block
			anEx.printStackTrace();
		}
		
	}

	@Override
	public void waitForLapOfHonor() throws InterruptedException {
		// TODO Auto-generated method stub
		try {
			waitingForLapOfHonor.await();
		} catch (BrokenBarrierException anEx) {
			// TODO Auto-generated catch block
			anEx.printStackTrace();
		}
	}

}
