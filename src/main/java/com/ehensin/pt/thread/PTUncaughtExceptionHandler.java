package com.ehensin.pt.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PTUncaughtExceptionHandler  implements java.lang.Thread.UncaughtExceptionHandler{
	private final static Logger log = LoggerFactory.getLogger(PTUncaughtExceptionHandler.class);

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		log.error("catched a exception from thread : " + t.getId() + "," + e.getClass().getName(), e);
	
	}

}
