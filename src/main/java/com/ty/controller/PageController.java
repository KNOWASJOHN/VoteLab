package com.ty.controller;

import com.ty.dto.CandidateRequest;
import com.ty.dto.VoteRequest;
import com.ty.exception.BadRequestException;
import com.ty.exception.NotFoundException;
import com.ty.model.Role;
import com.ty.model.User;
import com.ty.service.CandidateService;
import com.ty.service.UserService;
import com.ty.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {

    private final CandidateService candidateService;
    private final VoteService voteService;
    private final UserService userService;

    public PageController(CandidateService candidateService, VoteService voteService, UserService userService) {
        this.candidateService = candidateService;
        this.voteService = voteService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(Authentication authentication, Model model,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout) {
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("infoMessage", "You have been logged out");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Authentication authentication, Model model,
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "success", required = false) String success) {
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        if (success != null) {
            model.addAttribute("infoMessage", success);
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (password == null || password.length() < 8) {
                redirectAttributes.addAttribute("error", "Password must be at least 8 characters");
                return "redirect:/register";
            }
            userService.createUser(username, password, Role.USER);
            redirectAttributes.addAttribute("success", "Registration successful. Please log in.");
            return "redirect:/login";
        } catch (RuntimeException ex) {
            redirectAttributes.addAttribute("error", ex.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        User user = currentUser();
        model.addAttribute("candidates", candidateService.getAllSorted());
        model.addAttribute("vote", voteService.getVoteForUser(user.getId()));
        model.addAttribute("isAdmin", user.getRole() == Role.ADMIN);
        if (user.getRole() == Role.ADMIN) {
            model.addAttribute("voteCounts", candidateService.getVoteCountsByCandidate());
            model.addAttribute("totalVotes", candidateService.getTotalVotes());
        }
        return "dashboard";
    }

    @PostMapping("/vote")
    public String castVote(@RequestParam("candidateId") Long candidateId, RedirectAttributes redirectAttributes) {
        User user = currentUser();
        VoteRequest request = new VoteRequest();
        request.setCandidateId(candidateId);
        try {
            voteService.castVote(user.getId(), request);
            redirectAttributes.addFlashAttribute("successMessage", "Vote submitted successfully");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage(Model model) {
        model.addAttribute("candidates", candidateService.getAllSorted());
        model.addAttribute("voteCounts", candidateService.getVoteCountsByCandidate());
        model.addAttribute("totalVotes", candidateService.getTotalVotes());
        if (!model.containsAttribute("candidateForm")) {
            model.addAttribute("candidateForm", new CandidateRequest());
        }
        return "admin";
    }

    @PostMapping("/admin/candidates")
    @PreAuthorize("hasRole('ADMIN')")
    public String createCandidate(@Valid @ModelAttribute("candidateForm") CandidateRequest candidateRequest,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("candidateForm", candidateRequest);
            redirectAttributes.addFlashAttribute("errorMessage", "Please fix validation errors");
            return "redirect:/admin";
        }
        try {
            candidateService.create(candidateRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate created");
        } catch (BadRequestException ex) {
            redirectAttributes.addFlashAttribute("candidateForm", candidateRequest);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/candidates/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateCandidate(@PathVariable Long id,
                                  @Valid CandidateRequest candidateRequest,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please fix validation errors");
            return "redirect:/admin";
        }
        try {
            candidateService.update(id, candidateRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate updated");
        } catch (NotFoundException | BadRequestException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/candidates/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCandidate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            candidateService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate deleted");
        } catch (NotFoundException | BadRequestException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin";
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("User not authenticated");
        }
        return userService.getByUsername(auth.getName());
    }
}
