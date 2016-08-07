package com.xsconverter.config;

import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configs {
	
	private static final Logger log = LoggerFactory.getLogger(Configs.class);

	public static PathConfig path;

	public static DatabaseConfig database;

	public static ThreadConfig thread;

	public static void load() {
		path = ConfigFactory.create(PathConfig.class);
		database = ConfigFactory.create(DatabaseConfig.class);
		thread = ConfigFactory.create(ThreadConfig.class);
		log.info("Configs loaded");
	}
}
