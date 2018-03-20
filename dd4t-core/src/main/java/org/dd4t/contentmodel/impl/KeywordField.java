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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dd4t.contentmodel.Field;
import org.dd4t.contentmodel.FieldType;
import org.dd4t.contentmodel.Keyword;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class KeywordField extends BaseField implements Field, Serializable {

    private static final long serialVersionUID = 5239810268909419707L;

    @JsonProperty ("CategoryName")
    private String categoryName;

    @JsonProperty ("CategoryId")
    private String categoryId;

    public KeywordField() {
        setFieldType(FieldType.KEYWORD);
    }

    @Override
    public List<Object> getValues() {
        List<Keyword> keywordValues = getKeywordValues();
        List<Object> l = new LinkedList<>();

        for (Keyword k : keywordValues) {
            l.add(k);
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