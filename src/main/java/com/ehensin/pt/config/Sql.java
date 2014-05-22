package com.ehensin.pt.config;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Sql implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@XmlAttribute(name = "class")
		private String clazz;
}
