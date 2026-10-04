package com.dev.doculens.infrastructure.persistence.repository;

import com.dev.doculens.domain.model.DocStatus;
import com.dev.doculens.infrastructure.persistence.mapper.DocStatusRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DocStatusRepository {

    private final JdbcTemplate jdbcTemplate;
    private final DocStatusRowMapper rowMapper;

    public DocStatusRepository(JdbcTemplate jdbcTemplate, DocStatusRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    public void save(DocStatus doc) {
        jdbcTemplate.update(
                """
                INSERT INTO doc_status (id, file_name, content_type, file_size, file_hash, storage_key, status, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                doc.id(), doc.fileName(), doc.contentType(), doc.fileSize(),
                doc.fileHash(), doc.storageKey(), doc.status(),
                doc.createdAt(), doc.updatedAt()
        );
    }

    public Optional<DocStatus> findById(UUID id) {
        List<DocStatus> results = jdbcTemplate.query(
                "SELECT * FROM doc_status WHERE id = ?",
                rowMapper, id
        );
        return results.stream().findFirst();
    }

    public List<DocStatus> findAll() {
        return jdbcTemplate.query("SELECT * FROM doc_status ORDER BY created_at DESC", rowMapper);
    }

    public void updateStatus(UUID id, String status) {
        jdbcTemplate.update(
                "UPDATE doc_status SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?",
                status, id
        );
    }
}
