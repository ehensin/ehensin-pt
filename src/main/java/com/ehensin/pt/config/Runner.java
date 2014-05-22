package com.ehensin.pt.config;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
@XmlType
public class Runner implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlAttribute(name = "ip")
	private String ip;
	@XmlAttribute(name = "port")
	private String port;
	@XmlAttribute(name = "concurrentusers")
	private String concurrentusers;
	@XmlAttribute(name = "runseq")
	private String runseq;
	@XmlAttribute(name = "userdata")
	private String userdata;
	@XmlAttribute(name = "log")
	private String log;
	@XmlAttribute(name = "callback")
	private String callback;
	@XmlAttribute(name = "serviceName")
	private String serviceName;
	@XmlElement(name = "task")
	private List<Task> tasks;
	
	@XmlTransient
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@XmlTransient
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
	@XmlTransient
	public String getConcurrentusers() {
		return concurrentusers;
	}
	public void setConcurrentusers(String concurrentusers) {
		this.concurrentusers = concurrentusers;
	}
	
	@XmlTransient
	public String getRunseq() {
		return runseq;
	}
	public void setRunseq(String runseq) {
		this.runseq = runseq;
	}
	
	@XmlTransient
	public String getUserdata() {
		return userdata;
	}
	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}
	
	@XmlTransient
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	
	@XmlTransient
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	
	@XmlTransient
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	@XmlTransient
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	

}
