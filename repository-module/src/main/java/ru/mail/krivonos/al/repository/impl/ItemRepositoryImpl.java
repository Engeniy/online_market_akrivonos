package ru.mail.krivonos.al.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.ItemRepository;
import ru.mail.krivonos.al.repository.model.Item;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository("itemRepository")
public class ItemRepositoryImpl extends GenericRepositoryImpl<Long, Item> implements ItemRepository {

    @Override
    public Item findItemByUniqueNumber(String uniqueNumber) {
        String queryString = String.format("from %s %s", entityClass.getName(),
                " where unique_number = :unique_number");
        Query query = entityManager.createQuery(queryString).setParameter("unique_number", uniqueNumber);
        try {
            return (Item) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
