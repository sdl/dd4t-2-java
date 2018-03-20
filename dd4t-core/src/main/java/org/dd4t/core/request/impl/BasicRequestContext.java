/**
 * Copyright 2011 Capgemini & SDL
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dd4t.core.request.impl;

import org.dd4t.core.request.RequestContext;
import org.dd4t.core.util.HttpUtils;

import javax.servlet.http.HttpServletRequest;


/**
 * Simple implementation of the RequestContext interface.
 *
 * @author rooudsho
 */
@Deprecated
public class BasicRequestContext implements RequestContext {
    private HttpServletRequest req;

    public BasicRequestContext() {
        this.req = HttpUtils.getCurrentRequest();
    }

    public BasicRequestContext(HttpServletRequest req) {
        this.req = req;
    }

    @Override
    public HttpServletRequest getServletRequest() {
        return req;
    }

    public boolean isUserInRole(String role) {
        return req.isUserInRole(role);
    }
}
