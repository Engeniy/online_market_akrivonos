package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.ItemDTO;

import java.util.List;

public interface ItemService {

    PageDTO<ItemDTO> getItems(int pageNumber);

    List<ItemDTO> getItems(int limit, int offset);

    void deleteItem(Long itemId);

    ItemDTO getItemById(Long itemId);

    boolean isUnique(String uniqueNumber);

    ItemDTO add(ItemDTO itemDTO);
}
