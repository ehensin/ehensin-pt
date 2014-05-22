package com.ehensin.pt.spi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import com.ehensin.pt.RuntimeData;

/**
 * 回调类，压力发生器需要定时回调，传回实时的测试数据，供分析
 * */
public interface ICallBack extends Remote{
    public void callback(Map<String,RuntimeData> data)throws RemoteException;
}
