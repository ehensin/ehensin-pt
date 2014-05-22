package com.ehensin.pt.spi;

import com.ehensin.pt.ExeException;
import com.ehensin.pt.config.Controller;

public interface IController {
	/**
	 * 初始化控制器
	 * */
    public void init(Controller controller) throws ExeException;
    /**
     * 清理当前压力测试环境
     * */
    public void clean() throws ExeException;
    /**
     * 压测前的准备,主要是数据准备
     * */
    public void prepare()throws ExeException;
    /**
     * 执行压力测试
     * */
    public void execute()throws ExeException;
}
