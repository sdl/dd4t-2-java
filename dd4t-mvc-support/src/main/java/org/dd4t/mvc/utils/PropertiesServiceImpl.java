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

package org.dd4t.mvc.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * dd4t-2
 *
 * @author M.Cadariu
 */
public class PropertiesServiceImpl extends PropertiesServiceBase {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesServiceImpl.class);

    public PropertiesServiceImpl() {
    }

    @Override
    public void load(String propertiesFile) {
        LOG.debug("Loading file " + propertiesFile);
        InputStream input = null;
        try {
            input = PropertiesServiceImpl.class.getClassLoader().getResourceAsStream(propertiesFile);
            if (input == null) {
                throw new IOException("Cannot find properties file '" + propertiesFile + "' in classpath");
            }

            properties = new Properties();
            properties.load(input);
        } catch (IOException ioe) {
            LOG.error("Failed to load properties file " + propertiesFile, ioe);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Required
    public void setLocation(String location) {
        LOG.debug("Load Properties from: {}", location);
        load(location);
    }
}
