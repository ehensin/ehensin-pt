package com.ehensin.pt;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.ehensin.pt.config.Cleaner;
import com.ehensin.pt.config.Config;
import com.ehensin.pt.config.Controller;
import com.ehensin.pt.config.Preparer;
import com.ehensin.pt.spi.IController;
import com.ehensin.pt.thread.PoolThreadFactory;
import com.ehensin.pt.thread.PoolThreadGroup;

public class MainEntry {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			/*加载config文件解析并转化成java对象*/
			JAXBContext context = JAXBContext.newInstance(Config.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream in = MainEntry.class.getResourceAsStream("/config.xml");
			Config config = (Config)unmarshaller.unmarshal(in);
			/*获取controller*/
			List<Controller> controllers = config.getControllers();
			if( controllers == null || controllers.size() <= 0 ){
				System.out.println("无法获取控制器信息,程序退出......");
				return;
			}
			PoolThreadFactory ptf = new PoolThreadFactory(new PoolThreadGroup("pt"));
			ExecutorService pool = Executors.newFixedThreadPool(controllers.size(), ptf);	
			for( Controller c : controllers ){
				String clazz = c.getClazz();
				IController controller = (IController)Class.forName(clazz).newInstance();
				pool.execute(new ControllerRunnable(controller, c));
			}
			
			Thread.currentThread().join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	static class ControllerRunnable implements Runnable{
		Controller cconfig;
		IController controller;
		ControllerRunnable(IController controller, Controller cconfig){
			this.cconfig = cconfig;
			this.controller = controller;
		}
		@Override
		public void run() {
			try {
				controller.init(cconfig);				
				/*环境清理*/
				Cleaner cleaner = cconfig.getCleaner();
				if( cleaner != null && cleaner.getSwitcher() != null && cleaner.getSwitcher().equals("on")){
					controller.clean();
				}
				/*数据准备*/
				Preparer preparer = cconfig.getPrepare();
				if( preparer != null && preparer.getSwitcher() != null && preparer.getSwitcher().equals("on")){
					controller.prepare();
				}
				/*执行*/
				controller.execute();
			} catch (ExeException e) {
				e.printStackTrace();
			}
			
			
		}
		
	}

}
