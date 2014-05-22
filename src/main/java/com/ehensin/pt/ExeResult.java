package com.ehensin.pt;

import java.io.Serializable;

public class ExeResult implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isSuccess;
    private Object result;
    private String resultDesc;
    
	public ExeResult(boolean isSuccess, Object result, String resultDesc) {
		super();
		this.isSuccess = isSuccess;
		this.result = result;
		this.resultDesc = resultDesc;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
    
    
}
