package com.ehensin.pt.thread;

import java.lang.Thread.UncaughtExceptionHandler;

public class PoolThreadGroup extends ThreadGroup{

	UncaughtExceptionHandler handler;
	public PoolThreadGroup(String name) {
		super(name);	
		this.setDefaultUncaughtException(new PTUncaughtExceptionHandler());
	}
    public void setDefaultUncaughtException(UncaughtExceptionHandler handler){
    	this.handler = handler;
    }
	public void uncaughtException(Thread t, Throwable e){
		handler.uncaughtException(t, e);
	}
}
