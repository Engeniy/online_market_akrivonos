package ru.mail.krivonos.al.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.mail.krivonos.al.service.ItemService;
import ru.mail.krivonos.al.service.model.ItemDTO;

@Component("xmlItemValidator")
public class XMLItemValidator implements Validator {

    private static final int NAME_MAX_LENGTH = 30;
    private static final int DESCRIPTION_MAX_LENGTH = 200;
    private static final int UNIQUE_NUMBER_MAX_LENGTH = 6;
    private static final int UNIQUE_NUMBER_MIN_LENGTH = 5;
    private static final String PRICE_VALIDATION_REGEX = "^(\\d+\\.)?\\d+$";

    private final ItemService itemService;

    @Autowired
    public XMLItemValidator(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ItemDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ItemDTO itemDTO = (ItemDTO) o;
        if (itemDTO.getName() == null || itemDTO.getName().isEmpty()) {
            errors.rejectValue("file", "file");
        }
        if (itemDTO.getPrice() == null || itemDTO.getPrice().isEmpty()) {
            errors.rejectValue("file", "file");
        }
        if (itemDTO.getDescription() == null || itemDTO.getDescription().isEmpty()) {
            errors.rejectValue("file", "file");
        }
        if (itemDTO.getUniqueNumber() == null || itemDTO.getUniqueNumber().isEmpty()) {
            errors.rejectValue("file", "file");
        }
        if (itemDTO.getName() != null && itemDTO.getName().length() > NAME_MAX_LENGTH) {
            errors.rejectValue("file", "file");
        }
        if (itemDTO.getUniqueNumber() != null && (itemDTO.getUniqueNumber().length() > UNIQUE_NUMBER_MAX_LENGTH ||
                itemDTO.getUniqueNumber().length() < UNIQUE_NUMBER_MIN_LENGTH)) {
            errors.rejectValue("file", "file");
        }
        if (itemDTO.getPrice() != null && !itemDTO.getPrice().matches(PRICE_VALIDATION_REGEX)) {
            errors.rejectValue("file", "file");
        }
        if (itemDTO.getDescription() != null && itemDTO.getDescription().length() > DESCRIPTION_MAX_LENGTH) {
            errors.rejectValue("file", "file");
        }
        if (!itemService.isUnique(itemDTO.getUniqueNumber())) {
            errors.rejectValue("file", "file");
        }
    }
}
