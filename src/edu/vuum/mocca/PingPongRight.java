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

        private final int mFirstSema;
        private final int mSecondSema;
        private final int mMaxLoopIterations;
        private final String mStringToPrint;
        private final SimpleSemaphore mSemaphore;
        private final CountDownLatch mLatch;

        public PlayPingPongThread(String stringToPrint,
        		SimpleSemaphore semaphore, int firstSema, int secondSema,
        		CountDownLatch latch, int maxIterations) {
            mMaxLoopIterations = maxIterations;
            mStringToPrint = stringToPrint;
            mSemaphore = semaphore;
            mFirstSema = firstSema;
            mSecondSema = secondSema;
            mLatch = latch;
        }

        /**
         * Main event loop that runs in a separate thread of control
         * and performs the ping/pong algorithm using the
         * SimpleSemaphores.
         */
        public void run() {
        	for (int index = 1; index <= mMaxLoopIterations; ++index) {
        		acquire();
        		System.out.println(String.format("%s(%d)", mStringToPrint, index));
        		release();
        	}
        	mLatch.countDown();
        }

        /**
         * Method for acquiring the appropriate SimpleSemaphore.
         */
        private void acquire() {
            mSemaphore.acquireUninterruptibly(mFirstSema);
        }

        /**
         * Method for releasing the appropriate SimpleSemaphore.
         */
        private void release() {
            mSemaphore.release(mSecondSema);
        }
    }

    /**
     * The method that actually runs the ping/pong program.
     */
    public void process(String startString, String pingString, String pongString, String finishString, int maxIterations) throws InterruptedException {

    	CountDownLatch latch = new CountDownLatch(2);
        SimpleSemaphore semaphore = new SimpleSemaphore(true);

        System.out.println(startString);

        PlayPingPongThread ping = new PlayPingPongThread(pingString, semaphore, SimpleSemaphore.FIRST_SEMA, SimpleSemaphore.SECOND_SEMA, latch, maxIterations);
        PlayPingPongThread pong = new PlayPingPongThread(pongString, semaphore, SimpleSemaphore.SECOND_SEMA, SimpleSemaphore.FIRST_SEMA, latch, maxIterations);

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

