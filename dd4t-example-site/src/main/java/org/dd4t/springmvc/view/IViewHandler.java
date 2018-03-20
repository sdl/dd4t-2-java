/*
 * Copyright (c) 2015 R. Oudshoorn
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
package org.dd4t.springmvc.view;

import org.dd4t.contentmodel.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * ViewHandler interface; any implementing class will be able to: - Provide a
 * list of views it can handle - Actually process the views into a return string
 * which should contain the HTML
 *
 * @param <T>
 * @author rooudsho
 */
public interface IViewHandler<T> {
    Collection<String> provideViews();

    String handleView(Page pagemodel, T itemmodel, String ViewID, HttpServletRequest req, HttpServletResponse res)
            throws Exception;

    boolean canHandleView(String view, HttpServletRequest req, HttpServletResponse res);
}