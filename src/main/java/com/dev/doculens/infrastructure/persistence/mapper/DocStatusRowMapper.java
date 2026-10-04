package com.dev.doculens.infrastructure.persistence.mapper;

import com.dev.doculens.domain.model.DocStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class DocStatusRowMapper implements RowMapper<DocStatus> {

    @Override
    public DocStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DocStatus(
                rs.getObject("id", UUID.class),
                rs.getString("file_name"),
                rs.getString("content_type"),
                rs.getLong("file_size"),
                rs.getString("file_hash"),
                rs.getString("storage_key"),
                rs.getString("status"),
                rs.getObject("created_at", OffsetDateTime.class),
                rs.getObject("updated_at", OffsetDateTime.class)
        );
    }
}
