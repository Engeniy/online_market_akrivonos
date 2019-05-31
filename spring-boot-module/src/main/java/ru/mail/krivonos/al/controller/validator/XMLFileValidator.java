package ru.mail.krivonos.al.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.xml.sax.SAXException;
import ru.mail.krivonos.al.controller.exceptions.SchemaFileNotFoundException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Component("xmlFileValidator")
public class XMLFileValidator implements Validator {

    private static final String SCHEMA_NOT_FOUND_MESSAGE = "Can't find schema file.";

    @Override
    public boolean supports(Class<?> aClass) {
        return InputStream.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        File schemaFile;
        try {
            schemaFile = ResourceUtils.getFile("classpath:itemschema.xsd");
        } catch (FileNotFoundException e) {
            throw new SchemaFileNotFoundException(SCHEMA_NOT_FOUND_MESSAGE);
        }
        InputStream fileContent = (InputStream) o;
        Source xmlSource = new StreamSource(fileContent);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(schemaFile);
            schema.newValidator().validate(xmlSource);
        } catch (SAXException | IOException e) {
            errors.rejectValue("file", "file");
        }
    }
}
