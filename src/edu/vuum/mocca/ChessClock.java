package edu.vuum.mocca;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ChessClock {
	
	public static final int PLAYER_ONE = 0;
	public static final int PLAYER_TWO = 1;
	
	private final ReentrantLock lock = new ReentrantLock(true);
	private final Condition turns[] = new Condition[2];
	private volatile int currentPlayer;
	
	public ChessClock() {
		turns[PLAYER_ONE] = lock.newCondition();
		turns[PLAYER_TWO] = lock.newCondition();
		currentPlayer = PLAYER_ONE;
	}
	
	public void lock() {
		lock.lock();
	}
	
	public void unlock() {
		lock.unlock();
	}
	
	public void waitForTurn(int player) throws InterruptedException {
		while (currentPlayer != player)
			turns[player].await();
	}

	public void pushButton(int player) {
		currentPlayer = 1 - player;
		turns[currentPlayer].signal();
	}	
}
