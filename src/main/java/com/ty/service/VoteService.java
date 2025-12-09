package com.ty.service;

import com.ty.dto.VoteRequest;
import com.ty.dto.VoteResponse;
import com.ty.exception.AlreadyVotedException;
import com.ty.exception.NotFoundException;
import com.ty.model.Candidate;
import com.ty.model.User;
import com.ty.model.Vote;
import com.ty.repository.CandidateRepository;
import com.ty.repository.UserRepository;
import com.ty.repository.VoteRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository voteRepository, CandidateRepository candidateRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VoteResponse castVote(Long userId, VoteRequest request) {
        if (voteRepository.existsByUserId(userId)) {
            throw new AlreadyVotedException("User has already voted and cannot change the vote");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new NotFoundException("Candidate not found"));

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setCandidate(candidate);
        vote.setVotedAt(LocalDateTime.now());
        Vote saved = voteRepository.save(vote);

        return new VoteResponse(candidate.getId(), candidate.getName(), saved.getVotedAt());
    }

    public VoteResponse getVoteForUser(Long userId) {
        return voteRepository.findByUserId(userId)
                .map(vote -> new VoteResponse(
                        vote.getCandidate().getId(),
                        vote.getCandidate().getName(),
                        vote.getVotedAt()
                ))
                .orElse(null);
    }
}
