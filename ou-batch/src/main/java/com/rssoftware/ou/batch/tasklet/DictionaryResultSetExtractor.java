package com.rssoftware.ou.batch.tasklet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.rssoftware.ou.batch.common.DataType;
import com.rssoftware.ou.batch.common.Dictionary;
import com.rssoftware.ou.batch.common.FileType;
import com.rssoftware.ou.batch.field.FieldMapping;

public class DictionaryResultSetExtractor implements ResultSetExtractor<Dictionary> {

	@Override
	public Dictionary extractData(ResultSet rs) throws SQLException, DataAccessException {
		Dictionary dictionary = new Dictionary();

		List<FieldMapping> fieldMappings = new ArrayList<>();
		while (rs.next()) {
			if (rs.isFirst()) {
				dictionary.setFileType(FileType.valueOf(rs.getString("file_type")));
				dictionary.setDelimiter(rs.getString("delimiter"));
				//dictionary.setTargetBeanClass(rs.getString("target_class_name"));
			}
			FieldMapping fldMapping = new FieldMapping();
			fldMapping.setFieldQualifier(rs.getString("field_qualifier"));
			fldMapping.setFieldSequence(rs.getInt("sequence"));
			fldMapping.setStartPosition(rs.getInt("start_position"));
			fldMapping.setEndPosition(rs.getInt("end_position"));
			fldMapping.setDataType(DataType.valueOf(rs.getString("field_data_type")));
			fldMapping.setFieldFormat(rs.getString("field_format"));
			fieldMappings.add(fldMapping);
		}
		dictionary.setFieldMappings(fieldMappings);
		return dictionary;
	}

}
