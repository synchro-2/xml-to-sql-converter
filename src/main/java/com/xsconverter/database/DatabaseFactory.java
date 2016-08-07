package com.xsconverter.database;

import com.xsconverter.XmlToSqlConverter;
import com.xsconverter.config.Configs;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DatabaseFactory {

	private static final Logger log = LoggerFactory.getLogger(DatabaseFactory.class);

	private static DataSource dataSource;
	private static String databaseName;
	private static int databaseMajorVersion;
	private static int databaseMinorVersion;

	public static void init() {
		if (dataSource != null) {
			return;
		}

		try {
			Class.forName(Configs.database.driver()).newInstance();
		} catch (Exception e) {
			String errorMessage = "Error obtaining DB driver";
			XmlToSqlConverter.shutdown(e, errorMessage);
		}

		HikariConfig config = new HikariConfig();
		config.setMaximumPoolSize(4);
		config.setConnectionTimeout(5000);
		config.setDataSourceClassName(Configs.database.driver());
		config.addDataSourceProperty("url", Configs.database.url());
		config.addDataSourceProperty("user", Configs.database.user());
		config.addDataSourceProperty("password", Configs.database.password());

		dataSource = new HikariDataSource(config);

		try {
			Connection con = getConnection();
			DatabaseMetaData dmd = con.getMetaData();
			databaseName = dmd.getDatabaseProductName();
			databaseMajorVersion = dmd.getDatabaseMajorVersion();
			databaseMinorVersion = dmd.getDatabaseMinorVersion();
			con.close();
		} catch (Exception e) {
			String errorMessage = "Error with connection string: " + Configs.database.url();
			XmlToSqlConverter.shutdown(e, errorMessage);
		}
		
		log.info("Successfully connected to database {} {} {}", databaseName, databaseMajorVersion,
				databaseMinorVersion);
	}

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}
