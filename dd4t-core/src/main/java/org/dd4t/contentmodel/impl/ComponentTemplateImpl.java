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
import org.dd4t.contentmodel.ComponentTemplate;
import org.simpleframework.xml.Element;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentTemplateImpl extends BaseRepositoryLocalItem implements ComponentTemplate, Serializable {

    private static final long serialVersionUID = -5298380361943500591L;

    @Element (name = "outputFormat", required = false)
    @JsonProperty ("OutputFormat")
    private String outputFormat;

    /**
     * Get the output format
     *
     * @return
     */
    public String getOutputFormat() {
        return outputFormat;
    }

    /**
     * Set the output format
     *
     * @param outputFormat
     */
    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }
}
