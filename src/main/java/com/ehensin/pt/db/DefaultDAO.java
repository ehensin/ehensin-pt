/* @()DefaultDAO.java
 *
 * (c) COPYRIGHT 1998-2010 Newcosoft INC. All rights reserved.
 * Newcosoft CONFIDENTIAL PROPRIETARY
 * Newcosoft Advanced Technology and Software Operations
 *
 * REVISION HISTORY:
 * Author             Date                   Brief Description
 * -----------------  ----------     ---------------------------------------
 * hhbzzd            上午11:37:58                init version
 * 
 */
package com.ehensin.pt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * CLASS:
 *  BaseDAO 的缺省实现
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
public class DefaultDAO extends BaseDAO {

	@Override
	protected Object build(ResultSet set) throws SQLException {
		if (set != null) {
			int column = set.getMetaData().getColumnCount();
			List<Object[]> results = new ArrayList<Object[]>();
			while (set.next()) {
				Object[] objs = new Object[column];
				for (int i = 1; i <= column; i++) {
					objs[i - 1] = set.getObject(i);
				}
				results.add(objs);
			}
			return results;
		}
		return null;
	}

	@Override
	protected void prepareStatment(PreparedStatement pstmt, Object parameter) {
	}

}