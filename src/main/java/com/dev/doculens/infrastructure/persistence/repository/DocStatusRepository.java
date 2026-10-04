package com.dev.doculens.infrastructure.persistence.repository;

import com.dev.doculens.domain.enums.DocProcessingStatus;
import com.dev.doculens.domain.model.DocStatus;
import com.dev.doculens.infrastructure.persistence.mapper.DocStatusRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
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

    // 1. Insert new doc status record
    public int save(DocStatus doc) {
        String sql = """
                INSERT INTO doc_status (
                    id, file_name, content_type, file_size, file_hash, 
                    storage_key, status, created_at, updated_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        return jdbcTemplate.update(
                sql,
                doc.id(),
                doc.fileName(),
                doc.contentType(),
                doc.fileSize(),
                doc.fileHash(),
                doc.storageKey(),
                doc.status(),
                doc.createdAt() != null ? doc.createdAt() : OffsetDateTime.now(),
                doc.updatedAt() != null ? doc.updatedAt() : OffsetDateTime.now()
        );
    }

    // 2. Find by ID
    public Optional<DocStatus> findById(UUID id) {
        String sql = "SELECT * FROM doc_status WHERE id = ?";
        List<DocStatus> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.stream().findFirst();
    }

    // 3. Find by file hash (duplicate check / deduplication)
    public Optional<DocStatus> findByFileHash(String fileHash) {
        String sql = "SELECT * FROM doc_status WHERE file_hash = ? ORDER BY created_at DESC LIMIT 1";
        List<DocStatus> results = jdbcTemplate.query(sql, rowMapper, fileHash);
        return results.stream().findFirst();
    }

    // 4. Find all by status (e.g. all PENDING or PROCESSING jobs)
    public List<DocStatus> findByStatus(String status) {
        String sql = "SELECT * FROM doc_status WHERE status = ? ORDER BY created_at ASC";
        return jdbcTemplate.query(sql, rowMapper, status);
    }

    public List<DocStatus> findByStatus(DocProcessingStatus status) {
        return findByStatus(status.name());
    }

    // 5. Find by storage key
    public Optional<DocStatus> findByStorageKey(String storageKey) {
        String sql = "SELECT * FROM doc_status WHERE storage_key = ?";
        List<DocStatus> results = jdbcTemplate.query(sql, rowMapper, storageKey);
        return results.stream().findFirst();
    }

    // 6. Find all with pagination / limit
    public List<DocStatus> findAll(int limit, int offset) {
        String sql = "SELECT * FROM doc_status ORDER BY created_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, rowMapper, limit, offset);
    }

    public List<DocStatus> findAll() {
        String sql = "SELECT * FROM doc_status ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // 7. Update status only
    public int updateStatus(UUID id, String status) {
        String sql = """
                UPDATE doc_status 
                SET status = ?, updated_at = CURRENT_TIMESTAMP 
                WHERE id = ?
                """;
        return jdbcTemplate.update(sql, status, id);
    }

    public int updateStatus(UUID id, DocProcessingStatus status) {
        return updateStatus(id, status.name());
    }

    // 8. Update storage key and status together (e.g. after S3 upload finish)
    public int updateStorageKeyAndStatus(UUID id, String storageKey, String status) {
        String sql = """
                UPDATE doc_status 
                SET storage_key = ?, status = ?, updated_at = CURRENT_TIMESTAMP 
                WHERE id = ?
                """;
        return jdbcTemplate.update(sql, storageKey, status, id);
    }

    // 9. Check if record exists by ID
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(1) FROM doc_status WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    // 10. Check if file hash exists (fast duplicate check)
    public boolean existsByFileHash(String fileHash) {
        String sql = "SELECT COUNT(1) FROM doc_status WHERE file_hash = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, fileHash);
        return count != null && count > 0;
    }

    // 11. Count total documents by status
    public long countByStatus(String status) {
        String sql = "SELECT COUNT(1) FROM doc_status WHERE status = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, status);
        return count != null ? count : 0L;
    }

    // 12. Delete by ID
    public int deleteById(UUID id) {
        String sql = "DELETE FROM doc_status WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
