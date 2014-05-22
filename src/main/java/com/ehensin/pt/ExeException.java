package com.ehensin.pt;

public class ExeException extends Exception{

	/**
	 * 当前版本号
	 */
	private static final long serialVersionUID = 20121212L;
	
	public ExeException(String msg, Throwable t){
		super(msg, t);
	}

}
