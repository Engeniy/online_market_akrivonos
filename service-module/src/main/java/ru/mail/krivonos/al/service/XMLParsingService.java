package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.ItemDTO;

import java.io.InputStream;
import java.util.List;

public interface XMLParsingService {

    List<ItemDTO> getItems(InputStream fileContent);
}
