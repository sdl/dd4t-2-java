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

package org.dd4t.core.factories;

import org.dd4t.contentmodel.ComponentPresentation;
import org.dd4t.contentmodel.Item;
import org.dd4t.core.exceptions.FactoryException;
import org.dd4t.core.processors.RunPhase;
import org.dd4t.core.request.RequestContext;

public interface ComponentPresentationFactory extends Factory {

    void executeProcessors(Item item, RunPhase runPhase, RequestContext context) throws FactoryException;

    /**
     * Get a component by its uri and Component Template URI.
     *
     * @param componentURI String representing the Component TCMURI to retrieve
     * @return a Generic Component object
     * @throws org.dd4t.core.exceptions.FactoryException
     */
    ComponentPresentation getComponentPresentation(String componentURI) throws FactoryException;

    /**
     * Get a component by its uri and Component Template URI.
     *
     * @param componentURI      String representing the Component TCMURI to retrieve
     * @param viewOrTemplateURI String representing either the View Name or Component Template TCMURI
     *                          to use when looking up the DCP
     * @return a Generic Component object
     * @throws org.dd4t.core.exceptions.FactoryException
     */
    ComponentPresentation getComponentPresentation(String componentURI, String viewOrTemplateURI) throws
            FactoryException;


    /**
     * Get a component by its uri and Component Template URI.
     *
     * @param componentURI String representing the Component TCMURI to retrieve
     * @param context      RequestContext to be passed to the processors
     * @return a Generic Component object
     * @throws org.dd4t.core.exceptions.FactoryException
     */
    ComponentPresentation getComponentPresentation(String componentURI, RequestContext context) throws FactoryException;

    /**
     * Get a component by its uri and Component Template URI.
     *
     * @param componentURI      String representing the Component TCMURI to retrieve
     * @param viewOrTemplateURI String representing either the View Name or Component Template TCMURI
     *                          to use when looking up the DCP
     * @param context           RequestContext to be passed to the processors
     * @return a Generic Component object
     * @throws org.dd4t.core.exceptions.FactoryException
     */
    ComponentPresentation getComponentPresentation(String componentURI, String viewOrTemplateURI, RequestContext
            context) throws FactoryException;
}
