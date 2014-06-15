package edu.vuum.mocca;
import java.util.concurrent.Semaphore;

/**
 * @class SimpleSemaphore
 * 
 * @brief This class provides a simple counting semaphore implementation using
 *        Java a ReentrantLock and a ConditionObject (which is accessed via a
 *        Condition). It must implement both "Fair" and "NonFair" semaphore
 *        semantics, just liked Java Semaphores.
 */
public class SimpleSemaphore {
	
	public static final int FIRST_SEMA = 0;
	public static final int SECOND_SEMA = 1;
	
	private final Semaphore semas[] = new Semaphore[2];

    public SimpleSemaphore(boolean fair) {
    	semas[FIRST_SEMA] = new Semaphore(1, fair);
    	semas[SECOND_SEMA] = new Semaphore(0, fair);
    }

    public void acquire(int whichOne) throws InterruptedException {
    	semas[whichOne].acquire();
    }

    public void acquireUninterruptibly(int whichOne) {
    	semas[whichOne].acquireUninterruptibly();
    }

    void release(int whichOne) {
    	semas[whichOne].release();
    }
}
