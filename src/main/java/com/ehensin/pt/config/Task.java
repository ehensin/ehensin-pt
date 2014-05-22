package com.ehensin.pt.config;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Task implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlAttribute(name = "class")
	private String clazz;
	@XmlAttribute(name = "name")
	private String name;
	@XmlElement(name = "parameter", required=false,nillable=true )
	private List<Parameter> parameters;
	
	
	@XmlTransient
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
	@XmlTransient
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlTransient
	public List<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}


}
