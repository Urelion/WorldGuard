// $Id$
/*
 * This file is a part of WorldGuard.
 * Copyright (c) sk89q <http://www.sk89q.com>
 * Copyright (c) the WorldGuard team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY), without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.sk89q.worldguard.region.indices;

import java.util.Collection;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.region.Region;

/**
 * Indices keep a collection of regions, either partially or fully, in-memory in order
 * to allow for fast spatial queries. For maximum performance, indices should use
 * some form of a spatial index in order to quickly satisfy queries.
 * <p>
 * A collection of "frequently hit" regions may be kept and used if the
 * "preferOnlyCached" parameter is true in the spatial query methods. If the parameter
 * is true, then the implementation may search only between those regions, but it may
 * choose to search among all regions in the index (for example, if not implementing
 * the feature). Whether a region should be kept in this special cache is determined
 * using the method {@link Region#shouldCache()}.
 * <p>
 * Region IDs are case in-sensitive and implementations must be aware of this when
 * querying by ID. Any casing can be used when looking up an ID. Implementations must
 * be thread-safe.
 */
public interface RegionIndex {

    /**
     * Queries the index for a list of {@link Region}s that contain the
     * given location. Parent regions of regions that match the criteria may NOT
     * be included in the returned list unless they also match the criteria.
     *
     * @param location the location to use
     * @param preferOnlyCached true to only search cached regions (see class docs)
     * @return a collection of regions matching the criteria
     */
    Collection<Region> queryContains(
            Vector location, boolean preferOnlyCached);

    /**
     * Queries the index for a list of {@link Region}s that overlap with
     * the given region. Parent regions of regions that match the criteria may NOT
     * be included in the returned list unless they also match the criteria. Regions
     * in the index do not have to be wholly contained by the provided region
     * in order to satisfy the criteria.
     *
     * @param region the area that returned regions must overlap with
     * @param preferOnlyCached true to only search cached regions (see class docs)
     * @return a collection of regions matching the criteria
     */
    Collection<Region> queryOverlapping(
            Region region, boolean preferOnlyCached);

    /**
     * Queries the index for a list of {@link Region}s that contain the
     * given location. Parent regions of regions that match the criteria may NOT
     * be included in the returned list unless they also match the criteria.
     * <p>
     * Compared to the other method ({@link #queryContains(Vector, boolean)}), you
     * cannot choose to prefer only cached regions with this method.
     *
     * @see #queryContains(Vector, boolean)
     * @param location the location to use
     * @return a collection of regions matching the criteria
     */
    Collection<Region> queryContains(Vector location);

    /**
     * Queries the index for a list of {@link Region}s that overlap with
     * the given region. Parent regions of regions that match the criteria may NOT
     * be included in the returned list unless they also match the criteria. Regions
     * in the index do not have to be wholly contained by the provided region
     * in order to satisfy the criteria.
     * <p>
     * Compared to the other method ({@link #queryContains(Vector, boolean)}), you
     * cannot choose to prefer only cached regions with this method.
     *
     * @see #queryOverlapping(Region, boolean)
     * @param region the area that returned regions must overlap with
     * @return a collection of regions matching the criteria
     */
    Collection<Region> queryOverlapping(Region region);

    /**
     * Add the given region to this index. If a region already known by this index is
     * attempted to be added to this index, nothing will happen. Parents of the
     * given region will not be added automatically.
     *
     * @param region the region to add
     */
    void add(Region... region);

    /**
     * Remove the region with the given ID from this index. If the region being removed
     * has children, they will not be removed automatically. Their parent will also
     * not be set to null automatically.
     *
     * @param id the ID of the region to remove
     */
    void remove(String... id);

    /**
     * Remove a region from this index having the exact same ID, but possibly no
     * other equal attribute. If the region being removed as children, they will not be
     * removed automatically. Their parent will also not be set to null automatically.
     *
     * @param region the region with the ID to match against
     */
    void removeMatching(Region... region);

    /**
     * Get a region given by the ID.
     *
     * @see Region#getId() for details on acceptable IDs
     * @param id the ID
     * @return the requested region or null
     */
    Region get(String id);

    /**
     * Get a region with a matching ID. The returned region may or may not be the
     * same region as the provided one, as the only feature that has to match is
     * the region ID.
     *
     * @param region the region with the ID to match against
     * @return the requested region or null
     */
    Region getMatching(Region region);

    /**
     * Queries the index to see whether it contains a region given by an ID.
     *
     * @param id the ID
     * @return true if this index contains a region by the given ID
     */
    boolean contains(String id);

    /**
     * Queries the index to see whether it contains a region with an ID the same
     * as the given ID. The returned region may or may not be the same region as the
     * provided one, as the only feature that has to match is the region ID.
     *
     * @param region the region with the ID to match against
     * @return true if this index contains a region by the given ID
     */
    boolean containsMatching(Region region);

    /**
     * Queries the index to see whether it contains the exact given region object.
     *
     * @param region the region to search
     * @return true if this index contains the given region
     */
    boolean containsExact(Region region);

    /**
     * Get the number of regions stored in the index. This may not be the total
     * number of regions, as especially with the case of partial store-backed indices.
     *
     * @return the number of regions
     */
    int size();

}
