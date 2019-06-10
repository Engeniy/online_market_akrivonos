package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.service.converter.ItemConverter;
import ru.mail.krivonos.al.service.converter.ItemConverterAggregator;

@Component("itemConverterAggregator")
public class ItemConverterAggregatorImpl implements ItemConverterAggregator {

    private final ItemConverter itemConverter;
    private final ItemConverter orderItemConverter;

    @Autowired
    public ItemConverterAggregatorImpl(
            @Qualifier("itemConverter") ItemConverter itemConverter,
            @Qualifier("orderItemConverter") ItemConverter orderItemConverter
    ) {
        this.itemConverter = itemConverter;
        this.orderItemConverter = orderItemConverter;
    }

    @Override
    public ItemConverter getItemConverter() {
        return itemConverter;
    }

    @Override
    public ItemConverter getOrderItemConverter() {
        return orderItemConverter;
    }
}
