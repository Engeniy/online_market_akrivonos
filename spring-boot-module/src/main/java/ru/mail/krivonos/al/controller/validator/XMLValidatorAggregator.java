package ru.mail.krivonos.al.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("xmlValidatorAggregator")
public class XMLValidatorAggregator {

    private final XMLFileValidator xmlFileValidator;
    private final XMLItemsValidator xmlItemsValidator;

    @Autowired
    public XMLValidatorAggregator(
            XMLFileValidator xmlFileValidator,
            XMLItemsValidator xmlItemsValidator
    ) {
        this.xmlFileValidator = xmlFileValidator;
        this.xmlItemsValidator = xmlItemsValidator;
    }

    public XMLFileValidator getXMLFileValidator() {
        return xmlFileValidator;
    }

    public XMLItemsValidator getXMLItemsValidator() {
        return xmlItemsValidator;
    }
}
