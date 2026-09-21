package com.srm.creditengine.repository;

/**
 * @author DennisFerreira
 * @since 2026-09-21 13:26
 */
import com.srm.creditengine.domain.Receivable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivableRepository extends JpaRepository<Receivable, Long> {
}
