package ru.mail.krivonos.al.service.converter;

public interface ItemConverterAggregator {

    ItemConverter getItemConverter();

    ItemConverter getOrderItemConverter();
}
