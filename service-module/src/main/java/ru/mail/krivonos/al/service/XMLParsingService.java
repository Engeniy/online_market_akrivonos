package ru.mail.krivonos.al.service;

import org.springframework.web.multipart.MultipartFile;
import ru.mail.krivonos.al.service.model.ItemDTO;

import java.util.List;

public interface XMLParsingService {

    List<ItemDTO> getItems(MultipartFile file);
}
