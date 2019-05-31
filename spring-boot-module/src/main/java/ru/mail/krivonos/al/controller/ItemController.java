package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.krivonos.al.controller.model.UploadForm;
import ru.mail.krivonos.al.controller.validator.ItemValidator;
import ru.mail.krivonos.al.controller.validator.XMLValidatorAggregator;
import ru.mail.krivonos.al.service.ItemService;
import ru.mail.krivonos.al.service.XMLParsingService;
import ru.mail.krivonos.al.service.model.ItemDTO;
import ru.mail.krivonos.al.service.model.OrderDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import java.io.IOException;
import java.util.List;

import static ru.mail.krivonos.al.controller.constant.PageConstants.ITEMS_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ITEMS_UPLOAD_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ITEM_COPY_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ITEM_PAGE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ITEMS_ADD_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ITEMS_COPY_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ITEMS_DELETE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ITEMS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ITEMS_UPLOAD_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ITEM_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_PARAMETER_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.COPIED_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.DELETED_POSITIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.INVALID_CONTENT_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.INVALID_FILE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.PAGE_NUMBER_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.UPLOADED_PARAM;

@Controller("itemController")
public class ItemController {

    private final ItemService itemService;
    private final ItemValidator itemValidator;
    private final XMLValidatorAggregator xmlValidatorAggregator;
    private final XMLParsingService xmlParsingService;

    @Autowired
    public ItemController(
            ItemService itemService,
            ItemValidator itemValidator,
            XMLValidatorAggregator xmlValidatorAggregator,
            XMLParsingService xmlParsingService
    ) {
        this.itemService = itemService;
        this.itemValidator = itemValidator;
        this.xmlValidatorAggregator = xmlValidatorAggregator;
        this.xmlParsingService = xmlParsingService;
    }

    @GetMapping(ITEMS_PAGE_URL)
    public String getItems(
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber, Model model
    ) {
        PageDTO<ItemDTO> pageDTO = itemService.getItems(pageNumber);
        model.addAttribute("page", pageDTO);
        model.addAttribute("order", new OrderDTO());
        return ITEMS_PAGE;
    }

    @PostMapping(ITEMS_DELETE_URL)
    public String deleteItem(
            @RequestParam(name = "item_number") Long itemId,
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber
    ) {
        itemService.deleteItem(itemId);
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, ITEMS_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, DELETED_POSITIVE_PARAM);
    }

    @PostMapping(ITEMS_COPY_URL)
    public String getCopyItemPage(
            @RequestParam(name = "item_number") Long itemId, Model model
    ) {
        ItemDTO item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        return ITEM_COPY_PAGE;
    }

    @PostMapping(ITEMS_ADD_URL)
    public String addItem(
            @ModelAttribute("item") ItemDTO itemDTO, BindingResult bindingResult
    ) {
        itemValidator.validate(itemDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return ITEM_COPY_PAGE;
        }
        itemService.add(itemDTO);
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, ITEMS_PAGE_URL, COPIED_PARAM);
    }

    @GetMapping(ITEM_PAGE_URL)
    public String getItemPage(
            @RequestParam(name = "item_number") Long itemId, Model model
    ) {
        ItemDTO item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        return ITEM_PAGE;
    }

    @GetMapping(ITEMS_UPLOAD_PAGE_URL)
    public String getUploadPage(Model model) {
        model.addAttribute("upload_form", new UploadForm());
        return ITEMS_UPLOAD_PAGE;
    }

    @PostMapping(ITEMS_UPLOAD_PAGE_URL)
    public String uploadItems(
            @ModelAttribute("upload_form") UploadForm uploadForm, BindingResult bindingResult
    ) throws IOException {
        xmlValidatorAggregator.getXMLFileValidator().validate(uploadForm.getFile().getInputStream(), bindingResult);
        if (bindingResult.hasErrors()) {
            return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, ITEMS_UPLOAD_PAGE_URL, INVALID_FILE_PARAM);
        }
        List<ItemDTO> items = xmlParsingService.getItems(uploadForm.getFile().getInputStream());
        xmlValidatorAggregator.getXMLItemsValidator().validate(items, bindingResult);
        if (bindingResult.hasErrors()) {
            return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, ITEMS_UPLOAD_PAGE_URL, INVALID_CONTENT_PARAM);
        }
        itemService.add(items);
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, ITEMS_PAGE_URL, UPLOADED_PARAM);
    }
}
