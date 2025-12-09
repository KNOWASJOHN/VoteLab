package com.ty.dto;

import java.time.LocalDateTime;

public class VoteResponse {

    private Long candidateId;
    private String candidateName;
    private LocalDateTime votedAt;

    public VoteResponse() {
    }

    public VoteResponse(Long candidateId, String candidateName, LocalDateTime votedAt) {
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.votedAt = votedAt;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public LocalDateTime getVotedAt() {
        return votedAt;
    }

    public void setVotedAt(LocalDateTime votedAt) {
        this.votedAt = votedAt;
    }
}
