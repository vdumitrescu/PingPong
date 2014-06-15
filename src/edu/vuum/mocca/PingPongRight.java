package edu.vuum.mocca;

import java.util.concurrent.CountDownLatch;

/**
 * @class PingPongRight
 * 
 * @brief This class implements a Java program that creates two
 *        instances of the PlayPingPongThread and start these thread
 *        instances to correctly alternate printing "Ping" and "Pong",
 *        respectively, on the console display.
 */
public class PingPongRight {

    /**
     * @class PlayPingPongThread
     * 
     * @brief This class implements the ping/pong processing algorithm
     *        using the SimpleSemaphore to alternate printing "ping"
     *        and "pong" to the console display.
     */
    public class PlayPingPongThread extends Thread {

    	private final ChessClock mClock;
        private final CountDownLatch mLatch;
        private final String mStringToPrint;
        private final int mMaxLoopIterations;
        private final int mPlayer;

        public PlayPingPongThread(String stringToPrint, ChessClock clock, int player,
        		CountDownLatch latch, int maxIterations) {
            mStringToPrint = stringToPrint;
            mClock = clock;
            mLatch = latch;
            mMaxLoopIterations = maxIterations;
            mPlayer = player;
        }

        public void run() {
        	for (int index = 1; index <= mMaxLoopIterations; ++index) {
                mClock.lock();
                try {
                	mClock.waitForTurn(mPlayer);
                    System.out.println(String.format("%s(%d)", mStringToPrint, index));
                    mClock.pushButton(mPlayer);
                } catch (InterruptedException e) {
				} finally {
                	mClock.unlock();
                }
        	}
        	mLatch.countDown();
        }
    }

    /**
     * The method that actually runs the ping/pong program.
     */
    public void process(String startString, String pingString, String pongString, String finishString, int maxIterations) throws InterruptedException {

    	CountDownLatch latch = new CountDownLatch(2);
        ChessClock clock = new ChessClock();

        System.out.println(startString);

        PlayPingPongThread ping = new PlayPingPongThread(pingString, clock, ChessClock.PLAYER_ONE, latch, maxIterations);
        PlayPingPongThread pong = new PlayPingPongThread(pongString, clock, ChessClock.PLAYER_TWO, latch, maxIterations);

        // Starting pong first should not change the outcome
        pong.start();
        ping.start();

        latch.await();

        System.out.println(finishString);
    }

    /**
     * The main() entry point method into PingPongRight program.
     * 
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        new PingPongRight().process("Ready...Set...Go!", "Ping!  ", " Pong! ", "Done!", 10);
    }
}

