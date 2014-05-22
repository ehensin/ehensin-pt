package com.ehensin.pt.spi;

import java.io.Serializable;
import java.util.List;

import com.ehensin.pt.ExeException;
import com.ehensin.pt.config.Parameter;

/**
 * 任务接口，用户执行具体的任务
 * */
public interface ITask {
	public void init(String taskName, List<Parameter> parameters, List<Serializable> userData)throws ExeException; 
    public void doTask()throws ExeException; 
    public String getTaskName();
}
