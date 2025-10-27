/*
 * SPDX-FileCopyrightText: 2022-2024 NewPipe contributors <https://newpipe.net>
 * SPDX-FileCopyrightText: 2025 NewPipe e.V. <https://newpipe-ev.de>
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package org.schabi.newpipe.database.playlist

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.schabi.newpipe.database.playlist.model.PlaylistRemoteEntity
import org.schabi.newpipe.local.bookmark.MergedPlaylistManager

class PlaylistLocalItemTest {

    @Test
    fun emptyPlaylists() {
        val localPlaylists = listOf<PlaylistMetadataEntry?>()
        val remotePlaylists = listOf<PlaylistRemoteEntity?>()
        val mergedPlaylists = MergedPlaylistManager.merge(localPlaylists, remotePlaylists)
        assertEquals(0, mergedPlaylists.size)
    }

    @Test
    fun onlyLocalPlaylists() {
        val localPlaylists = listOf<PlaylistMetadataEntry?>(
            PlaylistMetadataEntry(
                uid = 1,
                orderingName = "name1",
                thumbnailUrl = "",
                isThumbnailPermanent = false,
                thumbnailStreamId = -1,
                displayIndex = 0,
                streamCount = 1
            ),
            PlaylistMetadataEntry(
                uid = 2,
                orderingName = "name2",
                thumbnailUrl = "",
                isThumbnailPermanent = false,
                thumbnailStreamId = -1,
                displayIndex = 1,
                streamCount = 1
            ),
            PlaylistMetadataEntry(
                uid = 3,
                orderingName = "name3",
                thumbnailUrl = "",
                isThumbnailPermanent = false,
                thumbnailStreamId = -1,
                displayIndex = 3,
                streamCount = 1
            )
        )
        val remotePlaylists = listOf<PlaylistRemoteEntity?>()
        val mergedPlaylists = MergedPlaylistManager.merge(localPlaylists, remotePlaylists)

        assertEquals(3, mergedPlaylists.size)
        assertEquals(0L, mergedPlaylists[0]!!.displayIndex)
        assertEquals(1L, mergedPlaylists[1]!!.displayIndex)
        assertEquals(3L, mergedPlaylists[2]!!.displayIndex)
    }

    @Test
    fun onlyRemotePlaylists() {
        val localPlaylists = listOf<PlaylistMetadataEntry?>()
        val remotePlaylists = listOf<PlaylistRemoteEntity?>(
            PlaylistRemoteEntity(
                serviceId = 1,
                orderingName = "name1",
                url = "url1",
                thumbnailUrl = "",
                uploader = "",
                displayIndex = 1,
                streamCount = 1
            ),
            PlaylistRemoteEntity(
                serviceId = 2,
                orderingName = "name2",
                url = "url2",
                thumbnailUrl = "",
                uploader = "",
                displayIndex = 2,
                streamCount = 1
            ),
            PlaylistRemoteEntity(
                serviceId = 3,
                orderingName = "name3",
                url = "url3",
                thumbnailUrl = "",
                uploader = "",
                displayIndex = 4,
                streamCount = 1
            )
        )
        val mergedPlaylists = MergedPlaylistManager.merge(localPlaylists, remotePlaylists)

        assertEquals(3, mergedPlaylists.size)
        assertEquals(1L, mergedPlaylists[0]!!.displayIndex)
        assertEquals(2L, mergedPlaylists[1]!!.displayIndex)
        assertEquals(4L, mergedPlaylists[2]!!.displayIndex)
    }

    @Test
    fun sameIndexWithDifferentName() {
        val localPlaylists = listOf<PlaylistMetadataEntry?>(
            PlaylistMetadataEntry(
                uid = 1,
                orderingName = "name1",
                thumbnailUrl = "",
                isThumbnailPermanent = false,
                thumbnailStreamId = -1,
                displayIndex = 0,
                streamCount = 1
            ),
            PlaylistMetadataEntry(
                uid = 2,
                orderingName = "name2",
                thumbnailUrl = "",
                isThumbnailPermanent = false,
                thumbnailStreamId = -1,
                displayIndex = 1,
                streamCount = 1
            )
        )
        val remotePlaylists = listOf<PlaylistRemoteEntity?>(
            PlaylistRemoteEntity(
                serviceId = 1,
                orderingName = "name3",
                url = "url1",
                thumbnailUrl = "",
                uploader = "",
                displayIndex = 0,
                streamCount = 1
            ),
            PlaylistRemoteEntity(
                serviceId = 2,
                orderingName = "name4",
                url = "url2",
                thumbnailUrl = "",
                uploader = "",
                displayIndex = 1,
                streamCount = 1
            )
        )
        val mergedPlaylists = MergedPlaylistManager.merge(localPlaylists, remotePlaylists)

        assertEquals(4, mergedPlaylists.size)
        assertTrue(mergedPlaylists[0] is PlaylistMetadataEntry)
        assertEquals("name1", (mergedPlaylists[0] as PlaylistMetadataEntry).orderingName)
        assertTrue(mergedPlaylists[1] is PlaylistRemoteEntity)
        assertEquals("name3", (mergedPlaylists[1] as PlaylistRemoteEntity).orderingName)
        assertTrue(mergedPlaylists[2] is PlaylistMetadataEntry)
        assertEquals("name2", (mergedPlaylists[2] as PlaylistMetadataEntry).orderingName)
        assertTrue(mergedPlaylists[3] is PlaylistRemoteEntity)
        assertEquals("name4", (mergedPlaylists[3] as PlaylistRemoteEntity).orderingName)
    }

    @Test
    fun sameNameWithDifferentIndex() {
        val localPlaylists = listOf<PlaylistMetadataEntry?>(
            PlaylistMetadataEntry(
                uid = 1,
                orderingName = "name1",
                thumbnailUrl = "",
                isThumbnailPermanent = false,
                thumbnailStreamId = -1,
                displayIndex = 1,
                streamCount = 1
            ),
            PlaylistMetadataEntry(
                uid = 2,
                orderingName = "name2",
                thumbnailUrl = "",
                isThumbnailPermanent = false,
                thumbnailStreamId = -1,
                displayIndex = 3,
                streamCount = 1
            )
        )
        val remotePlaylists = listOf<PlaylistRemoteEntity?>(
            PlaylistRemoteEntity(
                serviceId = 1,
                orderingName = "name1",
                url = "url1",
                thumbnailUrl = "",
                uploader = "",
                displayIndex = 0,
                streamCount = 1
            ),
            PlaylistRemoteEntity(
                serviceId = 2,
                orderingName = "name2",
                url = "url2",
                thumbnailUrl = "",
                uploader = "",
                displayIndex = 2,
                streamCount = 1
            )
        )
        val mergedPlaylists = MergedPlaylistManager.merge(localPlaylists, remotePlaylists)

        assertEquals(4, mergedPlaylists.size)
        assertTrue(mergedPlaylists[0] is PlaylistRemoteEntity)
        assertEquals("name1", (mergedPlaylists[0] as PlaylistRemoteEntity).orderingName)
        assertTrue(mergedPlaylists[1] is PlaylistMetadataEntry)
        assertEquals("name1", (mergedPlaylists[1] as PlaylistMetadataEntry).orderingName)
        assertTrue(mergedPlaylists[2] is PlaylistRemoteEntity)
        assertEquals("name2", (mergedPlaylists[2] as PlaylistRemoteEntity).orderingName)
        assertTrue(mergedPlaylists[3] is PlaylistMetadataEntry)
        assertEquals("name2", (mergedPlaylists[3] as PlaylistMetadataEntry).orderingName)
    }
}
