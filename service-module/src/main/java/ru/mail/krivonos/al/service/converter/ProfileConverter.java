package ru.mail.krivonos.al.service.converter;

import ru.mail.krivonos.al.repository.model.Profile;
import ru.mail.krivonos.al.service.model.ProfileDTO;

public interface ProfileConverter {

    ProfileDTO toDTO(Profile profile);

    Profile toEntity(ProfileDTO profileDTO);
}
