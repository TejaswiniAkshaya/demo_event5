package com.example.event5.demo_event5.service;

import com.example.event5.demo_event5.model.Contact;
import com.example.event5.demo_event5.model.Profile;
import com.example.event5.demo_event5.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    public Page<Profile> getEvents(String year,Pageable pageable) {
        return profileRepository.findByYear(year,pageable);
    }

    public Page<Profile> getProfilesWithPagination(String query, Pageable pageable) {
        return profileRepository.findByFirstNameContainingOrLastNameContainingOrUserNameContainingOrRollnoContainingOrBranchContainingOrCGPAContainingOrSectionContainingAndYearContaining(query,query,query,query,query,query,query,"3rdYear",pageable);
    }

    public Page<Profile> getProfilesWithPagination1(String query, Pageable pageable) {
        return profileRepository.findByFirstNameContainingOrLastNameContainingOrUserNameContainingOrRollnoContainingOrBranchContainingOrCGPAContainingOrSectionContainingAndYearContaining(query,query,query,query,query,query,query,"4thYear",pageable);
    }
}
