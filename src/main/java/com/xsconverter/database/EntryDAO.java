package com.xsconverter.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import com.xsconverter.xml.XmlEntry;

public class EntryDAO {

	private static final String INSERT_ENTRY = "INSERT INTO entry (content ,creation_date) VALUES(?,?)";

	public static void insertEntity(Collection<XmlEntry> entrys) throws SQLException {
		Connection con = DatabaseFactory.getConnection();
		PreparedStatement ps = con.prepareStatement(INSERT_ENTRY);

		for (XmlEntry entry : entrys) {
			if (entry != null) {
				ps.clearParameters();
				ps.setString(1, entry.getContent());
				ps.setDate(2, new Date(entry.getCreationDate().getTime()));
				ps.addBatch();
			}
		}
		ps.executeBatch();
		ps.close();
		con.close();
	}

}