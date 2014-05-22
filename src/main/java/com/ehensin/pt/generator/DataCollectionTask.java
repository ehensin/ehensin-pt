package com.ehensin.pt.generator;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ehensin.pt.RuntimeData;
import com.ehensin.pt.spi.ICallBack;

public class DataCollectionTask implements Runnable{
	private final static Logger log = LoggerFactory.getLogger(DataCollectionTask.class);
    private ICallBack callback;
    private List<TaskSet> runtimeTaskSets;
    private String gen;
	public DataCollectionTask(List<TaskSet> runtimeTaskSets,ICallBack callback, String gen) {
		this.callback = callback;
		this.runtimeTaskSets = runtimeTaskSets;
		this.gen = gen;
	}

	@Override
	public void run() {
		RuntimeData totalData = new RuntimeData(gen);
		Map<String, RuntimeData> rdMap = new HashMap<String, RuntimeData>();
		for ( TaskSet ts : runtimeTaskSets ){
			Map<String, RuntimeData> tmp = ts.getRuntimeData();
			Iterator<String> it = tmp.keySet().iterator();
			while( it.hasNext() ){
				String name = it.next();
				RuntimeData d = tmp.get(name);
				totalData.increment(d.getSuccess(), d.getFailed(), d.getTotalRespTime());
				RuntimeData rd =  rdMap.get(name);
				if( rd == null ){
					rd = new RuntimeData(gen);
					rdMap.put(name, rd);
				}
				rd.increment(d.getSuccess(), d.getFailed(), 0);
				rd.setTotalRespTime(Math.max(d.getTotalRespTime(),rd.getTotalRespTime()));
			}
			
		}
//		log.info("result : " + totalData);
		try {
			callback.callback(rdMap);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
