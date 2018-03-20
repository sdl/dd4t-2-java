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

package org.dd4t.databind.annotations;

import org.dd4t.contentmodel.FieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author R. Kempees
 */
@Target (ElementType.FIELD)
@Retention (RetentionPolicy.RUNTIME)
public @interface ViewModelProperty {
    /*
    Entity field name in essence is the Tridion component field name
     */
    String entityFieldName() default "";

    FieldType tridionFieldType() default FieldType.TEXT;

    boolean isMetadata() default false;

    boolean resolveLinkForComponentLinkField() default false;

    boolean isComponentLinkUrl() default false;
}
