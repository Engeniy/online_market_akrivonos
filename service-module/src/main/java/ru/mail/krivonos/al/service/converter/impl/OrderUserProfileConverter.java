package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.Profile;
import ru.mail.krivonos.al.service.converter.ProfileConverter;
import ru.mail.krivonos.al.service.model.ProfileDTO;

@Component("orderUserProfileConverter")
public class OrderUserProfileConverter implements ProfileConverter {

    @Override
    public ProfileDTO toDTO(Profile profile) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setTelephone(profile.getTelephone());
        return profileDTO;
    }

    @Override
    public Profile toEntity(ProfileDTO profileDTO) {
        throw new UnsupportedOperationException("toEntity() method in unsupported for OrderUserProfileConverter class.");
    }
}
