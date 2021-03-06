package main.lisp.evaluator.parallel.pool;

import main.util.parallel.Joiner;

public interface ThreadPoolWorker extends Runnable {
	public void setDynamicRunnable(Runnable dynamic, Joiner joiner);
	public void kill();
}
