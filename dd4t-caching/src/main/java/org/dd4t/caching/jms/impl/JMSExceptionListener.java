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

package org.dd4t.caching.jms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

/**
 * @author Mihai Cadariu
 */
public class JMSExceptionListener implements ExceptionListener {

    private static final Logger LOG = LoggerFactory.getLogger(JMSExceptionListener.class);

    @Resource
    private JMSCacheMonitor monitor;

    @Override
    public void onException(JMSException jmse) {
        LOG.error("JMS exception occurred", jmse);
        monitor.setMQServerStatusDown();
    }

    public void setMonitor(JMSCacheMonitor monitor) {
        this.monitor = monitor;
    }
}
