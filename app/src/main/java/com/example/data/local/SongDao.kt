package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    // Favorites
    @Query("SELECT * FROM favorite_songs ORDER BY savedAtTimestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteSongEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_songs WHERE id = :songId)")
    fun isFavorite(songId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(song: FavoriteSongEntity)

    @Query("DELETE FROM favorite_songs WHERE id = :songId")
    suspend fun deleteFavorite(songId: Long)

    @Query("SELECT cachedLyrics FROM favorite_songs WHERE id = :songId")
    suspend fun getCachedLyrics(songId: Long): String?

    // History
    @Query("SELECT * FROM history_songs ORDER BY playedAt DESC LIMIT 100")
    fun getAllHistory(): Flow<List<HistorySongEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(item: HistorySongEntity)

    @Query("DELETE FROM history_songs WHERE historyId = :historyId")
    suspend fun deleteHistoryItem(historyId: Long)

    @Query("DELETE FROM history_songs WHERE songId = :songId")
    suspend fun deleteHistoryBySongId(songId: Long)

    @Query("DELETE FROM history_songs")
    suspend fun clearAllHistory()

    // Cached Songs (Offline Caching)
    @Query("SELECT * FROM cached_songs")
    suspend fun getAllCachedSongs(): List<CachedSongEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedSongs(songs: List<CachedSongEntity>)

    @Query("SELECT * FROM cached_songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' OR genre LIKE '%' || :query || '%'")
    suspend fun searchCachedSongs(query: String): List<CachedSongEntity>

    // Cached Lyrics (Offline Caching)
    @Query("SELECT * FROM cached_lyrics WHERE songId = :songId")
    suspend fun getCachedLyricsEntity(songId: Long): CachedLyricsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedLyrics(entity: CachedLyricsEntity)
}
