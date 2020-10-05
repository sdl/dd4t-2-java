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

package org.dd4t.contentmodel.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.dd4t.contentmodel.BinaryData;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BinaryDataImpl implements BinaryData, Serializable {

    private static final long serialVersionUID = 1267561342466889726L;

    private byte[] bytes;

    @Override
    public byte[] getBytes() {
        return this.bytes.clone();
    }

    public void setBytes(final byte[] bytes) {
        this.bytes = bytes.clone();
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public long getDataSize() {
        return bytes == null ? 0 : bytes.length;
    }
}
