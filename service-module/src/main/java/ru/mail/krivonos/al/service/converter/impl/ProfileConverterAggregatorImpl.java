package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.service.converter.ProfileConverter;
import ru.mail.krivonos.al.service.converter.ProfileConverterAggregator;

@Component("profileConverterAggregator")
public class ProfileConverterAggregatorImpl implements ProfileConverterAggregator {

    private final ProfileConverter profileConverter;
    private final ProfileConverter orderUserProfileConverter;

    @Autowired
    public ProfileConverterAggregatorImpl(
            @Qualifier("profileConverter") ProfileConverter profileConverter,
            @Qualifier("orderUserProfileConverter") ProfileConverter orderUserProfileConverter
    ) {
        this.profileConverter = profileConverter;
        this.orderUserProfileConverter = orderUserProfileConverter;
    }

    @Override
    public ProfileConverter getProfileConverter() {
        return profileConverter;
    }

    @Override
    public ProfileConverter getOrderUserProfileConverter() {
        return orderUserProfileConverter;
    }
}
