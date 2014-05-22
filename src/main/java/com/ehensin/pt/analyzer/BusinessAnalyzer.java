package com.ehensin.pt.analyzer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ehensin.pt.ExeException;
import com.ehensin.pt.RuntimeData;
import com.ehensin.pt.spi.IAnalyzer;
import com.ehensin.pt.spi.IOutputer;

public class BusinessAnalyzer implements IAnalyzer {
	private IOutputer output;
	/*任务类型影射到不同压力发生器下的具体执行情况*/
	private Map<String, Map<String,RuntimeData>> rdMap;
	private Map<String,RuntimeData> totalRdMap;
	public BusinessAnalyzer(IOutputer output){
		this.output = output;
		this.rdMap = new HashMap<String, Map<String,RuntimeData>>();
		this.totalRdMap = new HashMap<String, RuntimeData>();
	}

	@Override
	public Map<String, RuntimeData> analyze(Map<String, RuntimeData> data) {
		
		if( data != null ){
			Iterator<String> it = data.keySet().iterator();
			while( it.hasNext() ){
				String name = it.next();
				RuntimeData d = data.get(name);
				Map<String,RuntimeData> genMap =  rdMap.get(name);
				RuntimeData rd;
				String gen = d.getGenerator();
				if( genMap == null ){
					genMap = new HashMap<String,RuntimeData>();
					rd = new RuntimeData(gen);
					genMap.put(gen, rd);
					rdMap.put(name, genMap);
				}else{
					rd = genMap.get(gen);
					if( rd == null ){
						rd = new RuntimeData(gen);
						genMap.put(gen, rd);
					}
				}
				rd.setSuccess(d.getSuccess());
				rd.setFailed(d.getFailed());
				rd.setTotalRespTime(Math.max(d.getTotalRespTime(),rd.getTotalRespTime()));
				/*计算同一个任务下,所有压力发生器下的和值*/
				RuntimeData totalRd = totalRdMap.get(name);
				if( totalRd == null ){
					totalRd = new RuntimeData(null);
					totalRdMap.put(name, totalRd);
				}
				totalRd.setSuccess(0);
				totalRd.setFailed(0);
				totalRd.setTotalRespTime(0);
				Iterator<RuntimeData> git = genMap.values().iterator();
				while(git.hasNext()){
					RuntimeData tmp = git.next();
					totalRd.setSuccess(totalRd.getSuccess() + tmp.getSuccess());
					totalRd.setFailed(totalRd.getFailed() + tmp.getFailed());
					totalRd.setTotalRespTime(Math.max(tmp.getTotalRespTime(),totalRd.getTotalRespTime()));
				}
				
				
			}
		}
		try {
			this.output.print(rdMap.toString());
			this.output.print("total : " + totalRdMap.toString());
		} catch (ExeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
