package com.ehensin.pt.preparer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ehensin.pt.db.BaseDAO;

public class DefaultUserDataPrepareDao extends BaseDAO{
    
	@Override
	protected Object build(ResultSet set) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareStatment(PreparedStatement pstmt, Object parameter) {
		
	}

}
