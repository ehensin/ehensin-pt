package com.ehensin.pt.generator;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.ehensin.pt.RuntimeParameter;
import com.ehensin.pt.spi.ICallBack;
import com.ehensin.pt.spi.IGenerator;
import com.ehensin.pt.thread.PoolThreadFactory;
import com.ehensin.pt.thread.PoolThreadGroup;


public class PTGenerator implements IGenerator {
	public static final int GEN_INIT = 1 ;
	public static final int GEN_RUNNING = 2 ;
	public static final int GEN_STOPED = 3 ;
	private final static Logger logger = org.slf4j.LoggerFactory.getLogger(PTGenerator.class);
	private String ip;
	private int port;
	private String serviceName;
	int status;
	private RuntimeParameter parameter;
	private ExecutorService pool ;
	
	private List<TaskSet> runtimeTaskSets;
	
	@Override
	public void init(RuntimeParameter parameter) throws RemoteException {
		if( status == GEN_RUNNING )
			throw new RemoteException("正在进行压力测试，请稍候初始化......");
		logger.info("压力机正在初始化....." + parameter.getConcurrents());
		status = GEN_INIT;
		this.parameter = parameter;
		if( this.parameter == null || this.parameter.getTasks() == null ||
				this.parameter.getTasks().size() <= 0 || this.parameter.getConcurrents() <= 0 )
			throw new RemoteException("invalid runtime parameters");
		if( pool != null && !pool.isShutdown() ){
			pool.shutdownNow();
		}
		pool = Executors.newFixedThreadPool(this.parameter.getConcurrents(), new PoolThreadFactory(new PoolThreadGroup("generator")));	
		runtimeTaskSets = new ArrayList<TaskSet>();
		
		logger.info("压力机初始化结束.....");
	}

	@Override
	public void run(ICallBack callback) throws RemoteException {
		if( status != GEN_INIT && parameter == null ){
			throw new RemoteException("压力发生器状态出错,请稍候再试......");
		}
		logger.info("压力机正在执行.....");
		status = GEN_RUNNING;
		/*设定结束时间*/
		
		Calendar end = Calendar.getInstance();
		end.add(Calendar.MINUTE, this.parameter.getSpan());
		logger.info("压力机执行结束时间....." + end);
		Calendar now = Calendar.getInstance();
		if( end.before(now) && !pool.isShutdown()){
			pool.shutdownNow();
			status = GEN_STOPED;
			logger.info("结束时间早于开始时间, end:"+ end.getTime() +"now : " + now.getTime() + "...");
			return;
		}
		/*执行所有的任务集合*/
		if( runtimeTaskSets == null )
			runtimeTaskSets = new ArrayList<TaskSet>();
		for( int i = 0 ; i < this.parameter.getConcurrents(); i++ ){
			TaskSet set = new TaskSet(this.parameter, i );
			runtimeTaskSets.add(set);
			pool.execute(set);
		}
		/*设定定时任务用于采集运行时的数据*/
		ScheduledExecutorService ss = Executors.newScheduledThreadPool(1);
		DataCollectionTask dcTask = new DataCollectionTask(runtimeTaskSets,callback, this.parameter.getGenUrl());
		ss.scheduleAtFixedRate(dcTask, 0, this.parameter.getPeriod(), TimeUnit.SECONDS);
		pool.shutdown();
		try {
			pool.awaitTermination(this.parameter.getSpan() + 1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		status = GEN_STOPED;
		ss.shutdownNow();
		/*最后一次收集数据*/
		dcTask.run();
		logger.info("压力机执行结束.....");
	}
	
	
	public void register() {

		logger.debug("generator service is registering");
		createRegistry();
		try {
			System.setProperty("java.rmi.server.hostname", ip);
			UnicastRemoteObject.exportObject(this, port);
			Naming.rebind(
					"rmi://" + ip + ":" + port + "/" + serviceName, this);
		} catch (IOException e) {
			logger.error("register service failed", e);
			return;
		}
		logger.debug("service register success. service name: " + serviceName);
	}

	private Registry createRegistry() {
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(port); // 如果该端口未被注册，则抛异常
			registry.list(); // 拿到该端口注册的rmi对象
		} catch (final Exception e) {
			try {
				registry = LocateRegistry.createRegistry(port);// 捕获异常，端口注册
			} catch (final Exception ee) {
				logger.error("register if remote service failed", ee);
			}
		}
		return registry;
	}
	
	public static void main(String[] args){
		if( args == null || args.length != 3){
			logger.error("启动参数不对，请重新设置.....");
			System.exit(1);			
		}
		PTGenerator generator = new PTGenerator();
		generator.ip = args[0];
		generator.port = Integer.valueOf(args[1]);
		generator.serviceName = args[2];
		generator.register();
		
		
	}

}
