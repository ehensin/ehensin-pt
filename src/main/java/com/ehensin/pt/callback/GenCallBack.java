package com.ehensin.pt.callback;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import com.ehensin.pt.RuntimeData;
import com.ehensin.pt.spi.IAnalyzer;
import com.ehensin.pt.spi.ICallBack;

public class GenCallBack extends UnicastRemoteObject  implements ICallBack{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	transient IAnalyzer analyzer;
    public GenCallBack(IAnalyzer analyzer)throws RemoteException {
    	this.analyzer = analyzer;
    }
	@Override
	public void callback(Map<String,RuntimeData> data) throws RemoteException {
		analyzer.analyze(data);
	}

}
