package com.xsconverter.xml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.xsconverter.config.Configs;

public class JaxbLoader {

	private static final Logger log = LoggerFactory.getLogger(JaxbLoader.class);

	public static Map<Path, XmlEntry> load(List<Path> files) throws JAXBException, SAXException {
		Unmarshaller jaxbUnmarshaller = createUnmarshaller();
		Map<Path, XmlEntry> entrys = new HashMap<Path, XmlEntry>();

		for (Path file : files) {
			try (FileReader fileReader = new FileReader(file.toFile());) {
				XmlEntry entry = (XmlEntry) jaxbUnmarshaller.unmarshal(fileReader);
				entrys.put(file, entry);
			} catch (UnmarshalException e) {
				entrys.put(file, null);
			} catch (IOException | JAXBException e1) {
				log.error("Can't read file {}", file.getFileName());
			}
		}
		return entrys;
	}

	private static Unmarshaller createUnmarshaller() throws JAXBException, SAXException {
		JAXBContext jaxbContext = JAXBContext.newInstance(XmlEntry.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setEventHandler(new XmlValidationHandler());
		jaxbUnmarshaller.setSchema(getSchema());

		return jaxbUnmarshaller;
	}

	private static Schema getSchema() throws SAXException {
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(new File(Configs.path.schemeFile()));

		return schema;
	}
}
