package com.xsconverter.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;;

@Sources({ "classpath:../config/folders.properties" })
public interface PathConfig extends Config {

	@Key("path.input.path")
	@DefaultValue("../data/input/")
	String input();

	@Key("path.output.path")
	@DefaultValue("../data/output/")
	String output();

	@Key("path.error.path")
	@DefaultValue("../data/error/")
	String error();

	@Key("path.scheme.file")
	@DefaultValue("../data/entry.xsd")
	String schemeFile();
	
	@Key("file.atomic.move")
	@DefaultValue("true")
	boolean atomicMove();
}
