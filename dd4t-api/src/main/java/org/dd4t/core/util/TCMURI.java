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

import java.io.Serializable;
import java.text.ParseException;
import java.util.StringTokenizer;

import static org.dd4t.core.util.TCMURI.Namespace.ISH;
import static org.dd4t.core.util.TCMURI.Namespace.TCM;

public class TCMURI implements Serializable {

    // Note: this variable left for possible backward compatibility
    public static final String URI_NAMESPACE = "tcm:";
    protected static final String COLON = ":";
    protected static final String SEPARATOR = "-";
    protected static final String DELIM_VERSION = "v";

    private int itemType;
    private int itemId;
    private int pubId;
    private int version;
    private Namespace namespace = TCM;

    public TCMURI() {
    }

    public TCMURI(String uri) throws ParseException {
        this.itemType = 0;
        this.itemId = -1;
        this.pubId = -1;
        this.version = -1;
        load(uri);
    }

    public TCMURI(int publicationId, int itemId, int itemType, int version) {
        this.itemType = itemType;
        this.itemId = itemId;
        this.pubId = publicationId;
        this.version = version;
    }

    public TCMURI(Namespace namespace, int publicationId, int itemId, int itemType, int version) {
        this(publicationId, itemId, itemType, version);
        this.namespace = namespace;
    }

    public static boolean isValid(String tcmUri) {
        return tcmUri != null && startsWithNamespace(tcmUri);
    }

    protected void load(String uriString) throws ParseException {
        if (uriString != null) {
            int colonCharAt = uriString.indexOf(COLON);
            int currentPosition = uriString.length();
            if ((colonCharAt < 0) || !startsWithNamespace(uriString)) {
                throw new ParseException("URI string " + uriString + " does not start with '" + TCM.getValue() + "' or '"
                        + ISH.getValue() + "'", currentPosition);
            }

            this.namespace = Namespace.valueOf(uriString.substring(0, colonCharAt).toUpperCase());
            String uri = uriString.substring(colonCharAt + 1);
            StringTokenizer st = new StringTokenizer(uri, "-");
            if (st.countTokens() < 2) {
                throw new ParseException("URI string " + uriString + " does not contain enough ID's", currentPosition);
            }
            try {
                String token = st.nextToken();
                currentPosition += token.length();
                this.pubId = Integer.parseInt(token);

                token = st.nextToken();
                currentPosition += token.length();
                this.itemId = Integer.parseInt(token);

                if (!st.hasMoreTokens()) {
                    this.itemType = 16;
                } else {
                    token = st.nextToken();
                    currentPosition += token.length();
                    if (!token.startsWith(DELIM_VERSION)) {
                        this.itemType = Integer.parseInt(token);
                    } else {
                        this.version = Integer.parseInt(token.substring(1, token.length()));
                        this.itemType = 16;
                    }

                    if (st.hasMoreTokens()) {
                        token = st.nextToken();
                        currentPosition += token.length();
                        this.version = Integer.parseInt(token.substring(1, token.length()));
                    }
                }
            } catch (NumberFormatException e) {
                throw new ParseException("Invalid ID (not an integer) in URI string " + uriString, currentPosition);
            }
        } else {
            throw new ParseException("Invalid TCMURI String, string cannot be null", 0);
        }
    }

    private static boolean startsWithNamespace(String uriString) {
        for (Namespace namespace : Namespace.values()) {
            if (uriString.startsWith(namespace.getValue())) {
                return true;
            }
        }
        return false;
    }


    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(namespace.getValue());
        sb.append(COLON);
        sb.append(this.pubId);
        sb.append(SEPARATOR);
        sb.append(this.itemId);
        sb.append(SEPARATOR);
        sb.append(this.itemType);
        return sb.toString();
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

    public Namespace getUriNamespace() {
        return this.namespace;
    }

    public enum Namespace {
        TCM("tcm"),
        ISH("ish");

        String value;

        Namespace(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}