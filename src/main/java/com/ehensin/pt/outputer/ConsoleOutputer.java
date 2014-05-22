package com.ehensin.pt.outputer;

import java.util.Date;

import com.ehensin.pt.ExeException;
import com.ehensin.pt.spi.IOutputer;

public class ConsoleOutputer implements IOutputer {

	@Override
	public void print(String msg) throws ExeException {
		System.out.println(new Date() + ":" + msg);
	}

}
