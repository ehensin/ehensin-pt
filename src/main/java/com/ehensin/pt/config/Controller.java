/* @()Game.java
 *
 * (c) COPYRIGHT 1998-2010 Newcosoft INC. All rights reserved.
 * Newcosoft CONFIDENTIAL PROPRIETARY
 * Newcosoft Advanced Technology and Software Operations
 *
 * REVISION HISTORY:
 * Author             Date                   Brief Description
 * -----------------  ----------     ---------------------------------------
 * hhbzzd            下午08:40:47                init version
 * 
 */
package com.ehensin.pt.config;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * <pre>
 * CLASS:
 * Describe class, extends and implements relationships to other classes.
 * 
 * RESPONSIBILITIES:
 * High level list of things that the class does
 * -) 
 * 
 * COLABORATORS:
 * List of descriptions of relationships with other classes, i.e. uses, contains, creates, calls...
 * -) class   relationship
 * -) class   relationship
 * 
 * USAGE:
 * Description of typical usage of class.  Include code samples.
 * 
 * 
 **/
@XmlType
public class Controller implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlAttribute(name = "class")
	private String clazz;
	@XmlElement(name = "cleaner")
	private Cleaner cleaner;
	@XmlElement(name = "preparer")
	private Preparer prepare;
	@XmlElement(name = "analyzer")
	private Analyzer analyzer;
	@XmlElement(name = "runners")
	private Runners runners;
	@XmlElement(name = "outputer")
	private Outputer outputer;
	
	@XmlTransient
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
	@XmlTransient
	public Cleaner getCleaner() {
		return cleaner;
	}
	public void setCleaner(Cleaner cleaner) {
		this.cleaner = cleaner;
	}
	
	@XmlTransient
	public Preparer getPrepare() {
		return prepare;
	}
	public void setPrepare(Preparer prepare) {
		this.prepare = prepare;
	}
	
	@XmlTransient
	public Analyzer getAnalyzer() {
		return analyzer;
	}
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	
	@XmlTransient
	public Runners getRunners() {
		return runners;
	}
	public void setRunners(Runners runners) {
		this.runners = runners;
	}
	
	@XmlTransient
	public Outputer getOutputer() {
		return outputer;
	}
	public void setOutputer(Outputer outputer) {
		this.outputer = outputer;
	}

	
	
}
