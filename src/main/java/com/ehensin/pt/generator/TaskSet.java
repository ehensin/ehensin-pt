package com.ehensin.pt.generator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.ehensin.pt.ExeException;
import com.ehensin.pt.RuntimeData;
import com.ehensin.pt.RuntimeParameter;
import com.ehensin.pt.config.Task;
import com.ehensin.pt.spi.ITask;

public class TaskSet implements Runnable {
	private final static Logger logger = org.slf4j.LoggerFactory.getLogger(TaskSet.class);
	private RuntimeParameter parameter;
	private List<ITask> tasks;
	private Map<String, RuntimeData> rdMap;
	/*当前任务集合的编号*/
	private int number;

	public TaskSet(RuntimeParameter parameter, int number) {
		this.parameter = parameter;
		rdMap = new HashMap<String, RuntimeData>();
		this.number = number;
		/* 初始化所有的任务 */
		List<Task> ts = parameter.getTasks();
		/*如果存在用户数据，计算分配给这个用户的用户数据量*/
		List<Serializable> totle = this.parameter.getUserData();
		List<Serializable> localUserData;
		if( totle != null && totle.size() > 0 && totle.size() >= this.parameter.getConcurrents() ){
			int count = totle.size() / this.parameter.getConcurrents();
			localUserData = totle.subList(number * count, number * count + count);
		}else
			localUserData = totle;
		tasks = new ArrayList<ITask>();
		for (Task s : ts) {
			String name = s.getName();
			String clazz = s.getClazz();
			try {
				ITask task = (ITask) Class.forName(clazz).newInstance();
				task.init(name,s.getParameters(), localUserData);
				tasks.add(task);
				rdMap.put(name, new RuntimeData(parameter.getGenUrl()));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public void run() {
		logger.debug(Thread.currentThread().getName() + " 正在执行任务集......");
		Calendar end = Calendar.getInstance();
		end.add(Calendar.MINUTE, this.parameter.getSpan());
		Calendar now = Calendar.getInstance();
		while (end.after(now)) {
			for (ITask task : tasks) {
				RuntimeData data = rdMap.get(task.getTaskName());
				long s = System.currentTimeMillis();
				try {
					task.doTask();
					long e = System.currentTimeMillis();
					data.increment(1, 0, e - s );
					continue;
				} catch (ExeException e) {
					e.printStackTrace();
				}
				long e = System.currentTimeMillis();
				data.increment(0, 1, e - s );
			}
			now = Calendar.getInstance();
		}
		/*结束后清理当前数据*/
		logger.info(rdMap.toString());
		//rdMap.clear();
	}

	public Map<String, RuntimeData> getRuntimeData() {
		return rdMap;
	}
}
