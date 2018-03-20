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

import org.dd4t.contentmodel.ComponentPresentation;
import org.dd4t.contentmodel.Item;
import org.dd4t.contentmodel.Page;
import org.dd4t.core.exceptions.FactoryException;
import org.dd4t.core.factories.ComponentPresentationFactory;
import org.dd4t.core.processors.Processor;
import org.dd4t.core.request.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Pagefactory processor intended to resolve DCP's on pages at the factory level. It checks the page
 * being produced, finds the dynamic components (if any), and resolves these components through
 * the ComponentFactory.
 *
 * @author Rogier Oudshoorn, Raimond Kempees
 */
public class DcpResolver extends BaseProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(DcpResolver.class);

    @Resource
    protected ComponentPresentationFactory componentPresentationFactory;

    @Override
    public void execute(Item item, RequestContext context) {
        LOG.debug("Processing item: {} ", item);
        if (item instanceof Page) {
            final Page page = (Page) item;

            LOG.debug("DCP Resolver detected {} component presentations.", page.getComponentPresentations().size());

            for (ComponentPresentation cp : page.getComponentPresentations()) {
                if (cp.isDynamic()) {
                    LOG.debug("Detected dynamic component presentation " + cp);

                    try {
                        final ComponentPresentation componentPresentation = componentPresentationFactory
                                .getComponentPresentation(cp.getComponent().getId(), cp.getComponentTemplate().getId());
                        cp.setComponent(componentPresentation.getComponent());
                        cp.setViewModel(componentPresentation.getAllViewModels());
                    } catch (FactoryException e) {
                        LOG.error("Unable to find dynamic component by id " + cp.getComponent().getId(), e);
                    }
                }
            }
        }
    }

    public ComponentPresentationFactory getComponentPresentationFactory() {
        return componentPresentationFactory;
    }

    public void setComponentPresentationFactory(ComponentPresentationFactory componentPresentationFactory) {
        this.componentPresentationFactory = componentPresentationFactory;
    }
}
