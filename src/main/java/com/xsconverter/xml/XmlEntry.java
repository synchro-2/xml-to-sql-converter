package com.xsconverter.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Entry")
public class XmlEntry {

	private String content;
	private Date creationDate;

	public String getContent() {
		return content;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	@XmlElement
	public void setContent(String content) {
		this.content = content;
	}

	@XmlElement
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "Entity [content=" + content + ", creationDate=" + creationDate + "]";
	}
}
