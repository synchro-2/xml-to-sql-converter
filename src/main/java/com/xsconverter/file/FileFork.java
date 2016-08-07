package com.xsconverter.file;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.xsconverter.XmlToSqlConverter;
import com.xsconverter.config.Configs;
import com.xsconverter.database.EntryDAO;
import com.xsconverter.xml.JaxbLoader;
import com.xsconverter.xml.XmlEntry;

@SuppressWarnings("serial")
public class FileFork extends RecursiveAction {

	private List<Path> files;
	private int startPosition;
	private int length;

	public FileFork(List<Path> files, int start, int length) {
		this.files = files;
		this.startPosition = start;
		this.length = length;
	}

	protected void computeDirectly() {
		try {
			List<Path> subFiles = files.subList(startPosition, startPosition + length);
			Map<Path, XmlEntry> entitys = JaxbLoader.load(subFiles);
			EntryDAO.insertEntity(entitys.values());
			FileService.move(entitys);
		} catch (JAXBException e) {
			String errorMessage = "Error while creating unmarshaller";
			XmlToSqlConverter.shutdown(e, errorMessage);
		} catch (SAXException e) {
			String errorMessage = "Error while getting schema file";
			XmlToSqlConverter.shutdown(e, errorMessage);
		} catch (SQLException e) {
			String errorMessage = "Error while inserting query";
			XmlToSqlConverter.shutdown(e, errorMessage);
		} catch (IOException e) {
			String errorMessage = "Error while moving file";
			XmlToSqlConverter.shutdown(e, errorMessage);
		}
	}

	protected void compute() {
		if (length < Configs.thread.taskMaxSize()) {
			computeDirectly();
			return;
		}
		int split = length / 2;
		invokeAll(new FileFork(files, startPosition, split),
				new FileFork(files, startPosition + split, length - split));
	}
}
