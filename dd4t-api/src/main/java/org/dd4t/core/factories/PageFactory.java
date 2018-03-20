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

import org.dd4t.contentmodel.Item;
import org.dd4t.contentmodel.Page;
import org.dd4t.core.exceptions.FactoryException;
import org.dd4t.core.processors.RunPhase;
import org.dd4t.core.request.RequestContext;
import org.dd4t.core.util.TCMURI;

public interface PageFactory extends Factory {

    void executeProcessors(Item item, RunPhase runPhase, RequestContext context) throws FactoryException;

    /**
     * Get a page by its URI. No security available; the method will fail if a
     * SecurityFilter is configured on the factory.
     *
     * @param uri of the page
     * @return a Page Object
     * @throws FactoryException
     */
    Page getPage(String uri) throws FactoryException;


    /**
     * Get a page by its URI. No security available; the method will fail if a
     * SecurityFilter is configured on the factory.
     *
     * @param uri     of the page
     * @param context describing the current request to be passed on to processors
     * @return a Page Object
     * @throws FactoryException
     */
    Page getPage(String uri, RequestContext context) throws FactoryException;

    /**
     * Get a Page by its URI. Deserialize into any object implementing the Page interface.
     *
     * @param uri       the page Uri
     * @param pageModel the Class to deserialize into
     * @param <T>       the Type to return
     * @return a deserialied object
     * @throws FactoryException
     */
    <T extends Page> T getPage(final String uri, final Class<T> pageModel) throws FactoryException;

    /**
     * Get a Page by its URI. Deserialize into any object implementing the Page interface.
     *
     * @param uri       the page Uri
     * @param pageModel the Class to deserialize into
     * @param <T>       the Type to return
     * @return a deserialied object
     * @throws FactoryException
     */
    <T extends Page> T getPage(final String uri, RequestContext context, final Class<T> pageModel) throws
            FactoryException;

    /**
     * Find page by its URL. The url and publication id are specified. No
     * security available; the method will fail if a SecurityFilter is
     * configured on the factory.
     *
     * @param url           of the page
     * @param publicationId of the publication
     * @return a Page Object
     * @throws org.dd4t.core.exceptions.FactoryException
     */
    Page findPageByUrl(String url, int publicationId) throws FactoryException;

    /**
     * Find page by its URL. The url and publication id are specified. No
     * security available; the method will fail if a SecurityFilter is
     * configured on the factory.
     *
     * @param url           of the page
     * @param publicationId of the publication
     * @param context       describing the current request to be passed on to processors
     * @return a Page Object
     * @throws org.dd4t.core.exceptions.FactoryException
     */
    Page findPageByUrl(String url, int publicationId, RequestContext context) throws FactoryException;

    /**
     * Find page by its URL. The url and publication id are specified. Deserialize into any object implementing the
     * Page interface.
     *
     * @return a Page Object
     * @throws org.dd4t.core.exceptions.FactoryException
     */
    <T extends Page> T findPageByUrl(String url, int publicationId, Class<T> pageModel) throws FactoryException;

    /**
     * Find page by its URL. The url and publication id are specified. Deserialize into any object implementing the
     * Page interface.
     *
     * @return a Page Object
     * @throws org.dd4t.core.exceptions.FactoryException
     */
    <T extends Page> T findPageByUrl(String url, int publicationId, RequestContext context, Class<T> pageModel)
            throws FactoryException;

    /**
     * Find the source of the Page by Url. The url and publication id are specified.
     *
     * @return The page source as string
     * @throws FactoryException
     */
    String findSourcePageByUrl(String url, int publicationId) throws FactoryException;

    /**
     * Find the source of the Page by Tcm Id.
     *
     * @param tcmId the Tcm Id of the page
     * @return The page source as String
     * @throws FactoryException
     */
    String findSourcePageByTcmId(String tcmId) throws FactoryException;

    /**
     * Find the TCM Uri of a page by URL
     *
     * @param url           the URL
     * @param publicationId the Publication Id
     * @return a TCMURI if found.
     * @throws FactoryException
     */
    TCMURI findPageIdByUrl(String url, int publicationId) throws FactoryException;

    /**
     * Deserializes a JSON encoded String into an object of the given type, which must
     * derive from the Page interface
     *
     * @param source String representing the JSON encoded object
     * @param clazz  Class representing the implementation type to deserialize into
     * @return the deserialized object
     */
    <T extends Page> T deserialize(final String source, final Class<? extends T> clazz) throws FactoryException;

    /**
     * Method to check whether a page exists in the Tridion Broker.
     *
     * @param url           the URL to check
     * @param publicationId the publication Id for the url
     * @return true if the page url exists in the broker
     */
    Boolean isPagePublished(String url, int publicationId);
}


