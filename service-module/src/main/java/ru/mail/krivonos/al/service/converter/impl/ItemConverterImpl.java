package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.Item;
import ru.mail.krivonos.al.service.converter.ItemConverter;
import ru.mail.krivonos.al.service.model.ItemDTO;

@Component("itemConverter")
public class ItemConverterImpl implements ItemConverter {

    @Override
    public ItemDTO toDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setUniqueNumber(item.getUniqueNumber());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setDescription(item.getDescription());
        return itemDTO;
    }

    @Override
    public Item toEntity(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setName(itemDTO.getName());
        item.setUniqueNumber(itemDTO.getUniqueNumber());
        item.setPrice(itemDTO.getPrice());
        item.setDescription(itemDTO.getDescription());
        return item;
    }
}
