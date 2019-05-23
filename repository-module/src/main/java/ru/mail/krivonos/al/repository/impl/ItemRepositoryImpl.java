package ru.mail.krivonos.al.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.ItemRepository;
import ru.mail.krivonos.al.repository.model.Item;

@Repository("itemRepository")
public class ItemRepositoryImpl extends GenericRepositoryImpl<Long, Item> implements ItemRepository {
}
