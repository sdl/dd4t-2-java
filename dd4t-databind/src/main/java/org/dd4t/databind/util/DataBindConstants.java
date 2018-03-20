/*
 * Copyright (c) 2015 Radagio
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

package org.dd4t.databind.util;

import org.dd4t.core.databind.BaseViewModel;
import org.dd4t.core.databind.TridionViewModel;
import org.dd4t.databind.annotations.ViewModel;
import org.dd4t.databind.viewmodel.base.TridionViewModelBase;
import org.dd4t.databind.viewmodel.base.ViewModelBase;

/**
 * test
 *
 * @author R. Kempees
 * @since 01/12/14.
 */
public class DataBindConstants {
    public static final String VIEW_MODEL_DEFAULT_NAMESPACE = "org.dd4t.web.models";
    public static final String VIEW_MODEL_DEFAULT_META_KEY = "view";

    public static final String SCHEMA_NODE_NAME = "Schema";
    public static final String COMPONENT_NODE_NAME = "Component";
    public static final String IS_DYNAMIC_NODE = "IsDynamic";
    public static final String COMPONENT_TEMPLATE_NODE_NAME = "ComponentTemplate";
    public static final String ORDER_ON_PAGE_NODE = "OrderOnPage";
    public static final String RENDERED_CONTENT_NODE = "RenderedContent";
    public static final String EXTENSION_DATA_NODE = "ExtensionData";
    public static final String ROOT_ELEMENT_NAME = "RootElementName";
    public static final String LAST_PUBLISHED_DATE = "LastPublishedDate";
    public static final String LAST_MODIFIED_DATE = "RevisionDate";
    public static final String ID = "Id";
    public static final String MESSAGE_ERROR_DESERIALIZING = "Error deserializing.";
    public static final String TRUE_STRING = "true";
    public static final String FALSE_STRING = "false";
    public static final String COMPONENT_FIELDS = "Fields";
    public static final String METADATA_FIELDS = "MetadataFields";
    public static final String VALUES_NODE = "Values";
    public static final String EMBEDDED_VALUES_NODE = "EmbeddedValues";
    public static final String LINKED_COMPONENT_VALUES_NODE = "LinkedComponentValues";
    public static final String FIELD_TYPE_KEY = "FieldType";
    public static final String COMPONENT_TYPE = "ComponentType";
    public static final String MULTIMEDIA = "Multimedia";
    public static final String XPATH = "XPath";
    public static final String URL = "Url";
    public static final String VIEW_MODEL_ANNOTATION_NAME = ViewModel.class.getCanonicalName();
    public static final String TRIDION_VIEW_MODEL_BASE_CLASS_NAME = TridionViewModelBase.class.getCanonicalName();
    public static final String VIEW_MODEL_BASE_CLASS_NAME = ViewModelBase.class.getCanonicalName();
    public static final String TRIDION_VIEW_MODEL_INTERFACE = TridionViewModel.class.getCanonicalName();
    public static final String BASE_VIEW_MODEL_INTERFACE = BaseViewModel.class.getCanonicalName();
    public static final String EMBEDDED_SCHEMA_FIELD_NAME = "EmbeddedSchema";
    public static final String VIEW_MODEL_ANNOTATION = "org.dd4t.databind.annotations.ViewModel";

    private DataBindConstants() {

    }
}
