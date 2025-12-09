package com.ty.dto;

public class CandidateResponse {

    private Long id;
    private String name;
    private int age;
    private String party;
    private String partyLogoUrl;
    private String pictureUrl;
    private String description;

    public CandidateResponse() {
    }

    public CandidateResponse(Long id, String name, int age, String party, String partyLogoUrl, String pictureUrl, String description) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.party = party;
        this.partyLogoUrl = partyLogoUrl;
        this.pictureUrl = pictureUrl;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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
