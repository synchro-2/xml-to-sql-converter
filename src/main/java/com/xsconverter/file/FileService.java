package com.xsconverter.file;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.nio.file.LinkOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xsconverter.XmlToSqlConverter;
import com.xsconverter.config.Configs;
import com.xsconverter.xml.XmlEntry;

public class FileService {

	private static final Logger log = LoggerFactory.getLogger(FileService.class);

	public static List<Path> fileList(String directory) {
		List<Path> files = new ArrayList<>();

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
			for (Path file : directoryStream) {
				if (Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS) && Files.isReadable(file)
						&& file.toString().endsWith(".xml")) {
					files.add(file);
				}
			}
		} catch (IOException e) {
			String errorMessage = "Error while reading input directory " + directory;
			XmlToSqlConverter.shutdown(e, errorMessage);
		}
		return files;
	}

	public static void move(Map<Path, XmlEntry> entitys) throws IOException {

		for (Entry<Path, XmlEntry> entry : entitys.entrySet()) {
			Path targetFile;
			Path sourceFile = entry.getKey();

			if (entry.getValue() != null) {
				targetFile = Paths.get(Configs.path.output(), sourceFile.getFileName().toString());
			} else {
				targetFile = Paths.get(Configs.path.error(), sourceFile.getFileName().toString());
			}

			if (Files.exists(targetFile)) {
				log.warn("File {} already exist in distination folder, renaming", targetFile.getFileName());
				targetFile = Paths.get(targetFile.toString() + Long.toString(System.currentTimeMillis()));
			}

			if (Configs.path.atomicMove()) {
				Files.move(sourceFile, targetFile, StandardCopyOption.ATOMIC_MOVE);
			} else {
				Files.move(sourceFile, targetFile);
			}
		}
	}
}
