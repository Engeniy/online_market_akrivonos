package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.krivonos.al.service.ItemService;
import ru.mail.krivonos.al.service.model.ItemDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import static ru.mail.krivonos.al.controller.constant.PageConstants.ITEMS_PAGE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ITEMS_PAGE_URL;

@Controller("itemController")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(ITEMS_PAGE_URL)
    public String getItems(
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber, Model model
    ) {
        PageDTO<ItemDTO> pageDTO = itemService.getItems(pageNumber);
        model.addAttribute("page", pageDTO);
        return ITEMS_PAGE;
    }
}
