package com.ehensin.pt.config;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Parameter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "name")
	private String name;
	@XmlAttribute(name = "value")
	private String value;
	
	@XmlTransient
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlTransient
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
