package com.ehensin.pt.preparer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ehensin.pt.ExeException;
import com.ehensin.pt.ExeResult;
import com.ehensin.pt.config.Parameter;
import com.ehensin.pt.config.Preparer;
import com.ehensin.pt.spi.IOutputer;
import com.ehensin.pt.spi.IPreparer;
import com.ehensin.pt.util.FileReader;
import com.ehensin.pt.util.FileWriter;

/**
 * 将包含彩民文件信息的数据导入到数据库中,并将彩民身份证编号信息作为列表传给Controller,
 * 由控制器根据generator的数量进行切分后分发到各个generator中去 如果
 * userdata参数里有数据,则读取其数据,如果没有数据则根据maxnum动态产生数据 并插入userdata所在的文件.
 * 如果dbupdate参数为true则更新数据库相应的表
 * */
public class DefaultUserDataPreparer implements IPreparer {
	final String PRAR_USERDATA = "userdata";
	final String PRAR_MAXNUM = "maxnum";
	final String PRAR_DBUPDATE = "dbupdate";

	String userData;
	long maxNum;
	boolean dbUpdate;
	IOutputer infoOutputer;

	public DefaultUserDataPreparer(Preparer p, IOutputer infoOutputer) {
		this.infoOutputer = infoOutputer;
		List<Parameter> parameters = p.getParameters();
		if (parameters != null) {
			for (Parameter para : parameters) {
				if (PRAR_USERDATA.equals(para.getName())) {
					userData = para.getValue();
				} else if (PRAR_MAXNUM.equals(para.getName())) {
					maxNum = Long.valueOf(para.getValue());
				} else if (PRAR_DBUPDATE.equals(para.getName())) {
					dbUpdate = Boolean.valueOf(para.getValue());
				}
			}
		}
	}

	@Override
	public ExeResult prepare() throws ExeException {
		if (userData == null || maxNum < 0)
			throw new ExeException("参数有问题", null);
		/*
		 * 读取用户数据文件中的用户信息，目前用户数据文件只有彩民身份证编号， 以后可以扩展用户名
		 */
		infoOutputer.print("彩民数据准备中，这可能需要耗费几分钟时间......");
		List<String> userList = this.readUserData(userData);
		if (userList == null || userList.size() <= 0) {
			/* 根据max num 动态产生用户数据此时需要更新数据库中的用户表 */
			userList = createUserData(maxNum, userData);
			dbUpdate = true;
		}
		if (dbUpdate) {
			infoOutputer.print("正在更新彩民数据库......");
			boolean result = false;
			try {
				result = updateDb(userList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (!result)
				throw new ExeException("更新数据库有问题", null);
		}
		infoOutputer.print("彩民数据准备完成......");
		return new ExeResult(true, userList, null);
	}

	private List<String> readUserData(String filePath) {
		File file = new File(filePath);
		if (!file.exists())
			return null;
		try {
			FileReader reader = new FileReader(file.getParent(), file
					.getName());
			String line;
			List<String> results = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				results.add(line);
			}
			return results;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<String> createUserData(long maxNum, String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists())
				file.createNewFile();

			FileWriter writer = new FileWriter(file.getParent(), file
					.getName(), true);
			List<String> results = new ArrayList<String>();
			for (long i = 0; i < maxNum; i++) {
				String userNum = String.format("%018d", i);
				results.add(userNum);
				writer.write(userNum);
			}
			writer.close();
			return results;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	private boolean updateDb(List<String> userData) throws SQLException {
		/**
		 * 
		 * */
		final DefaultUserDataPrepareDao dao = new DefaultUserDataPrepareDao();
		final String payAccountSql = "insert into lboss_pay_account(ID, ACCOUNT_USER_NUM,ACCOUNT_STATUS, ACCOUNT1,ACCOUNT2,ACCOUNT3,ACCOUNT4,ACCOUNT5"
				+ "START_AMOUNT1,START_AMOUNT2,START_AMOUNT3,START_AMOUNT4,START_AMOUNT5,VERSION) "
				+ " values(SEQ_PAY_ACCOUNT.nextval," + "?,?,?,?,?,?,?,?,?,?,?,?,?)";
		final String lotteryUserSql = "insert into LSMP_LOTTERY_USER(ID, LUSER_NUM,LUSER_NAME,LUSER_REAL_NAME,LUSER_PASSWORD,LUSER_PAY_PASSWORD,"
				+ " LUSER_TELEPHONE,LUSER_PHONETYPE,LUSER_PROVINCE_NUM,LUSER_CITY_NUM,LUSER_STATUS,LUSER_REGISTER_CHANNAL,LUSER_STATUS_LAST_CHANGE_TIME,"
				+ " LUSER_REGISTER_TIME, LUSER_LEVEL) "
				+ " values(SEQ_LOTTERY_USER.nextval,"
				+ " ?,?,?,?,?,?,?,?,?,?,?,sysdate,sysdate,?)";

		int count = 0;
		List<List<Object>> payBatch = new ArrayList<List<Object>>();
		List<List<Object>> userBatch = new ArrayList<List<Object>>();
		//PoolThreadFactory ptf = new PoolThreadFactory(new PoolThreadGroup("pt"));
		//ExecutorService pool = Executors.newFixedThreadPool(10, ptf);
		for (String userNum : userData) {
			/* pay account */
			List<Object> data = new ArrayList<Object>();
			data.add(userNum);
			data.add(1);
			data.add(1000000000l);
			data.add(1000000000l);
			data.add(1000000000l);
			data.add(1000000000l);
			data.add(1000000000l);
			data.add(0l);
			data.add(0l);
			data.add(0l);
			data.add(0l);
			data.add(0l);
			data.add(1l);
			payBatch.add(data);
			/* lottery user */
			List<Object> user = new ArrayList<Object>();
			user.add(userNum);
			user.add(userNum);
			user.add(userNum);
			user.add(userNum);
			user.add(userNum);
			user.add("12345678912");
			user.add(1);
			user.add("01");
			user.add("0101");
			user.add(1);
			user.add("1");
			user.add(4);
			userBatch.add(user);

			if (count >= 10000) {
				//pool.execute(new UpdateThread(payAccountSql, lotteryUserSql,payBatch, userBatch));
				dao.batchUpdate(payAccountSql, payBatch);
				dao.batchUpdate(lotteryUserSql, userBatch);
				payBatch =  new ArrayList<List<Object>>(); 
				userBatch = new ArrayList<List<Object>>();
				count = 0;
			}
			count++;
		}
		if (count != 0) {
			dao.batchUpdate(payAccountSql, payBatch);
			dao.batchUpdate(lotteryUserSql, userBatch);			
		}
		//pool.shutdown();
		return true;
	}
	
	class UpdateThread implements Runnable{
		List<List<Object>> tmp1;
		List<List<Object>> tmp2;
		String payAccountSql;
		String lotteryUserSql;
		final DefaultUserDataPrepareDao dao = new DefaultUserDataPrepareDao();
        UpdateThread(String payAccountSql, String lotteryUserSql, List<List<Object>> tmp1, List<List<Object>> tmp2){
        	this.tmp1 = tmp1;
        	this.tmp2 = tmp2;
        	this.payAccountSql = payAccountSql;
        	this.lotteryUserSql = lotteryUserSql;
        }
		@Override
		public void run() {
			try {
				infoOutputer.print(Thread.currentThread().getName() + " is running....");
				dao.batchUpdate(payAccountSql, tmp1);
				dao.batchUpdate(lotteryUserSql, tmp2);
				tmp1.clear();
				tmp2.clear();
			} catch (Exception e) {							
				throw new RuntimeException("更新数据库失败");
			}
			
		}
		
	}
}
