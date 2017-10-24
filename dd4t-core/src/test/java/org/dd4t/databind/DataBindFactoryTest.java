package org.dd4t.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.io.FileUtils;
import org.dd4t.contentmodel.Component;
import org.dd4t.contentmodel.ComponentPresentation;
import org.dd4t.contentmodel.Field;
import org.dd4t.contentmodel.FieldSet;
import org.dd4t.contentmodel.Page;
import org.dd4t.contentmodel.impl.ComponentImpl;
import org.dd4t.contentmodel.impl.ComponentPresentationImpl;
import org.dd4t.contentmodel.impl.EmbeddedField;
import org.dd4t.contentmodel.impl.FieldSetImpl;
import org.dd4t.contentmodel.impl.PageImpl;
import org.dd4t.contentmodel.impl.TextField;
import org.dd4t.core.databind.DataBinder;
import org.dd4t.core.exceptions.SerializationException;
import org.dd4t.core.util.CompressionUtils;
import org.dd4t.databind.builder.json.JsonDataBinder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataBindFactoryTest {

    private ApplicationContext applicationContext;

    @Before
    public void setUp () throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
    }

    @Test
    public void testDataBindFactory () throws SerializationException, URISyntaxException, IOException {
        DataBinder databinder = applicationContext.getBean(DataBinder.class);

        String page = FileUtils.readFileToString(new File(ClassLoader.getSystemResource("test.json").toURI()));
        Page deserializedPage = databinder.buildPage(CompressionUtils.decompressGZip(CompressionUtils.decodeBase64(page)), PageImpl.class);
        Assert.notNull(deserializedPage, "page cannot be bound");
        Assert.hasLength(deserializedPage.getTitle(), "page has no valid title");
    }

    @Test
    public void testExtensionDataBindFactory () throws SerializationException, URISyntaxException, IOException {
        String page = FileUtils.readFileToString(new File(ClassLoader.getSystemResource("extensiondatapage.json").toURI()));
        DataBinder databinder = applicationContext.getBean(DataBinder.class);
        Page deserializedPage = databinder.buildPage(page, PageImpl.class);
        Assert.notNull(deserializedPage, "page cannot be bound");
        Assert.hasLength(deserializedPage.getTitle(), "page has no valid title");


        String serialized = JsonDataBinder.getGenericMapper().writeValueAsString(deserializedPage);

        assertNotNull(serialized);
        
        String rootFolder = new ClassPathResource(".").getFile().getAbsolutePath();
        FileUtils.write(new File(rootFolder + "/testserialized.json"), serialized, "UTF-8");
    }

    @Test
    public void testDcpDeserialization() throws URISyntaxException, IOException, SerializationException {
        DataBinder databinder = applicationContext.getBean(DataBinder.class);

        String dcp = FileUtils.readFileToString(new File(ClassLoader.getSystemResource("testdcpanimal.json").toURI()));
        Assert.notNull(dcp);
        ComponentPresentation componentPresentation = databinder.buildComponentPresentation(dcp, ComponentPresentation.class);
        Assert.notNull(componentPresentation,"DCP cannot be bound");

    }

    @Test
    public void testEmbeddedSerialization() throws URISyntaxException, SerializationException, IOException {
        String page = FileUtils.readFileToString(new File(ClassLoader.getSystemResource("test.json").toURI()));
        DataBinder databinder = applicationContext.getBean(DataBinder.class);
        Page deserializedPage = databinder.buildPage(CompressionUtils.decompressGZip(CompressionUtils.decodeBase64(page)), PageImpl.class);

        EmbeddedField field = (EmbeddedField) deserializedPage.getComponentPresentations().get(0).getComponent().getContent().get("embeddedTest");

        String serialized = JsonDataBinder.getGenericMapper().writeValueAsString(field);

        System.out.println(serialized);

        Component component = deserializedPage.getComponentPresentations().get(0).getComponent();

        String serializedComponent = JsonDataBinder.getGenericMapper().writeValueAsString(component);

        assertNotNull(deserializedPage);

    }

    @Test
    public void testStaticDataBindFactory () throws SerializationException, URISyntaxException, IOException {


        String page = FileUtils.readFileToString(new File(ClassLoader.getSystemResource("test.json").toURI()));
        DataBinder databinder = applicationContext.getBean(DataBinder.class);
        Page deserializedPage = databinder.buildPage(CompressionUtils.decompressGZip(CompressionUtils.decodeBase64(page)), PageImpl.class);
        Assert.notNull(deserializedPage, "page cannot be bound");
        Assert.hasLength(deserializedPage.getTitle(), "page has no valid title");
    }

    @Test
    public void testJsonDataBinder() {
        DataBinder dataBinder = applicationContext.getBean(DataBinder.class);
        assertNotNull(dataBinder);

    }

    @Test
    public void testDcp() throws URISyntaxException, IOException, SerializationException {
        String dcp = FileUtils.readFileToString(new File(ClassLoader.getSystemResource("newitem.json").toURI()));
        DataBinder dataBinder = applicationContext.getBean(DataBinder.class);

        ComponentPresentation componentPresentation = dataBinder.buildComponentPresentation(dcp, ComponentPresentation.class);

        assertNotNull(componentPresentation);
    }

    @Test
    public void testLoadEmbeddedFieldsFromJson() throws URISyntaxException, SerializationException, IOException {
        //given
        String page = FileUtils.readFileToString(new File(ClassLoader.getSystemResource("page_emb_fieldset.json").toURI()));
        DataBinder databinder = applicationContext.getBean(DataBinder.class);

        //when
        Page deserializedPage = databinder.buildPage(page, PageImpl.class);

        //then
        List<Object> values = deserializedPage.getComponentPresentations().get(0)
                .getComponent().getContent().get("articleBody").getValues();
        assertTrue(values.size() == 1);

        FieldSet fieldSet = (FieldSet) values.get(0);

        Iterator<Map.Entry<String, Field>> iterator = fieldSet.getContent().entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value article body", iterator.next().getValue().getValues().get(0));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testManualPageCreationSerialization() throws JsonProcessingException, SerializationException {
        //given
        PageImpl page = new PageImpl();

        TextField textField = new TextField();
        textField.setTextValues(Collections.singletonList("Subheading 1"));

        FieldSetImpl fieldSet = new FieldSetImpl();
        fieldSet.setContent(getMap("subheading", textField));

        EmbeddedField field = new EmbeddedField();
        field.setEmbeddedValues(Collections.<FieldSet>singletonList(fieldSet));

        ComponentImpl component = new ComponentImpl();
        component.setContent(getMap("articleBody", field));

        ComponentPresentationImpl presentation = new ComponentPresentationImpl();
        presentation.setComponent(component);
        page.setComponentPresentations(Collections.<ComponentPresentation>singletonList(presentation));

        //when
        String asString = serialize(page);

        //then
        assertInnerFields(asString);
    }

    private HashMap<String, Field> getMap(String key, org.dd4t.contentmodel.Field value) {
        HashMap<String, org.dd4t.contentmodel.Field> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private String serialize(PageImpl page) throws JsonProcessingException {
        return JsonDataBinder.getGenericMapper().writeValueAsString(page);
    }

    private void assertInnerFields(String asString) {
        assertTrue("Should contain articleBody in: " + asString, asString.contains("articleBody"));
        assertTrue("Should contain subheading in : " + asString, asString.contains("subheading"));
        assertTrue("Should contain embedded text value Subheading 1 in: " + asString, asString.contains("Subheading " +
                "1"));
    }
}
