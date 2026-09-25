package com.example.data.firebase

import android.util.Log
import com.example.model.Song
import com.example.model.UserSession
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreSyncManager {

    private val tag = "FirestoreSyncManager"

    private val firestore: FirebaseFirestore?
        get() = try {
            if (FirebaseApp.getApps(FirebaseApp.getInstance().applicationContext).isNotEmpty()) {
                FirebaseFirestore.getInstance()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(tag, "Firestore instance not available yet: ${e.message}")
            null
        }

    suspend fun saveUserProfile(user: UserSession) = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext
        try {
            val userMap = hashMapOf(
                "uid" to user.uid,
                "email" to (user.email ?: ""),
                "displayName" to (user.displayName ?: ""),
                "photoUrl" to (user.photoUrl ?: ""),
                "authProvider" to user.authProvider,
                "lastActiveAt" to System.currentTimeMillis()
            )
            db.collection("users").document(user.uid)
                .set(userMap, SetOptions.merge())
                .await()
            Log.d(tag, "User profile saved to Firestore for ${user.uid}")
        } catch (e: Exception) {
            Log.w(tag, "Failed to save user profile to Firestore: ${e.message}")
        }
    }

    suspend fun saveFavorite(userId: String, song: Song) = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext
        try {
            val songData = hashMapOf(
                "id" to song.id,
                "title" to song.title,
                "artist" to song.artist,
                "album" to song.album,
                "artworkUrl" to song.artworkUrl,
                "previewUrl" to (song.previewUrl ?: ""),
                "durationMs" to song.durationMs,
                "genre" to song.genre,
                "spotifyTrackId" to (song.spotifyTrackId ?: ""),
                "syncedAt" to System.currentTimeMillis()
            )
            db.collection("users").document(userId)
                .collection("favorites").document(song.id.toString())
                .set(songData, SetOptions.merge())
                .await()
            Log.d(tag, "Favorite song ${song.title} synced to Firestore")
        } catch (e: Exception) {
            Log.w(tag, "Failed to save favorite to Firestore: ${e.message}")
        }
    }

    suspend fun removeFavorite(userId: String, songId: Long) = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext
        try {
            db.collection("users").document(userId)
                .collection("favorites").document(songId.toString())
                .delete()
                .await()
            Log.d(tag, "Favorite song $songId removed from Firestore")
        } catch (e: Exception) {
            Log.w(tag, "Failed to remove favorite from Firestore: ${e.message}")
        }
    }

    suspend fun saveHistory(userId: String, song: Song) = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext
        try {
            val historyData = hashMapOf(
                "songId" to song.id,
                "title" to song.title,
                "artist" to song.artist,
                "album" to song.album,
                "artworkUrl" to song.artworkUrl,
                "previewUrl" to (song.previewUrl ?: ""),
                "genre" to song.genre,
                "playedAt" to System.currentTimeMillis()
            )
            db.collection("users").document(userId)
                .collection("history").document(song.id.toString())
                .set(historyData, SetOptions.merge())
                .await()
            Log.d(tag, "History track ${song.title} synced to Firestore")
        } catch (e: Exception) {
            Log.w(tag, "Failed to save history to Firestore: ${e.message}")
        }
    }

    suspend fun getRemoteFavorites(userId: String): List<Song> = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext emptyList()
        try {
            val snapshot = db.collection("users").document(userId)
                .collection("favorites")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                val id = doc.getLong("id") ?: doc.id.toLongOrNull() ?: return@mapNotNull null
                val title = doc.getString("title") ?: return@mapNotNull null
                val artist = doc.getString("artist") ?: "Unknown Artist"
                val album = doc.getString("album") ?: ""
                val artworkUrl = doc.getString("artworkUrl") ?: ""
                val previewUrl = doc.getString("previewUrl")
                val durationMs = doc.getLong("durationMs") ?: 30000L
                val genre = doc.getString("genre") ?: "Pop"
                val spotifyTrackId = doc.getString("spotifyTrackId")

                Song(
                    id = id,
                    title = title,
                    artist = artist,
                    album = album,
                    artworkUrl = artworkUrl,
                    previewUrl = previewUrl,
                    durationMs = durationMs,
                    genre = genre,
                    spotifyTrackId = spotifyTrackId
                )
            }
        } catch (e: Exception) {
            Log.w(tag, "Failed to fetch favorites from Firestore: ${e.message}")
            emptyList()
        }
    }

    suspend fun syncAllFavorites(userId: String, localFavorites: List<Song>): List<Song> = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext localFavorites
        try {
            // 1. Upload local favorites that may not be on remote
            for (song in localFavorites) {
                saveFavorite(userId, song)
            }
            // 2. Fetch remote favorites
            val remote = getRemoteFavorites(userId)
            val combined = (localFavorites + remote).distinctBy { it.id }
            combined
        } catch (e: Exception) {
            Log.w(tag, "Error during full favorites sync: ${e.message}")
            localFavorites
        }
    }
}
