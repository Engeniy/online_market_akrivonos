package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.ItemDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import java.util.List;

public interface ItemService {

    PageDTO<ItemDTO> getItems(int pageNumber);

    List<ItemDTO> getItems(int limit, int offset);

    ItemDTO deleteItem(Long itemId);

    ItemDTO getItemById(Long itemId);

    boolean isNotUnique(String uniqueNumber);

    ItemDTO add(ItemDTO itemDTO);

    void add(List<ItemDTO> items);
}
