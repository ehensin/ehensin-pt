package com.ehensin.pt;

import java.io.Serializable;
import java.util.List;

import com.ehensin.pt.config.Task;

/**
 * 发送给压力发生器generator运行时的参数
 * */
public class RuntimeParameter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*运行时间周期，分为单位*/
    int span;
    /*多少时间定期汇总运行时数据，秒为单位*/
    int period;
    /*执行的并发用户数，即线程数*/
    int concurrents;
    /*执行任务的顺序,true为顺序执行，false为随机执行*/
    boolean isSeq;
    /*是否在本机记录日志信息*/
    boolean isLog;
    /*用户数据*/
    List<Serializable> userData;
    /*执行的任务列表*/
    List<Task> tasks;
    /*压力发生器url*/
    String genUrl;
	public int getSpan() {
		return span;
	}
	public void setSpan(int span) {
		this.span = span;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public int getConcurrents() {
		return concurrents;
	}
	public void setConcurrents(int concurrents) {
		this.concurrents = concurrents;
	}
	public boolean isSeq() {
		return isSeq;
	}
	public void setSeq(boolean isSeq) {
		this.isSeq = isSeq;
	}
	public boolean isLog() {
		return isLog;
	}
	public void setLog(boolean isLog) {
		this.isLog = isLog;
	}
	public List<Serializable> getUserData() {
		return userData;
	}
	public void setUserData(List<Serializable> userData) {
		this.userData = userData;
	}
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public String getGenUrl() {
		return genUrl;
	}
	public void setGenUrl(String genUrl) {
		this.genUrl = genUrl;
	}
    
    
    
}
