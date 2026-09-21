package com.srm.creditengine.repository;

/**
 * @author DennisFerreira
 * @since 2026-09-21 13:26
 */
import com.srm.creditengine.domain.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    Optional<Settlement> findByIdempotencyKey(String idempotencyKey);
}
