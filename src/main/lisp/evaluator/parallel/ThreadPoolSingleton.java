package main.lisp.evaluator.parallel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import util.trace.Tracer;

public class ThreadPoolSingleton {
	private static final Class<? extends ThreadPool> defaultThreadPoolClass;
	private static Class<? extends ThreadPool> ThreadPoolClass;
	
	private static ThreadPool threadPool;
	
	static {
		defaultThreadPoolClass = NullThreadPool.class;
		ThreadPoolClass = defaultThreadPoolClass;
	}
	
	/**
	 * Sets the class for use as the thread pool. 
	 * 
	 * @param clazz thread pool class
	 * @throws UnsupportedOperationException if changing the class after the singleton has
	 *         been created
	 */
	public static void setClass(Class<? extends ThreadPool> clazz) {
		if (threadPool != null) {
			throw new UnsupportedOperationException("Cannot change thread pool class after instantiation");
		}
		try {
			Constructor<? extends ThreadPool> c = clazz.getDeclaredConstructor();
			int modifiers = c.getModifiers();
			boolean canAccess = false;
			if ((modifiers & Modifier.PUBLIC) != 0) {
				canAccess = true;
			} else if ((modifiers & Modifier.PROTECTED) != 0) {
				if (c.getDeclaringClass().getPackage().equals(ThreadPoolSingleton.class.getPackage())) {
					canAccess = true;
				}
			}
			if (!canAccess) {
				throw new IllegalArgumentException("Thread pool class' constructor is not accessible by the factory (is it private?)");
			}
		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
			throw new IllegalArgumentException("Thread pool class must have a contructor with no arguments", e);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		ThreadPoolClass = clazz;
	}
	
	/**
	 * Gets the {@link ThreadPool} for eager evaluation, creating it
	 * with specified class on first call.
	 * 
	 * @return thread pool
	 */
	public static ThreadPool get() {
		if (threadPool == null) {
			createInstance();
		}
		return threadPool;
	}
	
	private static void createInstance() {
		try {
			threadPool = (ThreadPool) ThreadPoolClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			try {
				threadPool = (ThreadPool) defaultThreadPoolClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e1) {
				e1.printStackTrace();
				threadPool = null;
			}
		}
		Tracer.info(ThreadPoolSingleton.class, "New thread pool: " + threadPool);
	}
}
