package com.ehensin.pt.spi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.ehensin.pt.RuntimeParameter;

/**
 * 压力发生器接口，是一个远端rmi接口
 * */
public interface IGenerator extends Remote {
    /**初始化*/
	public void init(RuntimeParameter parameter) throws RemoteException;
	/**运行接口*/
	public  void run(ICallBack callback)throws RemoteException;
}
