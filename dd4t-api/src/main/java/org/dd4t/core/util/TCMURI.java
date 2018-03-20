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

package org.dd4t.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.dd4t.core.util.TCMURI.Namespace.ISH;
import static org.dd4t.core.util.TCMURI.Namespace.TCM;
import static org.dd4t.core.util.TCMURI.Namespace.getNamespaceFor;

public class TCMURI implements Serializable {

    public static final String URI_NAMESPACE = "tcm:";
    protected static final String SEPARATOR = "-";

    protected int itemType;
    protected int itemId;
    protected int pubId;
    protected int version;
    private Namespace namespace;

    public TCMURI(String uri) throws ParseException {
        this(new Builder(uri));
    }

    public TCMURI(String uri, int version) throws ParseException {
        this(new Builder(uri).version(version));
    }

    public TCMURI(int publicationId, int itemId, int itemType) {
        this(new Builder(publicationId, itemId, itemType));
    }

    public TCMURI(int publicationId, int itemId, int itemType, int version) {
        this(new Builder(publicationId, itemId, itemType).version(version));
    }

    public TCMURI(Namespace namespace, int publicationId, int itemId, int itemType, int version) {
        this(new Builder(publicationId, itemId, itemType).version(version).namespace(namespace));
    }

    private TCMURI(Builder builder) {
        this.itemType = builder.itemType;
        this.itemId = builder.itemId;
        this.pubId = builder.pubId;
        this.version = builder.version;
        this.namespace = builder.namespace;
    }

    public static boolean isValid(String tcmUri) {
        return tcmUri != null && getNamespaceFor(tcmUri.split(":")[0]) != null;
    }

    protected void load(String uriString) throws ParseException {
        Builder builder = new Builder(uriString);
        this.itemType = builder.itemType;
        this.itemId = builder.itemId;
        this.pubId = builder.pubId;
        this.version = builder.version;
        this.namespace = builder.namespace;
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    @Override
    public String toString() {
        return namespace.getValue() + ":" + this.pubId + SEPARATOR + this.itemId + SEPARATOR + this.itemType;
    }

    public int getItemType() {
        return this.itemType;
    }

    public int getItemId() {
        return this.itemId;
    }

    public int getPublicationId() {
        return this.pubId;
    }

    public int getVersion() {
        return this.version;
    }

    public static class Builder {
        private static final Pattern PATTERN = Pattern.compile("^(?<namespace>(tcm|ish){1}):(?<pubId>\\d+)-" +
                "(?<itemId>\\d+)(-(?<itemType>\\d+))?(-v(?<version>\\d+))?$");
        private static final Logger LOG = LoggerFactory.getLogger(Builder.class);

        private int pubId;
        private int itemId;
        private int itemType = 16;
        private int version = -1;
        private Namespace namespace = TCM;

        /**
         * Defaults to ItemType component
         *
         * @param pubId  the publication Id
         * @param itemId the item Id
         */
        public Builder(int pubId, int itemId) {
            this.pubId = pubId;
            this.itemId = itemId;
        }

        public Builder(int pubId, int itemId, int itemType) {
            this.pubId = pubId;
            this.itemId = itemId;
            this.itemType = itemType;
        }

        public Builder(String uri) throws ParseException {
            try {
                validatePatternOf(uri);
                extractItemsFrom(uri);
            } catch (IllegalArgumentException iae) {
                LOG.trace(iae.getLocalizedMessage(), iae);
                throw new ParseException(iae.getMessage(), 0);
            }
        }

        public Builder itemType(int itemType) {
            this.itemType = itemType;
            return this;
        }

        public Builder version(int version) {
            this.version = version;
            return this;
        }

        public Builder namespace(Namespace namespace) {
            this.namespace = namespace;
            return this;
        }

        private static void validatePatternOf(String uri) {
            if (uri == null) {
                throw new IllegalArgumentException("Invalid TCMURI String, string cannot be null");
            }

            if (!uri.startsWith(TCM.getValue()) && !uri.startsWith(ISH.getValue())) {
                throw new IllegalArgumentException(String.format("URI string %s does not start with %s or %s", uri,
                        TCM.getValue(), ISH.getValue()));
            }
        }

        private void extractItemsFrom(String uri) {
            Matcher m = PATTERN.matcher(uri);

            if (!m.find()) {
                throw new IllegalArgumentException(String.format("URI %s does not match the pattern", uri));
            }

            this.namespace = Namespace.getNamespaceFor(m.group("namespace"));
            this.pubId = Integer.parseInt(m.group("pubId"));
            this.itemId = Integer.parseInt(m.group("itemId"));

            if (m.group("itemType") != null) {
                this.itemType = Integer.parseInt(m.group("itemType"));
            }

            if (m.group("version") != null) {
                this.version = Integer.parseInt(m.group("version"));
            }
        }

        public TCMURI create() {
            return new TCMURI(this);
        }
    }

    public enum Namespace {
        TCM("tcm"),
        ISH("ish");

        private String value;

        private static final Map<String, Namespace> namespaces = Collections.unmodifiableMap(initialize());

        private static Map<String, Namespace> initialize() {
            Map<String, Namespace> result = new HashMap<>();
            for (Namespace n : Namespace.values()) {
                result.put(n.getValue(), n);
            }
            return result;
        }

        Namespace(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Namespace getNamespaceFor(String value) {
            return namespaces.get(value);
        }
    }
}
