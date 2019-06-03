package ru.mail.krivonos.al.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import static ru.mail.krivonos.al.service.constant.OrderConstants.NAME;

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
    @Transactional
    public PageDTO<ItemDTO> getItems(int pageNumber) {
        PageDTO<ItemDTO> pageDTO = new PageDTO<>();
        int countOfEntities = itemRepository.getCountOfNotDeletedEntities();
        int countOfPages = pageCountingService.getCountOfPages(countOfEntities, ITEMS_LIMIT);
        pageDTO.setCountOfPages(countOfPages);
        int currentPageNumber = pageCountingService.getCurrentPageNumber(pageNumber, countOfPages);
        pageDTO.setCurrentPageNumber(currentPageNumber);
        int offset = pageCountingService.getOffset(currentPageNumber, ITEMS_LIMIT);
        List<Item> items = itemRepository.findAllNotDeletedWithAscendingOrder(ITEMS_LIMIT, offset, NAME);
        List<ItemDTO> itemDTOs = getItemDTOs(items);
        pageDTO.setList(itemDTOs);
        return pageDTO;
    }

    @Override
    @Transactional
    public List<ItemDTO> getItems(int limit, int offset) {
        List<Item> items = itemRepository.findAllNotDeletedWithAscendingOrder(limit, offset, NAME);
        return getItemDTOs(items);
    }

    @Override
    @Transactional
    public ItemDTO deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId);
        if (item == null) {
            return null;
        }
        itemRepository.remove(item);
        return itemConverter.toDTO(item);
    }

    @Override
    @Transactional
    public ItemDTO getItemById(Long itemId) {
        Item item = itemRepository.findByIdNotDeleted(itemId);
        if (item == null) {
            return null;
        }
        return itemConverter.toDTO(item);
    }

    @Override
    @Transactional
    public boolean isNotUnique(String uniqueNumber) {
        Item item = itemRepository.findItemByUniqueNumber(uniqueNumber);
        return item != null;
    }

    @Override
    @Transactional
    public ItemDTO add(ItemDTO itemDTO) {
        Item item = itemConverter.toEntity(itemDTO);
        itemRepository.persist(item);
        return itemConverter.toDTO(item);
    }

    @Override
    @Transactional
    public void add(List<ItemDTO> items) {
        for (ItemDTO item : items) {
            itemRepository.persist(itemConverter.toEntity(item));
        }
    }

    private List<ItemDTO> getItemDTOs(List<Item> items) {
        return items.stream()
                .map(itemConverter::toDTO)
                .collect(Collectors.toList());
    }
}
