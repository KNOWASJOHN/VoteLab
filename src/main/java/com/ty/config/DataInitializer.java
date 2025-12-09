package com.ty.config;

import com.ty.dto.CandidateRequest;
import com.ty.model.Role;
import com.ty.repository.CandidateRepository;
import com.ty.repository.UserRepository;
import com.ty.service.CandidateService;
import com.ty.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(UserService userService, UserRepository userRepository,
                               CandidateService candidateService, CandidateRepository candidateRepository) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                userService.createUser("admin", "Admin@123", Role.ADMIN);
            }
            if (!userRepository.existsByUsername("user")) {
                userService.createUser("user", "User@123", Role.USER);
            }
            if (candidateRepository.count() == 0) {
                CandidateRequest c1 = new CandidateRequest();
                c1.setName("Alice Johnson");
                c1.setAge(45);
                c1.setParty("Unity Party");
                c1.setPartyLogoUrl("https://example.com/logo1.png");
                c1.setPictureUrl("https://example.com/pic1.png");
                c1.setDescription("Focused on community and transparency.");
                candidateService.create(c1);

                CandidateRequest c2 = new CandidateRequest();
                c2.setName("Brian Smith");
                c2.setAge(50);
                c2.setParty("Progressive Front");
                c2.setPartyLogoUrl("https://example.com/logo2.png");
                c2.setPictureUrl("https://example.com/pic2.png");
                c2.setDescription("Committed to innovation and growth.");
                candidateService.create(c2);
            }
        };
    }
}
