package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.ItemDTO;

public interface ItemService {

    PageDTO<ItemDTO> getItems(int pageNumber);
}
