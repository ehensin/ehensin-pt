/* @()StandardBusiness.java
 *
 * (c) COPYRIGHT 1998-2010 Newcosoft INC. All rights reserved.
 * Newcosoft CONFIDENTIAL PROPRIETARY
 * Newcosoft Advanced Technology and Software Operations
 *
 * REVISION HISTORY:
 * Author             Date                   Brief Description
 * -----------------  ----------     ---------------------------------------
 * hhbzzd            下午05:57:36                init version
 * 
 */
package com.ehensin.pt.config;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <pre>
 * CLASS:
 * 注码xml解析类, 用于注码xml和业务对象直接的转换.
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
@XmlRootElement(name = "config")
public class Config implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "controller")
	private List<Controller> controllers;
	
	@XmlTransient
	public List<Controller> getControllers() {
		return controllers;
	}

	public void setControllers(List<Controller> controllers) {
		this.controllers = controllers;
	}
}
