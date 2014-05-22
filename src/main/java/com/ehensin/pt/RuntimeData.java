package com.ehensin.pt;

import java.io.Serializable;

/**
 * 记录压力发生器产生的实时压力数据
 * */
public class RuntimeData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String generator;
	/*执行成功的调用次数*/
	int success;
	/*执行失败的次数*/
	int failed;
	/*平均响应时间，以毫秒为单位*/
	int avgRespTime;
	/*总的响应时间*/
	long totalRespTime;

	/*其他数据*/
	Object other;
	
	public RuntimeData(String genUrl){
		this.generator = genUrl;
	}
	
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getFailed() {
		return failed;
	}
	public void setFailed(int failed) {
		this.failed = failed;
	}
	public int getAvgRespTime() {
		return avgRespTime;
	}
	public void setAvgRespTime(int avgRespTime) {
		this.avgRespTime = avgRespTime;
	}
	public Object getOther() {
		return other;
	}
	public void setOther(Object other) {
		this.other = other;
	}
	
	public long getTotalRespTime() {
		return totalRespTime;
	}
	public void setTotalRespTime(long totalRespTime) {
		this.totalRespTime = totalRespTime;
	}
	
	public String getGenerator() {
		return generator;
	}
	public void setGenerator(String generator) {
		this.generator = generator;
	}
	
	public void increment(int success, int failed, long resp){
		this.success += success;
		this.failed += failed;
		this.totalRespTime += resp;
	}
	
	public String toString(){
		int total = this.success + this.failed;
		if( total == 0 )
			total = 1;
		return this.generator + ": success(" + success + "), failed(" + failed + "), resp(" 
		   + totalRespTime + "), avgRep(" + (float)((float)totalRespTime/total) + " ms) ";
	}
	

}
