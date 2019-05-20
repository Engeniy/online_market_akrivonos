package ru.mail.krivonos.al.service.converter;

public interface UserConverterAggregator {

    UserConverter getUserAuthorizationConverter();

    UserConverter getUserForProfileConverter();

    UserConverter getAuthorConverter();

    UserConverter getUserForShowingConverter();
}
