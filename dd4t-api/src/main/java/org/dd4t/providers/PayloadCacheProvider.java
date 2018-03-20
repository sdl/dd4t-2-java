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

package org.dd4t.providers;

import org.dd4t.caching.CacheDependency;
import org.dd4t.caching.CacheElement;

import java.util.List;

public interface PayloadCacheProvider {

    /**
     * Loads object corresponding to the given key from cache. Method always returns a non-null CacheElement that
     * wraps around the actual payload object. Payload can be null and can also be expired, which denotes the payload
     * is stale and needs updating, but can still be used while updating is complete.
     *
     * @param key String representing the name under which the object is stored in the cache
     * @return CacheElement the wrapper around the actual payload object in cache
     */
    <T> CacheElement<T> loadPayloadFromLocalCache(String key);

    /**
     * Store given item in the cache with a simple time-to-live property (for items not depending on Tridion items)
     *
     * @param key          String representing the name under which the object is stored in the cache
     * @param cacheElement CacheElement representing wrapper around the actual payload to store in cache
     */
    <T> void storeInItemCache(String key, CacheElement<T> cacheElement);

    /**
     * Store given item in the cache with a reference to supplied Tridion Item.
     *
     * @param key                    String representing the name under which the object is stored in the cache
     * @param cacheElement           CacheElement representing wrapper around the actual payload to store in cache
     * @param dependingPublicationId int representing the Publication id of the Tridion item the cacheItem depends on
     * @param dependingItemId        int representing the Item id of the Tridion item the cacheItem depends on
     */
    <T> void storeInItemCache(String key, CacheElement<T> cacheElement, int dependingPublicationId, int
            dependingItemId);

    /**
     * Stores item in the cache with a reference to all given depdencies
     *
     * @param key          String representing the name under which the object is stored in the cache
     * @param cacheElement CacheElement representing wrapper around the actual payload to store in cache
     * @param dependencies List of CacheDependency objects, representing the dependencies for this item
     */
    <T> void storeInItemCache(String key, CacheElement<T> cacheElement, List<CacheDependency> dependencies);

    /**
     * Adds a dependency between two cache keys
     *
     * @param cacheKey      String representing the key of the cache item that will flush if the other item flushes
     * @param dependencyKey String representing the key of the cache item that will act as a dependency
     */
    void addDependency(String cacheKey, String dependencyKey);


    /**
     * Checks the cache.enabled property in dd4t.properties
     * and then determines whether anything should be cached at all.
     *
     * @return boolean. cache is enabled or disabled.
     */
    boolean isEnabled();
}
