package com.kerneldc.flightlogserver.batch.copyMakeModelTable;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.kerneldc.flightlogserver.domain.makeModel.MakeModel;

public class MakeModelRowMapper implements RowMapper<MakeModel> {

    public static final String ID_COLUMN = "id";
    public static final String MAKE_MODEL_COLUMN = "make_model";
    public static final String CREATED_COLUMN = "created";
    public static final String MODIFIED_COLUMN = "modified";
    public static final String VERSION_COLUMN = "version";

	@Override
	public MakeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		MakeModel makeModel = new MakeModel();
		makeModel.setId(rs.getLong(ID_COLUMN));
		makeModel.setMakeModel(rs.getString(MAKE_MODEL_COLUMN));
		makeModel.setCreated(rs.getDate(CREATED_COLUMN));
		makeModel.setModified(rs.getDate(MODIFIED_COLUMN));
		makeModel.setVersion(rs.getLong(VERSION_COLUMN));
		return makeModel;
	}

}
