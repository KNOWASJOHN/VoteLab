package com.ty.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CandidateRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    private Integer age;

    @NotBlank(message = "Party is required")
    @Size(max = 255, message = "Party must be at most 255 characters")
    private String party;

    @NotBlank(message = "Party logo URL is required")
    @Size(max = 255, message = "Party logo URL must be at most 255 characters")
    private String partyLogoUrl;

    @NotBlank(message = "Picture URL is required")
    @Size(max = 255, message = "Picture URL must be at most 255 characters")
    private String pictureUrl;

    @Size(max = 2000, message = "Description must be at most 2000 characters")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPartyLogoUrl() {
        return partyLogoUrl;
    }

    public void setPartyLogoUrl(String partyLogoUrl) {
        this.partyLogoUrl = partyLogoUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
