package com.ehensin.pt.controller;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ehensin.pt.ExeException;
import com.ehensin.pt.ExeResult;
import com.ehensin.pt.RuntimeParameter;
import com.ehensin.pt.config.Cleaner;
import com.ehensin.pt.config.Controller;
import com.ehensin.pt.config.Preparer;
import com.ehensin.pt.config.Runner;
import com.ehensin.pt.config.Runners;
import com.ehensin.pt.spi.IAnalyzer;
import com.ehensin.pt.spi.ICallBack;
import com.ehensin.pt.spi.ICleaner;
import com.ehensin.pt.spi.IController;
import com.ehensin.pt.spi.IGenerator;
import com.ehensin.pt.spi.IOutputer;
import com.ehensin.pt.spi.IPreparer;
import com.ehensin.pt.thread.PoolThreadFactory;
import com.ehensin.pt.thread.PoolThreadGroup;

public class PTController implements IController{
	private IOutputer infoOutputer;
	private ICleaner cleaner;
	private IPreparer preparer;
	private IAnalyzer analyzer;
	private Controller controller;
	@Override
	public void init(Controller controller) throws ExeException {
		this.controller = controller;
		/*初始化信息输出类*/
		String clazz = controller.getOutputer().getClazz();
		String cleanerClazz = controller.getCleaner().getClazz();
		String prepareClazz = controller.getPrepare().getClazz();
		String analyzerClazz = controller.getAnalyzer().getClazz();
		try {
			infoOutputer = (IOutputer)Class.forName(clazz).newInstance();		
			Class<?> t = Class.forName(cleanerClazz);
			Constructor<?> c = t.getConstructor(Cleaner.class, IOutputer.class);
			cleaner = (ICleaner)c.newInstance(controller.getCleaner(), infoOutputer);
			
			t = Class.forName(prepareClazz);
			c = t.getConstructor(Preparer.class, IOutputer.class);
			preparer = (IPreparer)c.newInstance(controller.getPrepare(), infoOutputer);
			
			t = Class.forName(analyzerClazz);
			c = t.getConstructor(IOutputer.class);
			analyzer = (IAnalyzer)c.newInstance(infoOutputer);
		} catch (Exception e) {
			throw new ExeException("无法初始化类实例",e);
		} 
		
	}
	
	@Override
	public void clean() throws ExeException {
		cleaner.clean();
	}
	ExeResult result;
	@Override
	public void prepare() throws ExeException {
		result = preparer.prepare();
	}
	
	@Override
	public void execute() throws ExeException {
		infoOutputer.print("execute.....");
		/*得到当前配置最大的压力发生器的数目*/
		Runners runners = controller.getRunners();
		List<Runner> rs = runners.getRunners();
		if( rs == null || rs.size() <= 0 ){
			throw new ExeException("没有配置压力发生器", null);
		}
		infoOutputer.print("共配置了" + rs.size() + "个压力发生器......");
		PoolThreadFactory ptf = new PoolThreadFactory(new PoolThreadGroup("runner"));
		ExecutorService pool = Executors.newFixedThreadPool(10, ptf);
		
		int runnersize = 0;
		List<Serializable> userData = null;
		Iterator<Serializable> uit = null;
		if( result != null && result.getResult() != null && result.getResult() instanceof List<?>){
			userData = (List<Serializable>)result.getResult();
			runnersize = userData.size()/rs.size();
			uit = userData.iterator();
		}
		Iterator<Runner> it = rs.iterator();
		while ( it.hasNext() ){
			Runner r = it.next();
			RuntimeParameter data = new RuntimeParameter();
			data.setConcurrents(Integer.valueOf(r.getConcurrentusers()));
			data.setSpan(Integer.valueOf(runners.getSpan()));
			data.setPeriod(Integer.valueOf(runners.getPeriod()));
			data.setLog(r.getLog().equals("on"));
			data.setSeq(r.getRunseq().equals("on"));
			data.setTasks(r.getTasks());
			if( uit != null && runnersize != 0){
				int i = 0;
				List<Serializable> ud = new ArrayList<Serializable>();
				while( uit.hasNext() ){
					ud.add(uit.next());
					if( i == runnersize )
						break;
					i++;
				}
				data.setUserData(ud);
			}else{
				data.setUserData(null);
			}
			
			pool.execute(new GRunner(data, r));
		}
		pool.shutdown();
	}
	
	class GRunner implements Runnable{
		RuntimeParameter rdata;
		Runner runner;
        GRunner(RuntimeParameter rdata, Runner runner){
        	this.rdata = rdata;
        	this.runner = runner;
        }
		@Override
		public void run() {
			try {
				infoOutputer.print(Thread.currentThread().getName() + " 正在执行......");
				/*与发生器进行通信*/
				String callback = this.runner.getCallback();
				String generatorUrl = "rmi://"
					+ this.runner.getIp() + ":"
					+ this.runner.getPort() + "/"
					+ runner.getServiceName();
				infoOutputer.print("连接URl : " + generatorUrl);
				IGenerator generator = (IGenerator) Naming.lookup(generatorUrl);
				rdata.setGenUrl(generatorUrl);
				generator.init(rdata);
				/*实例化回调函数*/
				Class<?> t = Class.forName(callback);
				Constructor<?> c = t.getConstructor(IAnalyzer.class);
				ICallBack cb = (ICallBack)c.newInstance(analyzer);
				/*执行压力测试*/
				generator.run(cb);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
		}
		
	}

}
