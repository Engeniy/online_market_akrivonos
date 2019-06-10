package ru.mail.krivonos.al.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component("xmlFileValidator")
public class XMLFileValidator implements Validator {

    private static final String SCHEMA_FILE_LOCATION = "classpath:itemschema.xsd";

    private static final Logger logger = LoggerFactory.getLogger(XMLFileValidator.class);

    @Override
    public boolean supports(Class<?> aClass) {
        return InputStream.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        MultipartFile file = (MultipartFile) o;
        try (InputStream inputStream = file.getInputStream()) {
            File schemaFile = ResourceUtils.getFile(SCHEMA_FILE_LOCATION);
            Source xmlSource = new StreamSource(inputStream);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaFile);
            schema.newValidator().validate(xmlSource);
        } catch (SAXException | IOException e) {
            logger.error(e.getMessage(), e);
            errors.rejectValue("file", "file");
        }
    }
}
