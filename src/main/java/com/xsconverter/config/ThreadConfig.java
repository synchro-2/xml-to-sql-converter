package com.xsconverter.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;;

@Sources({ "classpath:../config/thread.properties" })
public interface ThreadConfig extends Config {

	@Key("thread.parallelism.level")
	@DefaultValue("4")
	int parallelismLevel();

	@Key("thread.task.size.limit")
	@DefaultValue("100")
	int taskMaxSize();

	@Key("thread.interval")
	@DefaultValue("200")
	int interval();
}
