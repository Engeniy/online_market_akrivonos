package ru.mail.krivonos.al.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.mail.krivonos.al.service.ItemService;
import ru.mail.krivonos.al.service.model.ItemDTO;

import static ru.mail.krivonos.al.controller.constant.FieldConstants.DESCRIPTION_FIELD;
import static ru.mail.krivonos.al.controller.constant.FieldConstants.NAME_FIELD;
import static ru.mail.krivonos.al.controller.constant.FieldConstants.PRICE_FIELD;
import static ru.mail.krivonos.al.controller.constant.FieldConstants.UNIQUE_NUMBER_FIELD;

@Component("itemValidator")
public class ItemValidator implements Validator {

    private static final int NAME_MAX_LENGTH = 30;
    private static final int DESCRIPTION_MAX_LENGTH = 200;
    private static final int UNIQUE_NUMBER_MAX_LENGTH = 6;
    private static final int UNIQUE_NUMBER_MIN_LENGTH = 5;
    private static final String PRICE_VALIDATION_REGEX = "^(\\d+\\.)?\\d+$";

    private final ItemService itemService;

    @Autowired
    public ItemValidator(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ItemDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, NAME_FIELD, "item.name.empty");
        ValidationUtils.rejectIfEmpty(errors, UNIQUE_NUMBER_FIELD, "item.unique_number.empty");
        ValidationUtils.rejectIfEmpty(errors, PRICE_FIELD, "item.price.empty");
        ValidationUtils.rejectIfEmpty(errors, DESCRIPTION_FIELD, "item.description.empty");
        ItemDTO itemDTO = (ItemDTO) o;
        if (itemDTO.getName() != null && itemDTO.getName().length() > NAME_MAX_LENGTH) {
            errors.rejectValue(NAME_FIELD, "item.name.length");
        }
        if (itemDTO.getUniqueNumber() != null && (itemDTO.getUniqueNumber().length() > UNIQUE_NUMBER_MAX_LENGTH ||
                itemDTO.getUniqueNumber().length() < UNIQUE_NUMBER_MIN_LENGTH)) {
            errors.rejectValue(UNIQUE_NUMBER_FIELD, "item.unique_number.length");
        }
        if (itemDTO.getPrice() != null && !itemDTO.getPrice().matches(PRICE_VALIDATION_REGEX)) {
            errors.rejectValue(PRICE_FIELD, "item.price.format");
        }
        if (itemDTO.getDescription() != null && itemDTO.getDescription().length() > DESCRIPTION_MAX_LENGTH) {
            errors.rejectValue(DESCRIPTION_FIELD, "item.description.length");
        }
        if (itemService.isNotUnique(itemDTO.getUniqueNumber())) {
            errors.rejectValue(UNIQUE_NUMBER_FIELD, "item.unique_number.not_unique");
        }
    }
}
