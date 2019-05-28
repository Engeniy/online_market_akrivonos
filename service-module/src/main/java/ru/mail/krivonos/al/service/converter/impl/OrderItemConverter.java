package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.Item;
import ru.mail.krivonos.al.service.converter.ItemConverter;
import ru.mail.krivonos.al.service.model.ItemDTO;

@Component("orderItemConverter")
public class OrderItemConverter implements ItemConverter {

    @Override
    public ItemDTO toDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName(item.getName());
        return itemDTO;
    }

    @Override
    public Item toEntity(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.getId());
        return item;
    }
}
