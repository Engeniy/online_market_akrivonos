package ru.mail.krivonos.al.repository;

import ru.mail.krivonos.al.repository.model.Item;

public interface ItemRepository extends GenericRepository<Long, Item> {

    Item findItemByUniqueNumber(String uniqueNumber);
}
