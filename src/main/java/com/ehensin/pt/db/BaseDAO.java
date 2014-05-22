/* @()DataImportBaseHandler.java
 *
 * (c) COPYRIGHT 1998-2010 Newcosoft INC. All rights reserved.
 * Newcosoft CONFIDENTIAL PROPRIETARY
 * Newcosoft Advanced Technology and Software Operations
 *
 * REVISION HISTORY:
 * Author             Date                   Brief Description
 * -----------------  ----------     ---------------------------------------
 * hhbzzd            上午10:49:10                init version
 * 
 */
package com.ehensin.pt.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * CLASS:
 * Describe class, extends and implements relationships to other classes.
 * 
 * RESPONSIBILITIES:
 * High level list of things that the class does
 * -) 
 * 
 * COLABORATORS:
 * List of descriptions of relationships with other classes, i.e. uses, contains, creates, calls...
 * -) class   relationship
 * -) class   relationship
 * 
 * USAGE:
 * Description of typical usage of class.  Include code samples.
 * 
 * 
 **/
public abstract class BaseDAO {
	private final Logger log = LoggerFactory.getLogger(BaseDAO.class);

	public Object fetch(String sql, Object[] args) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = ConnectionFactory.getInstance().getConnection();
		try {
			if (sql == null || sql.isEmpty())
				return null;
			//log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			if (args != null && args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					if (args[i] instanceof String) {
						pstmt.setString(i + 1, (String) args[i]);
					} else if (args[i] instanceof Integer) {
						pstmt.setInt(i + 1, (Integer) args[i]);
					} else if (args[i] instanceof Long) {
						pstmt.setLong(i + 1, (Long) args[i]);
					} else if (args[i] instanceof Timestamp) {
						pstmt.setTimestamp(i + 1, (Timestamp) args[i]);
					} else {
						pstmt.setObject(i + 1, args[i]);
					}
				}
			}
			ResultSet set = pstmt.executeQuery();
			return this.build(set);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			ConnectionFactory.getInstance().close(con, pstmt);
		}
	}

	public void update(String sql, Object p) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = ConnectionFactory.getInstance().getConnection();
		try {
			log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			prepareStatment(pstmt, p);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			try {
				con.rollback();
			} catch (SQLException e2) {
				log.error(e2.getMessage(), e2);
			}
			throw e;
		} finally {
			ConnectionFactory.getInstance().close(con, pstmt);
		}
	}

	public void update(String sql, List<Object> p) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = ConnectionFactory.getInstance().getConnection();
		try {
			log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			if (p != null)
				for (int i = 0; i < p.size(); i++) {
					pstmt.setObject(i + 1, p.get(i));
				}
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			try {
				con.rollback();
			} catch (SQLException e2) {
				log.error(e2.getMessage(), e2);
			}
			throw e;
		} finally {
			ConnectionFactory.getInstance().close(con, pstmt);
		}
	}

	public void batchUpdate(String sql, java.util.List<List<Object>> list)
			throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = ConnectionFactory.getInstance().getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			if (list != null)
				for (List<Object> objs : list) {
					for (int i = 0; i < objs.size(); i++) {
						pstmt.setObject(i + 1, objs.get(i));
					}
					pstmt.addBatch();
				}
			pstmt.executeBatch();
			con.commit();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			try {
				con.rollback();
			} catch (SQLException e2) {
				log.error(e2.getMessage(), e2);
			}			
		} finally {
			ConnectionFactory.getInstance().close(con, pstmt);
		}
	}

	/**
	 * 子类需要实现该接口以便能够将返回的数据集转化成合适的业务对象
	 * */
	protected abstract Object build(ResultSet set) throws SQLException;

	/**
	 * 子类需要实现该接口用来提供SQL语句的参数
	 * */
	protected abstract void prepareStatment(PreparedStatement pstmt,
			Object parameter);

}
