package com.company.framework.utils;

import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.StringReader;


public class SoapHelper {

    public boolean validate(String response, String schemaFile) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(SoapHelper.class.getResource(schemaFile));

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(response)));
            return true;
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
