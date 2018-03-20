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

package org.dd4t.core.factories.impl;

import org.apache.commons.lang3.StringUtils;
import org.dd4t.caching.CacheElement;
import org.dd4t.contentmodel.Page;
import org.dd4t.contentmodel.impl.PageImpl;
import org.dd4t.core.databind.DataBinder;
import org.dd4t.core.exceptions.FactoryException;
import org.dd4t.core.exceptions.ItemNotFoundException;
import org.dd4t.core.exceptions.ProcessorException;
import org.dd4t.core.exceptions.SerializationException;
import org.dd4t.core.factories.PageFactory;
import org.dd4t.core.processors.RunPhase;
import org.dd4t.core.request.RequestContext;
import org.dd4t.core.util.TCMURI;
import org.dd4t.providers.PageProvider;
import org.dd4t.providers.PageProviderResultItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;

public class PageFactoryImpl extends BaseFactory implements PageFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PageFactoryImpl.class);

    @Resource
    protected PageProvider pageProvider;

    /**
     * @param uri of the page
     * @return the Page Object
     * @throws org.dd4t.core.exceptions.FactoryException
     */
    @Override
    public Page getPage(String uri) throws FactoryException {
        return getPage(uri, null, PageImpl.class);
    }

    /**
     * @param url           the url of the page
     * @param publicationId the publication Id
     * @return a GenericPage object
     * @throws org.dd4t.core.exceptions.FactoryException
     */
    @Override
    public Page findPageByUrl(String url, int publicationId) throws FactoryException {
        return findPageByUrl(url, publicationId, null, PageImpl.class);
    }

    private void executePostCacheProcessors(final Page page, RequestContext context) {
        if (page != null) {
            LOG.debug("Running Post caching Processors");
            try {
                this.executeProcessors(page, RunPhase.AFTER_CACHING, context);
            } catch (ProcessorException e) {
                LOG.error(e.getLocalizedMessage(), e);
            }
        }
    }


    /**
     * This method explicitly used for querying the Broker Storage
     * for pages and returns raw content as string. Does NOT trigger
     * processors.
     * <p/>
     * To handle nulls, check for a returned null in the controller.
     *
     * @param url           the url of the page
     * @param publicationId the publication Id
     * @return XML as string
     */
    @Override
    public String findSourcePageByUrl(String url, int publicationId) throws FactoryException {
        LOG.debug("Enter findXMLPageByUrl with url: {} and publicationId: {}", url, publicationId);

        String cacheKey = "PSE" + "-" + publicationId + "-" + url.toLowerCase();
        CacheElement<String> cacheElement = cacheProvider.loadPayloadFromLocalCache(cacheKey);

        String page;

        if (cacheElement.isExpired()) {
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (cacheElement) {
                if (cacheElement.isExpired()) {
                    page = pageProvider.getPageContentByURL(url, publicationId);

                    if (page == null || page.length() == 0) {
                        cacheElement.setPayload(null);
                        cacheProvider.storeInItemCache(cacheKey, cacheElement);
                        cacheElement.setExpired(true);
                        throw new ItemNotFoundException("XML Page with url: " + url + " not found.");
                    }

                    cacheElement.setPayload(page);
                    cacheProvider.storeInItemCache(cacheKey, cacheElement);
                    cacheElement.setExpired(false);

                    LOG.debug("Added XML page with uri: {} and publicationId: {} to cache", url, publicationId);
                } else {
                    LOG.debug("Return a XML page with url: {} and publicationId: {} from cache", url, publicationId);
                    page = cacheElement.getPayload();
                }
            }
        } else {
            LOG.debug("Return XML page with url: {} and publicationId: {} from cache", url, publicationId);
            page = cacheElement.getPayload();
        }

        if (page == null) {
            throw new ItemNotFoundException("Page with url: " + url + " not found.");
        }
        return page;
    }

    /**
     * Find the source of the Page by Tcm Id.
     *
     * @param tcmId the Tcm Id of the page
     * @return The page source as String
     * @throws FactoryException
     */
    @Override
    public String findSourcePageByTcmId(final String tcmId) throws FactoryException {

        LOG.debug("Enter findSourcePageByTcmId with uri: {}", tcmId);

        String cacheKey = "PSE-" + tcmId;

        CacheElement<String> cacheElement = cacheProvider.loadPayloadFromLocalCache(cacheKey);
        String pageSource;

        if (cacheElement.isExpired()) {
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (cacheElement) {
                if (cacheElement.isExpired()) {

                    try {
                        pageSource = pageProvider.getPageContentById(tcmId);
                    } catch (ParseException e) {
                        LOG.error(e.getLocalizedMessage(), e);
                        throw new SerializationException(e);
                    }

                    if (StringUtils.isEmpty(pageSource)) {
                        cacheElement.setPayload(null);
                        cacheProvider.storeInItemCache(cacheKey, cacheElement);
                        cacheElement.setExpired(true);
                        throw new ItemNotFoundException("Unable to find page by id " + tcmId);
                    }

                    cacheElement.setPayload(pageSource);
                    cacheProvider.storeInItemCache(cacheKey, cacheElement);
                    cacheElement.setExpired(false);
                } else {
                    LOG.debug("Return a page with uri: {} from cache", tcmId);
                    pageSource = cacheElement.getPayload();
                }
            }
        } else {
            LOG.debug("Return page with uri: {} from cache", tcmId);
            pageSource = cacheElement.getPayload();
        }

        return pageSource;
    }

    /**
     * Find the TCM Uri of a page by URL
     *
     * @param url           the URL
     * @param publicationId the Publication Id
     * @return a TCMURI if found.
     * @throws FactoryException
     */
    @Override
    public TCMURI findPageIdByUrl(final String url, final int publicationId) throws FactoryException {
        return pageProvider.getPageIdForUrl(url, publicationId);
    }

    /**
     * Method to check whether a page exists in the Tridion Broker.
     *
     * @param url           the Url to check
     * @param publicationId the publication Id
     * @return boolean indicating the page is present
     */
    @Override
    public Boolean isPagePublished(String url, int publicationId) {
        LOG.debug("Enter isPagePublished with url: {} and publicationId: {}", url, publicationId);
        try {
            return pageProvider.checkPageExists(url, publicationId);
        } catch (ItemNotFoundException | SerializationException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public <T extends Page> T getPage(final String uri, final Class<T> pageModel) throws FactoryException {
        if (!pageModel.getClass().isInstance(Page.class)) {
            throw new SerializationException("Given model class does not implement the Page interface");
        }

        return getPage(uri, null, pageModel);
    }

    @Override
    public Page getPage(String uri, RequestContext context) throws FactoryException {
        return getPage(uri, context, PageImpl.class);
    }

    @Override
    public <T extends Page> T getPage(String uri, RequestContext context, Class<T> pageModel) throws FactoryException {

        if (!pageModel.getClass().isInstance(Page.class)) {
            throw new SerializationException("Given model class does not implement the Page interface");
        }

        LOG.debug("Enter getPage with uri: {}", uri);

        CacheElement<Page> cacheElement = cacheProvider.loadPayloadFromLocalCache(uri);
        Page page;

        if (cacheElement.isExpired()) {
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (cacheElement) {
                if (cacheElement.isExpired()) {
                    String pageSource;
                    PageProviderResultItem<String> resultItem;
                    TCMURI tcmUri;
                    try {
                        tcmUri = new TCMURI(uri);
                        resultItem = pageProvider.getPageById(tcmUri.getItemId(), tcmUri.getPublicationId());
                        pageSource = resultItem.getSourceContent();

                    } catch (ParseException | IOException e) {
                        LOG.error(e.getLocalizedMessage(), e);
                        throw new SerializationException(e);
                    }

                    if (StringUtils.isEmpty(pageSource)) {
                        cacheElement.setPayload(null);
                        cacheElement.setNull(true);
                        cacheProvider.storeInItemCache(uri, cacheElement);
                        throw new ItemNotFoundException("Unable to find page by id " + uri);
                    }

                    page = producePage(resultItem, context, PageImpl.class);

                    cacheElement.setPayload(page);

                    cacheProvider.storeInItemCache(uri, cacheElement, tcmUri.getPublicationId(), tcmUri.getItemId());
                    cacheElement.setExpired(false);
                    LOG.debug("Added page with uri: {} to cache", uri);

                } else {
                    LOG.debug("Return a page with uri: {} from cache", uri);
                    page = cacheElement.getPayload();
                }
            }
        } else {
            LOG.debug("Return page with uri: {} from cache", uri);
            page = cacheElement.getPayload();
        }

        if (page == null) {
            throw new ItemNotFoundException("Found nullreference for page in cache. Please try again later.");
        }

        executePostCacheProcessors(page, context);
        return (T) page;
    }

    @Override
    public Page findPageByUrl(final String url, final int publicationId, final RequestContext context) throws
            FactoryException {
        return findPageByUrl(url, publicationId, context, PageImpl.class);
    }

    @Override
    public <T extends Page> T findPageByUrl(final String url, final int publicationId, final Class<T> pageModel)
            throws FactoryException {
        return findPageByUrl(url, publicationId, null, pageModel);
    }

    @Override
    public <T extends Page> T findPageByUrl(String url, int publicationId, RequestContext context, final Class<T>
            pageModel) throws FactoryException {
        LOG.debug("Enter findPageByUrl with url: {} and publicationId: {}", url, publicationId);

        String cacheKey = publicationId + "-" + url.toLowerCase();
        CacheElement<Page> cacheElement = cacheProvider.loadPayloadFromLocalCache(cacheKey);
        Page page;

        if (cacheElement.isExpired() || cacheElement.getPayload() == null) {
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (cacheElement) {
                if (cacheElement.isExpired() || cacheElement.getPayload() == null) {
                    PageProviderResultItem<String> resultItem;
                    resultItem = pageProvider.getPageByURL(url, publicationId);
                    String pageSource = resultItem.getSourceContent();

                    if (StringUtils.isEmpty(pageSource)) {
                        cacheElement.setPayload(null);
                        cacheProvider.storeInItemCache(cacheKey, cacheElement);
                        cacheElement.setExpired(true);
                        throw new ItemNotFoundException("Page with url: " + url + " not found.");
                    }

                    page = producePage(resultItem, context, PageImpl.class);

                    cacheElement.setPayload(page);
                    cacheProvider.storeInItemCache(cacheKey, cacheElement, resultItem.getPublicationId(), resultItem
                            .getItemId());
                    cacheElement.setExpired(false);


                    LOG.debug("Added page with uri: {} and publicationId: {} to cache", url, publicationId);
                } else {
                    LOG.debug("Return a page with url: {} and publicationId: {} from cache", url, publicationId);
                    page = cacheElement.getPayload();
                }
            }
        } else {
            LOG.debug("Return page with url: {} and publicationId: {} from cache", url, publicationId);
            page = cacheElement.getPayload();
        }
        executePostCacheProcessors(page, context);
        return (T) page;
    }

    private <T extends Page> T producePage(PageProviderResultItem<String> resultItem, RequestContext context,
                                           Class<T> pageClass) throws FactoryException {

        String pageSource = resultItem.getSourceContent();
        Page page = deserialize(pageSource, pageClass);
        page.setLastPublishedDate(resultItem.getLastPublishDate());
        page.setRevisionDate(resultItem.getRevisionDate());
        page.setUrl(resultItem.getUrl());

        LOG.debug("Running pre caching processors");
        this.executeProcessors(page, RunPhase.BEFORE_CACHING, context);

        return (T) page;
    }

    /**
     * Deserializes a JSON encoded String into an object of the given type, which must
     * derive from the Page interface
     *
     * @param source String representing the JSON encoded object
     * @param clazz  Class representing the implementation type to deserialize into
     * @return the deserialized object
     */
    @Override
    public <T extends Page> T deserialize(final String source, final Class<? extends T> clazz) throws FactoryException {
        DataBinder binder = selectDataBinder(source);

        if (binder == null) {
            throw new SerializationException("Unable to select databinder.");
        }

        return binder.buildPage(source, clazz);
    }

    public void setPageProvider(PageProvider provider) {
        this.pageProvider = provider;
    }
}