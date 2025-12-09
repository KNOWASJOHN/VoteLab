package com.ty.repository;

import com.ty.model.Vote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    long countByCandidateId(Long candidateId);
}
