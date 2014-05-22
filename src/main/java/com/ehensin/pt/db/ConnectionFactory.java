package com.ehensin.pt.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class ConnectionFactory {

	private final Logger log = LoggerFactory.getLogger(ConnectionFactory.class);
	private static ConnectionFactory instance = new ConnectionFactory();

	private BoneCP connectionPool = null;
	private DBConfig dbConfig;

	public static ConnectionFactory getInstance() {
		if( instance.connectionPool == null )
		    instance.init();
		return instance;
	}

	public static ConnectionFactory getInstance(DBConfig dbConfig) {
		if( instance.connectionPool == null ){
		    instance.dbConfig = dbConfig;
		    instance.init();
		}
		return instance;
	}

	private ConnectionFactory() {
	}

	private void init() {
		try {
			if (dbConfig == null){
				Properties parameters = new Properties();
				parameters.load(ConnectionFactory.class
						.getResourceAsStream("/dbConfig.properties"));
				dbConfig = new DBConfig();
				dbConfig.setDbUrl(parameters.getProperty("JdbcUrl"));
				dbConfig.setUserName(parameters.getProperty("Username"));
				dbConfig.setPassword(parameters.getProperty("Password"));
				dbConfig.setMinConnections(Integer.valueOf(parameters
						.getProperty("Min")));
				dbConfig.setMaxConnections(Integer.valueOf(parameters
						.getProperty("Max")));
			}
			Class.forName("oracle.jdbc.OracleDriver");
			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl(dbConfig.getDbUrl());
			config.setUsername(dbConfig.getUserName());
			config.setPassword(dbConfig.getPassword());
			config.setMinConnectionsPerPartition(dbConfig.getMinConnections());
			config.setMaxConnectionsPerPartition(dbConfig.getMaxConnections());
			config.setPartitionCount(1);
			connectionPool = new BoneCP(config);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			if (connectionPool == null)
				throw new SQLException("cannot connect db server");
			conn = connectionPool.getConnection();
			conn.setAutoCommit(false);
			return conn;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		}

	}

	public void close(Connection con, PreparedStatement pstmt) {
		try {
			if (pstmt != null)
				pstmt.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void shutdown() {
		if (connectionPool != null)
			connectionPool.shutdown();
	}
}
