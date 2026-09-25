package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Artist

@Entity(tableName = "followed_artists")
data class FollowedArtistEntity(
    @PrimaryKey
    val name: String,
    val imageUrl: String,
    val genre: String,
    val topHitsCount: String,
    val followedAt: Long = System.currentTimeMillis()
) {
    fun toArtist(): Artist {
        return Artist(
            name = name,
            imageUrl = imageUrl,
            genre = genre,
            topHitsCount = topHitsCount
        )
    }

    companion object {
        fun fromArtist(artist: Artist): FollowedArtistEntity {
            return FollowedArtistEntity(
                name = artist.name,
                imageUrl = artist.imageUrl,
                genre = artist.genre,
                topHitsCount = artist.topHitsCount,
                followedAt = System.currentTimeMillis()
            )
        }
    }
}
