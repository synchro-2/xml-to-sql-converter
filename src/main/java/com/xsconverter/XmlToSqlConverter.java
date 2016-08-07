package com.xsconverter;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xsconverter.config.Configs;
import com.xsconverter.database.DatabaseFactory;
import com.xsconverter.file.FileFork;
import com.xsconverter.file.FileService;

public class XmlToSqlConverter {

	private static Thread mainThread;
	private static final Logger log = LoggerFactory.getLogger(XmlToSqlConverter.class);

	public static void main(String[] args) {

		mainThread = Thread.currentThread();

		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		Configs.load();
		DatabaseFactory.init();
		ForkJoinPool pool = new ForkJoinPool(Configs.thread.parallelismLevel());

		while (!mainThread.isInterrupted()) {
			List<Path> files = FileService.fileList(Configs.path.input());

			if (!files.isEmpty()) {
				int size = files.size();
				
				log.info("Start processing {} files", size);
				FileFork fork = new FileFork(files, 0, size);
				pool.invoke(fork);

				if (!mainThread.isInterrupted()) {
					log.info("Done");
				}
			}

			try {
				Thread.sleep(Configs.thread.interval());
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public static void shutdown(Exception e, String s) {
		log.error(s);
		log.debug(e.getMessage(), e);
		shutdown();
	}

	public static void shutdown() {
		if (!mainThread.isInterrupted()) {
			mainThread.interrupt();
		}
	}
}
