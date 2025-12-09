package com.ty.service;

import com.ty.dto.CandidateRequest;
import com.ty.dto.CandidateResponse;
import com.ty.exception.BadRequestException;
import com.ty.exception.NotFoundException;
import com.ty.model.Candidate;
import com.ty.repository.VoteRepository;
import com.ty.repository.CandidateRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;

    public CandidateService(CandidateRepository candidateRepository, VoteRepository voteRepository) {
        this.candidateRepository = candidateRepository;
        this.voteRepository = voteRepository;
    }

    public List<CandidateResponse> getAllSorted() {
        return candidateRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Map<Long, Long> getVoteCountsByCandidate() {
        return candidateRepository.findAll().stream()
                .collect(Collectors.toMap(Candidate::getId, c -> voteRepository.countByCandidateId(c.getId())));
    }

    public long getTotalVotes() {
        return candidateRepository.findAll().stream()
                .mapToLong(c -> voteRepository.countByCandidateId(c.getId()))
                .sum();
    }

    public CandidateResponse getById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Candidate not found"));
        return toResponse(candidate);
    }

    @Transactional
    public CandidateResponse create(CandidateRequest request) {
        validateAge(request.getAge());
        Candidate candidate = new Candidate();
        applyRequest(candidate, request);
        Candidate saved = candidateRepository.save(candidate);
        return toResponse(saved);
    }

    @Transactional
    public CandidateResponse update(Long id, CandidateRequest request) {
        validateAge(request.getAge());
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Candidate not found"));
        applyRequest(candidate, request);
        Candidate saved = candidateRepository.save(candidate);
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Candidate not found"));
        long votes = voteRepository.countByCandidateId(id);
        if (votes > 0) {
            throw new BadRequestException("Cannot delete candidate with existing votes");
        }
        candidateRepository.delete(candidate);
    }

    private void applyRequest(Candidate candidate, CandidateRequest request) {
        candidate.setName(request.getName());
        candidate.setAge(request.getAge());
        candidate.setParty(request.getParty());
        validateImageUrl(request.getPartyLogoUrl(), "Party logo must be a JPG or PNG");
        validateImageUrl(request.getPictureUrl(), "Picture must be a JPG or PNG");
        candidate.setPartyLogoUrl(request.getPartyLogoUrl());
        candidate.setPictureUrl(request.getPictureUrl());
        candidate.setDescription(request.getDescription());
    }

    private void validateAge(Integer age) {
        if (age == null || age < 18) {
            throw new BadRequestException("Candidate must be at least 18 years old");
        }
    }

    private void validateImageUrl(String url, String message) {
        if (url == null) {
            throw new BadRequestException(message);
        }
        String lower = url.toLowerCase();
        if (!(lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png"))) {
            throw new BadRequestException(message);
        }
    }

    private CandidateResponse toResponse(Candidate candidate) {
        return new CandidateResponse(
                candidate.getId(),
                candidate.getName(),
                candidate.getAge(),
                candidate.getParty(),
                candidate.getPartyLogoUrl(),
                candidate.getPictureUrl(),
                candidate.getDescription()
        );
    }
}
