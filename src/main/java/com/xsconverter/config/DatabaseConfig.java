package com.xsconverter.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;;

@Sources({ "classpath:../config/database.properties" })
public interface DatabaseConfig extends Config {

	@Key("database.driver")
	@DefaultValue("org.postgresql.ds.PGSimpleDataSource")
	String driver();

	@Key("database.url")
	@DefaultValue("jdbc:postgresql://localhost:5432/xml_data")
	String url();

	@Key("database.user")
	@DefaultValue("postgres")
	String user();

	@Key("database.password")
	@DefaultValue("toor")
	String password();

	@Key("database.pool.connections.max")
	@DefaultValue("4")
	int maxConnections();

}
