package com.ehensin.pt.config;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Runners implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "runner")
	private List<Runner> runners;
	@XmlAttribute(name = "span")
	private String span;
	@XmlAttribute(name = "period")
	private String period;
	
	@XmlTransient
	public List<Runner> getRunners() {
		return runners;
	}

	public void setRunners(List<Runner> runners) {
		this.runners = runners;
	}

	@XmlTransient
	public String getSpan() {
		return span;
	}

	public void setSpan(String span) {
		this.span = span;
	}

	@XmlTransient
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	
	
}
