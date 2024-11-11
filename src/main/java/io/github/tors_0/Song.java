package io.github.tors_0;

public class Song {
    private final String title;
    private final String artist;
    private final String id;
    private final String imageUrl;

    public Song(String title, String artist, String id, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
