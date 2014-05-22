package com.ehensin.pt.cleaner;

import java.sql.SQLException;
import java.util.List;

import com.ehensin.pt.ExeException;
import com.ehensin.pt.ExeResult;
import com.ehensin.pt.config.Cleaner;
import com.ehensin.pt.spi.ICleaner;
import com.ehensin.pt.spi.IOutputer;
/**
 * 业务数据清理，在执行压力测试的时候进行业务数据的清理
 * */
public class BusinessDataCleaner implements ICleaner {
	BusinessDataCleanerDao dao = new BusinessDataCleanerDao();
	Cleaner cleaner;
	IOutputer infoOutputer;
	public BusinessDataCleaner(Cleaner cleaner,IOutputer infoOutputer){
	    this.cleaner = cleaner;	
	    this.infoOutputer = infoOutputer;
	}
	@Override
	public ExeResult clean() throws ExeException {
		this.infoOutputer.print("正在执行清理操作，这可能需要花费几分钟时间......");
		List<String> sqls =  cleaner.getSqls();
		if( sqls == null || sqls.size() <= 0 )
			return new ExeResult(true, null, "没有可执行的sql语句");
		
		try {
			for( String sql : sqls ){
				this.infoOutputer.print("正在执行：" + sql);
				dao.update(sql, null);
			}
		} catch (SQLException e) {
			throw new ExeException("执行SQL语句发生异常", e);
		}
		this.infoOutputer.print("清理操作执行完毕......");
		return new ExeResult(true, null, null);
	}

}
