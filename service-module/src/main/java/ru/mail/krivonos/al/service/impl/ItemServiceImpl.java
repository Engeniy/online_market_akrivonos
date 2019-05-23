package ru.mail.krivonos.al.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mail.krivonos.al.repository.ItemRepository;
import ru.mail.krivonos.al.repository.model.Item;
import ru.mail.krivonos.al.service.ItemService;
import ru.mail.krivonos.al.service.PageCountingService;
import ru.mail.krivonos.al.service.converter.ItemConverter;
import ru.mail.krivonos.al.service.model.ItemDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import java.util.List;
import java.util.stream.Collectors;

import static ru.mail.krivonos.al.service.constant.LimitConstants.ITEMS_LIMIT;

@Service("itemService")
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemConverter itemConverter;
    private final PageCountingService pageCountingService;

    @Autowired
    public ItemServiceImpl(
            ItemRepository itemRepository,
            ItemConverter itemConverter,
            PageCountingService pageCountingService
    ) {
        this.itemRepository = itemRepository;
        this.itemConverter = itemConverter;
        this.pageCountingService = pageCountingService;
    }

    @Override
    public PageDTO<ItemDTO> getItems(int pageNumber) {
        PageDTO<ItemDTO> pageDTO = new PageDTO<>();
        int countOfEntities = itemRepository.getCountOfEntities();
        int countOfPages = pageCountingService.getCountOfPages(countOfEntities, ITEMS_LIMIT);
        pageDTO.setCountOfPages(countOfPages);
        int currentPageNumber = pageCountingService.getCurrentPageNumber(pageNumber, countOfPages);
        pageDTO.setCurrentPageNumber(currentPageNumber);
        int offset = pageCountingService.getOffset(currentPageNumber, ITEMS_LIMIT);
        List<Item> items = itemRepository.findAll(ITEMS_LIMIT, offset);
        List<ItemDTO> itemDTOs = getItemDTOs(items);
        pageDTO.setList(itemDTOs);
        return pageDTO;
    }

    private List<ItemDTO> getItemDTOs(List<Item> items) {
        return items.stream()
                .map(itemConverter::toDTO)
                .collect(Collectors.toList());
    }
}
