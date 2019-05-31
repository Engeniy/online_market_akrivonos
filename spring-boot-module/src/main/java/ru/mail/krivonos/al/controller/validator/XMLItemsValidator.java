package ru.mail.krivonos.al.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.mail.krivonos.al.service.ItemService;
import ru.mail.krivonos.al.service.model.ItemDTO;

import java.util.List;

@Component("xmlItemsValidator")
public class XMLItemsValidator implements Validator {

    private final ItemService itemService;

    @Autowired
    public XMLItemsValidator(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return List.class.equals(aClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(Object o, Errors errors) {
        List<ItemDTO> items = (List<ItemDTO>) o;
        for (ItemDTO item : items) {
            if (itemService.isNotUnique(item.getUniqueNumber())) {
                errors.rejectValue("file", "file");
            }
        }
    }
}
