package com.xsconverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownHook extends Thread {

	private static final Logger log = LoggerFactory.getLogger(ShutdownHook.class);

	@Override
	public void run() {
		log.info("Program shutting down.");
	}
}

