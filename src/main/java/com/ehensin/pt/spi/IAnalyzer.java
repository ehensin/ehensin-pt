package com.ehensin.pt.spi;

import java.util.Map;

import com.ehensin.pt.RuntimeData;

/*
 * 性能压测分析接口定义
 * */
public interface IAnalyzer {
    public Map<String,RuntimeData> analyze(Map<String,RuntimeData> data);
}
