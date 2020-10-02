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

package org.dd4t.core.processors.impl;

import org.dd4t.contentmodel.Component;
import org.dd4t.contentmodel.ComponentPresentation;
import org.dd4t.contentmodel.Field;
import org.dd4t.contentmodel.FieldSet;
import org.dd4t.contentmodel.Item;
import org.dd4t.contentmodel.Page;
import org.dd4t.contentmodel.impl.ComponentLinkField;
import org.dd4t.contentmodel.impl.EmbeddedField;
import org.dd4t.contentmodel.impl.XhtmlField;
import org.dd4t.core.exceptions.ItemNotFoundException;
import org.dd4t.core.exceptions.ProcessorException;
import org.dd4t.core.exceptions.SerializationException;
import org.dd4t.core.processors.LinkResolverProcessor;
import org.dd4t.core.request.RequestContext;
import org.dd4t.core.resolvers.LinkResolver;
import org.dd4t.core.util.TridionUtils;
import org.dd4t.core.util.XSLTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter to resolve component links.
 *
 * @author bjornl
 */
public class DefaultLinkResolver extends BaseProcessor implements LinkResolverProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultLinkResolver.class);
	// TODO: remove
    private static final boolean USE_XSLT = true;
    private final XSLTransformer xslTransformer = XSLTransformer.getInstance();
    private final Pattern xsltPattern = Pattern.compile("</?ddtmproot>");
    private final Pattern regExpPattern = Pattern.compile("href=\"" + TridionUtils.TCM_REGEX + "\"");

    @Resource
    private LinkResolver linkResolver;
    private String contextPath;
    private Map<String, Object> params = new HashMap<>();

    /**
     * Recursively resolves all components links.
     *
     * @param item    the to resolve the links
     */
    @Override
    public void execute(Item item, RequestContext context) throws ProcessorException {
        linkResolver.setContextPath(contextPath);

        if (item instanceof Page) {
            try {
                resolvePage((Page) item);
            } catch (TransformerException e) {
                LOG.error("Could not resolve page " + item, e);
                throw new ProcessorException(e);
            }
        } else if (item instanceof Component) {
            try {
                resolveComponent((Component) item);
            } catch (TransformerException e) {
                LOG.error("Could not resolve component " + item, e);
                throw new ProcessorException(e);
            }
        } else {
            LOG.debug("DefaultLinkResolverFilter. Item is not a GenericPage or GenericComponent so no component to resolve");
        }
    }

    protected void resolvePage(Page page) throws TransformerException {
        List<ComponentPresentation> cpList = page.getComponentPresentations();
        if (cpList != null) {
            for (ComponentPresentation cp : cpList) {
                resolveComponentOnPage(cp.getComponent());
            }
        }
        resolveMap(page.getMetadata());
    }

    protected void resolveComponentOnPage (Component component) throws TransformerException {
        if (component != null) {
            // resolve regular content
            resolveMap(component.getContent());
            // resolve metadata
            resolveMap(component.getMetadata());
        }
    }

    protected void resolveComponent(Component component) throws TransformerException {
        try {
            if (component != null) {
                resolveMap(component.getContent());
                resolveMap(component.getMetadata());
                linkResolver.resolve(component);
            }
        } catch (Exception e) {
            throw new TransformerException("Could not resolve component " + component, e);
        }
    }

    protected void resolveMap(Map<String, Field> fieldMap) throws TransformerException {
        if (fieldMap != null && !fieldMap.isEmpty()) {
            Collection<Field> values = fieldMap.values();
            for (Field field : values) {
                if (field instanceof ComponentLinkField) {
                    resolveComponentLinkField((ComponentLinkField) field);
                } else if (field instanceof EmbeddedField) {
                    resolveList(((EmbeddedField) field).getEmbeddedValues());
                } else if (field instanceof XhtmlField) {
                    resolveXhtmlField((XhtmlField) field);
                }
            }
        }
    }

    protected void resolveList(List<FieldSet> fslist) throws TransformerException {
        if (fslist != null && !fslist.isEmpty()) {
            for (FieldSet fs : fslist) {
                resolveMap(fs.getContent());
            }
        }
    }

    protected void resolveComponentLinkField(ComponentLinkField componentLinkField) throws TransformerException {
        List<Object> compList = componentLinkField.getValues();

        for (Object component : compList) {
            resolveComponent((Component) component);
        }
    }

    protected void resolveXhtmlField(XhtmlField xhtmlField) throws TransformerException {
        List<Object> xhtmlValues = xhtmlField.getValues();
        List<String> newValues = new ArrayList<>();

        if (USE_XSLT) {
            // find all component links and try to resolve them
            for (Object xhtmlValue : xhtmlValues) {
                String result = xslTransformer.transformSourceFromFilesource("<ddtmproot>" + xhtmlValue + "</ddtmproot>", "/resolveXhtmlWithLinks.xslt", params);
                newValues.add(xsltPattern.matcher(result).replaceAll(""));
            }
        } else {
            try {
                // find all component links and try to resolve them
                for (Object xhtmlValue : xhtmlValues) {

                    Matcher m = regExpPattern.matcher((String) xhtmlValue);

                    StringBuffer sb = new StringBuffer();
                    String resolvedLink = null;
                    while (m.find()) {
                        resolvedLink = linkResolver.resolve(m.group(1));
                        // if not possible to resolve the link do nothing
                        if (resolvedLink != null) {
                            //@ToDo consider to replace with Matcher.quoteReplacement(resolvedLink)
                            m.appendReplacement(sb, "href=\"" + resolvedLink + "\"");
                        }
                    }
                    m.appendTail(sb);
                    newValues.add(sb.toString());
                }
            } catch (Exception e) {
                throw new TransformerException("Could not resolve field "+ xhtmlField, e);
            }
        }

        xhtmlField.setTextValues(newValues);
    }

    public LinkResolver getLinkResolver() {
        return linkResolver;
    }

    @Override
    public String getContextPath() {
        return this.contextPath;
    }

    @Override
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
        this.params = new HashMap<>();
        this.params.put("contextpath", contextPath);
    }
}