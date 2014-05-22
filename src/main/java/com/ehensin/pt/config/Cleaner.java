package com.ehensin.pt.config;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
@XmlType
public class Cleaner implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlAttribute(name = "class")
	private String clazz;
	@XmlAttribute(name = "switch")
	private String switcher;
	@XmlElement(name = "sql")
	private List<String> sqls;
	
	@XmlTransient
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
	@XmlTransient
	public String getSwitcher() {
		return switcher;
	}
	public void setSwitcher(String switcher) {
		this.switcher = switcher;
	}
	
	@XmlTransient
	public List<String> getSqls() {
		return sqls;
	}
	public void setSqls(List<String> sqls) {
		this.sqls = sqls;
	}
	
	
}
