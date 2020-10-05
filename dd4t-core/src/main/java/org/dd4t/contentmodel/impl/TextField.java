/*
 * Copyright (c) 2015 SDL, Radagio & R. Oudshoorn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dd4t.contentmodel.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.dd4t.contentmodel.Field;
import org.dd4t.contentmodel.FieldType;
import org.simpleframework.xml.Attribute;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TextField extends BaseField implements Field, Serializable {

    private static final long serialVersionUID = -818637646285558123L;

    @Attribute (required = false)
    @JsonProperty ("CategoryId")
    private String categoryId;

    @Attribute (required = false)
    @JsonProperty ("CategoryName")
    private String categoryName;

    public TextField() {
        setFieldType(FieldType.TEXT);
    }

    @Override
    public List<Object> getValues() {
        List<String> textValues = getTextValues();
        List<Object> l = new LinkedList<>();

        for (String s : textValues) {
            l.add(s);
        }

        return l;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
