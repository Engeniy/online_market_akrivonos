package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.service.converter.UserConverter;
import ru.mail.krivonos.al.service.converter.UserConverterAggregator;

@Component("userConverterAggregator")
public class UserConverterAggregatorImpl implements UserConverterAggregator {

    private final UserConverter userAuthorizationConverter;
    private final UserConverter userForProfileConverter;
    private final UserConverter authorConverter;
    private final UserConverter userForShowingConverter;
    private final UserConverter orderUserConverter;

    @Autowired
    public UserConverterAggregatorImpl(
            @Qualifier("userAuthorizationConverter") UserConverter userAuthorizationConverter,
            @Qualifier("userForProfileConverter") UserConverter userForProfileConverter,
            @Qualifier("authorConverter") UserConverter authorConverter,
            @Qualifier("userForShowingConverter") UserConverter userForShowingConverter,
            @Qualifier("orderUserConverter") UserConverter orderUserConverter
    ) {
        this.userAuthorizationConverter = userAuthorizationConverter;
        this.userForProfileConverter = userForProfileConverter;
        this.authorConverter = authorConverter;
        this.userForShowingConverter = userForShowingConverter;
        this.orderUserConverter = orderUserConverter;
    }

    @Override
    public UserConverter getUserAuthorizationConverter() {
        return userAuthorizationConverter;
    }

    @Override
    public UserConverter getUserForProfileConverter() {
        return userForProfileConverter;
    }

    @Override
    public UserConverter getAuthorConverter() {
        return authorConverter;
    }

    @Override
    public UserConverter getUserForShowingConverter() {
        return userForShowingConverter;
    }

    @Override
    public UserConverter getOrderUserConverter() {
        return orderUserConverter;
    }
}
