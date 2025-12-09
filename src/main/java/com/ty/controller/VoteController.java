package com.ty.controller;

import com.ty.dto.VoteRequest;
import com.ty.dto.VoteResponse;
import com.ty.model.User;
import com.ty.service.UserService;
import com.ty.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;
    private final UserService userService;

    public VoteController(VoteService voteService, UserService userService) {
        this.voteService = voteService;
        this.userService = userService;
    }

    @PostMapping
    public VoteResponse castVote(@Valid @RequestBody VoteRequest request) {
        User user = currentUser();
        if (user.getRole() != com.ty.model.Role.USER) {
            throw new IllegalStateException("Admins cannot vote");
        }
        return voteService.castVote(user.getId(), request);
    }

    @GetMapping("/me")
    public VoteResponse myVote() {
        User user = currentUser();
        return voteService.getVoteForUser(user.getId());
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }
        return userService.getByUsername(auth.getName());
    }
}
