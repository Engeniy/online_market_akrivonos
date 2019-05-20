package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.Profile;
import ru.mail.krivonos.al.service.converter.ProfileConverter;
import ru.mail.krivonos.al.service.model.ProfileDTO;

@Component("profileConverter")
public class ProfileConverterImpl implements ProfileConverter {

    @Override
    public ProfileDTO toDTO(Profile profile) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUserId(profile.getUserId());
        profileDTO.setAddress(profile.getAddress());
        profileDTO.setTelephone(profile.getTelephone());
        return profileDTO;
    }

    @Override
    public Profile toEntity(ProfileDTO profileDTO) {
        Profile profile = new Profile();
        profile.setUserId(profileDTO.getUserId());
        profile.setAddress(profileDTO.getAddress());
        profile.setTelephone(profileDTO.getTelephone());
        return profile;
    }
}
