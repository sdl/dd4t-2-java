package org.dd4t.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dd4t.contentmodel.ComponentPresentation;
import org.dd4t.contentmodel.Field;
import org.dd4t.contentmodel.FieldSet;
import org.dd4t.contentmodel.impl.ComponentImpl;
import org.dd4t.contentmodel.impl.ComponentPresentationImpl;
import org.dd4t.contentmodel.impl.EmbeddedField;
import org.dd4t.contentmodel.impl.FieldSetImpl;
import org.dd4t.contentmodel.impl.PageImpl;
import org.dd4t.contentmodel.impl.TextField;
import org.dd4t.core.exceptions.SerializationException;
import org.dd4t.databind.builder.json.JsonDataBinder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class FieldSetNotSerializableTest {

    private static final String ORIGINAL_DD4T =
            "{\n" +
                    "  \"RevisionDate\": \"2016-02-02T13:22:30\",\n" +
                    "  \"Filename\": \"test_article_page\",\n" +
                    "  \"LastPublishedDate\": \"0001-01-01T00:00:00\",\n" +
                    "  \"PageTemplate\": {\n" +
                    "    \"FileExtension\": \"html\",\n" +
                    "    \"RevisionDate\": \"2014-06-25T14:37:02.387\",\n" +
                    "    \"MetadataFields\": {\n" +
                    "      \"includes\": {\n" +
                    "        \"Name\": \"includes\",\n" +
                    "        \"Values\": [\n" +
                    "          \"system/include/header\",\n" +
                    "          \"system/include/footer\",\n" +
                    "          \"system/include/content-tools\"\n" +
                    "        ],\n" +
                    "        \"NumericValues\": [\n" +
                    "          \n" +
                    "        ],\n" +
                    "        \"DateTimeValues\": [\n" +
                    "          \n" +
                    "        ],\n" +
                    "        \"LinkedComponentValues\": [\n" +
                    "          \n" +
                    "        ],\n" +
                    "        \"FieldType\": 0,\n" +
                    "        \"KeywordValues\": [\n" +
                    "          \n" +
                    "        ]\n" +
                    "      },\n" +
                    "      \"view\": {\n" +
                    "        \"Name\": \"view\",\n" +
                    "        \"Values\": [\n" +
                    "          \"GeneralPage\"\n" +
                    "        ],\n" +
                    "        \"NumericValues\": [\n" +
                    "          \n" +
                    "        ],\n" +
                    "        \"DateTimeValues\": [\n" +
                    "          \n" +
                    "        ],\n" +
                    "        \"LinkedComponentValues\": [\n" +
                    "          \n" +
                    "        ],\n" +
                    "        \"FieldType\": 0,\n" +
                    "        \"KeywordValues\": [\n" +
                    "          \n" +
                    "        ]\n" +
                    "      }\n" +
                    "    },\n" +
                    "    \"Id\": \"tcm:1081-108-128\",\n" +
                    "    \"Title\": \"Content Page Without Navigation\"\n" +
                    "  },\n" +
                    "  \"MetadataFields\": {\n" +
                    "    \n" +
                    "  },\n" +
                    "  \"ComponentPresentations\": [\n" +
                    "    {\n" +
                    "      \"Component\": {\n" +
                    "        \"LastPublishedDate\": \"0001-01-01T00:00:00\",\n" +
                    "        \"RevisionDate\": \"2016-09-22T17:41:36\",\n" +
                    "        \"Schema\": {\n" +
                    "          \"RootElementName\": \"Article\",\n" +
                    "          \"Id\": \"tcm:1081-80-8\",\n" +
                    "          \"Title\": \"Article\"\n" +
                    "        },\n" +
                    "        \"Fields\": {\n" +
                    "          \"headline\": {\n" +
                    "            \"Name\": \"headline\",\n" +
                    "            \"Values\": [\n" +
                    "              \"Test Article used for Automated Testing (Sdl.Web.Tridion.Tests)\"\n" +
                    "            ],\n" +
                    "            \"NumericValues\": [\n" +
                    "              \n" +
                    "            ],\n" +
                    "            \"DateTimeValues\": [\n" +
                    "              \n" +
                    "            ],\n" +
                    "            \"LinkedComponentValues\": [\n" +
                    "              \n" +
                    "            ],\n" +
                    "            \"FieldType\": 0,\n" +
                    "            \"XPath\": \"tcm:Content/custom:Article/custom:headline\",\n" +
                    "            \"KeywordValues\": [\n" +
                    "              \n" +
                    "            ]\n" +
                    "          },\n" +
                    "          \"articleBody\": {\n" +
                    "            \"Name\": \"articleBody\",\n" +
                    "            \"Values\": [\n" +
                    "              \n" +
                    "            ],\n" +
                    "            \"NumericValues\": [\n" +
                    "              \n" +
                    "            ],\n" +
                    "            \"DateTimeValues\": [\n" +
                    "              \n" +
                    "            ],\n" +
                    "            \"LinkedComponentValues\": [\n" +
                    "              \n" +
                    "            ],\n" +
                    "            \"EmbeddedValues\": [\n" +
                    "              {\n" +
                    "                \"subheading\": {\n" +
                    "                  \"Name\": \"subheading\",\n" +
                    "                  \"Values\": [\n" +
                    "                    \"Subheading 1\"\n" +
                    "                  ],\n" +
                    "                  \"NumericValues\": [\n" +
                    "                    \n" +
                    "                  ],\n" +
                    "                  \"DateTimeValues\": [\n" +
                    "                    \n" +
                    "                  ],\n" +
                    "                  \"LinkedComponentValues\": [\n" +
                    "                    \n" +
                    "                  ],\n" +
                    "                  \"FieldType\": 0,\n" +
                    "                  \"XPath\": \"tcm:Content/custom:Article/custom:articleBody[1]/custom:subheading\",\n" +
                    "                  \"KeywordValues\": [\n" +
                    "                    \n" +
                    "                  ]\n" +
                    "                },\n" +
                    "                \"content\": {\n" +
                    "                  \"Name\": \"content\",\n" +
                    "                  \"Values\": [\n" +
                    "                    \"<p>Test <strong>rich</strong><em>text</em> content.</p><p>Component link (not published): <a title=\\\"Test Component\\\" xlink:href=\\\"tcm:1081-9710\\\" xlink:title=\\\"Test Component\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\">Test Component</a></p><p>Component link (published): <a title=\\\"TSI-1758 Test Component\\\" xlink:href=\\\"tcm:1081-9971\\\" xlink:title=\\\"TSI-1758 Test Component\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\">TSI-1758 Test Component</a></p><p>MMC link: <a title=\\\"bulls-eye\\\" xlink:href=\\\"tcm:1081-759\\\" data-schemaUri=\\\"tcm:1081-79-8\\\" data-multimediaFileName=\\\"bulls-eye.JPG\\\" data-multimediaFileSize=\\\"1276322\\\" data-multimediaMimeType=\\\"image/jpeg\\\" xlink:title=\\\"bulls-eye\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" href=\\\"/autotest-parent-legacy/Images/bulls-eye_tcm1081-759.JPG\\\">bulls-eye</a></p><p>Embedded Image: <img title=\\\"calculator\\\" alt=\\\"calculator\\\" xlink:href=\\\"tcm:1081-760\\\" data-schemaUri=\\\"tcm:1081-79-8\\\" data-multimediaFileName=\\\"calculator.jpg\\\" data-multimediaFileSize=\\\"1007535\\\" data-multimediaMimeType=\\\"image/jpeg\\\" xlink:title=\\\"calculator\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" src=\\\"/autotest-parent-legacy/Images/calculator_tcm1081-760.jpg\\\" /></p>\"\n" +
                    "                  ],\n" +
                    "                  \"NumericValues\": [\n" +
                    "                    \n" +
                    "                  ],\n" +
                    "                  \"DateTimeValues\": [\n" +
                    "                    \n" +
                    "                  ],\n" +
                    "                  \"LinkedComponentValues\": [\n" +
                    "                    \n" +
                    "                  ],\n" +
                    "                  \"FieldType\": 2,\n" +
                    "                  \"XPath\": \"tcm:Content/custom:Article/custom:articleBody[1]/custom:content\",\n" +
                    "                  \"KeywordValues\": [\n" +
                    "                    \n" +
                    "                  ]\n" +
                    "                }\n" +
                    "              }\n" +
                    "            ],\n" +
                    "            \"EmbeddedSchema\": {\n" +
                    "              \"RootElementName\": \"Paragraph\",\n" +
                    "              \"Id\": \"tcm:1081-232-8\",\n" +
                    "              \"Title\": \"Paragraph\"\n" +
                    "            },\n" +
                    "            \"FieldType\": 4,\n" +
                    "            \"XPath\": \"tcm:Content/custom:Article/custom:articleBody\",\n" +
                    "            \"KeywordValues\": [\n" +
                    "              \n" +
                    "            ]\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"MetadataFields\": {\n" +
                    "          \n" +
                    "        },\n" +
                    "        \"ComponentType\": 1,\n" +
                    "        \"Folder\": {\n" +
                    "          \"PublicationId\": \"tcm:0-1081-1\",\n" +
                    "          \"Id\": \"tcm:1081-2574-2\",\n" +
                    "          \"Title\": \"Test\"\n" +
                    "        },\n" +
                    "        \"Categories\": [\n" +
                    "          \n" +
                    "        ],\n" +
                    "        \"Version\": 3,\n" +
                    "        \"Publication\": {\n" +
                    "          \"Id\": \"tcm:0-1081-1\",\n" +
                    "          \"Title\": \"500 Automated Test Parent (Legacy)\"\n" +
                    "        },\n" +
                    "        \"OwningPublication\": {\n" +
                    "          \"Id\": \"tcm:0-1065-1\",\n" +
                    "          \"Title\": \"401 Automated Test Parent\"\n" +
                    "        },\n" +
                    "        \"Id\": \"tcm:1081-9712\",\n" +
                    "        \"Title\": \"Test Article\"\n" +
                    "      },\n" +
                    "      \"ComponentTemplate\": {\n" +
                    "        \"OutputFormat\": \"HTML Fragment\",\n" +
                    "        \"RevisionDate\": \"2015-07-16T15:20:52.71\",\n" +
                    "        \"MetadataFields\": {\n" +
                    "          \"view\": {\n" +
                    "            \"Name\": \"view\",\n" +
                    "            \"Values\": [\n" +
                    "              \"Article\"\n" +
                    "            ],\n" +
                    "            \"NumericValues\": [\n" +
                    "              \n" +
                    "            ],\n" +
                    "            \"DateTimeValues\": [\n" +
                    "              \n" +
                    "            ],\n" +
                    "            \"LinkedComponentValues\": [\n" +
                    "              \n" +
                    "            ],\n" +
                    "            \"FieldType\": 0,\n" +
                    "            \"KeywordValues\": [\n" +
                    "              \n" +
                    "            ]\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"Id\": \"tcm:1081-83-32\",\n" +
                    "        \"Title\": \"Article\"\n" +
                    "      },\n" +
                    "      \"RenderedContent\": \"\",\n" +
                    "      \"IsDynamic\": false,\n" +
                    "      \"OrderOnPage\": 0\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"StructureGroup\": {\n" +
                    "    \"PublicationId\": \"tcm:0-1081-1\",\n" +
                    "    \"Id\": \"tcm:1081-3-4\",\n" +
                    "    \"Title\": \"Home\"\n" +
                    "  },\n" +
                    "  \"Version\": 1,\n" +
                    "  \"Publication\": {\n" +
                    "    \"Id\": \"tcm:0-1081-1\",\n" +
                    "    \"Title\": \"500 Automated Test Parent (Legacy)\"\n" +
                    "  },\n" +
                    "  \"OwningPublication\": {\n" +
                    "    \"Id\": \"tcm:0-1065-1\",\n" +
                    "    \"Title\": \"401 Automated Test Parent\"\n" +
                    "  },\n" +
                    "  \"Id\": \"tcm:1081-9786-64\",\n" +
                    "  \"Title\": \"Test Article Page\"\n" +
                    "}";

    private ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
    }

    @Test
    public void test() throws JsonProcessingException, SerializationException {
        testOriginalDd4t();

        testManuallyConstructed();
    }

    private void testManuallyConstructed() throws JsonProcessingException {
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

    private HashMap<String, Field> getMap(String key, Field value) {
        HashMap<String, Field> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private void testOriginalDd4t() throws SerializationException, JsonProcessingException {
        //given
        PageImpl originalPage = DataBindFactory.buildPage(ORIGINAL_DD4T, PageImpl.class);

        //when
        String originalSerilized = serialize(originalPage);

        //then
        assertInnerFields(originalSerilized);
    }

    private String serialize(PageImpl page) throws JsonProcessingException {
        return JsonDataBinder.getGenericMapper().writeValueAsString(page);
    }

    private void assertInnerFields(String asString) {
        assertTrue("Should contain articleBody in: " + asString, asString.contains("articleBody"));
        assertTrue("Should contain subheading in : " + asString, asString.contains("subheading"));
        assertTrue("Should contain embedded text value Subheading 1 in: " + asString, asString.contains("Subheading 1"));
    }
}