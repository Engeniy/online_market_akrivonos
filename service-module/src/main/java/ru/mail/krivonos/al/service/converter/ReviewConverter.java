package ru.mail.krivonos.al.service.converter;

import ru.mail.krivonos.al.repository.model.Review;
import ru.mail.krivonos.al.service.model.ReviewDTO;

public interface ReviewConverter {

    ReviewDTO toDTO(Review review);

    Review toEntity(ReviewDTO reviewDTO);
}
