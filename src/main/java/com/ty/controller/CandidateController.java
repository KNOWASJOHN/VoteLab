package com.ty.controller;

import com.ty.dto.CandidateRequest;
import com.ty.dto.CandidateResponse;
import com.ty.service.CandidateService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping("/candidates")
    public List<CandidateResponse> getCandidates() {
        return candidateService.getAllSorted();
    }

    @GetMapping("/candidates/{id}")
    public CandidateResponse getCandidate(@PathVariable Long id) {
        return candidateService.getById(id);
    }

    @PostMapping("/admin/candidates")
    public CandidateResponse createCandidate(@Valid @RequestBody CandidateRequest request) {
        return candidateService.create(request);
    }

    @PutMapping("/admin/candidates/{id}")
    public CandidateResponse updateCandidate(@PathVariable Long id, @Valid @RequestBody CandidateRequest request) {
        return candidateService.update(id, request);
    }

    @DeleteMapping("/admin/candidates/{id}")
    public void deleteCandidate(@PathVariable Long id) {
        candidateService.delete(id);
    }
}
