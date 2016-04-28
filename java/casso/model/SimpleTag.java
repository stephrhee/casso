package casso.model;

import java.util.List;

/**
 * Created by stephrhee on 4/27/16.
 */

public class SimpleTag {

    public String encodedString;
    public List<SimpleArtwork> suggestedArtworks;

    @SuppressWarnings("unused")
    private SimpleTag() {
    }

    public SimpleTag(String encodedString, List<SimpleArtwork> suggestedArtworks) {
        this.encodedString = encodedString;
        this.suggestedArtworks = suggestedArtworks;
    }

    public String getEncodedString() {
        return this.encodedString;
    }

    public List<SimpleArtwork> getSuggestedArtworks() {
        return this.suggestedArtworks;
    }

    public static class SimpleArtwork {

        public Integer id;
        public String thumbUrl;

        @SuppressWarnings("unused")
        private SimpleArtwork() {
        }

        public SimpleArtwork(Integer id, String thumbUrl) {
            this.id = id;
            this.thumbUrl = thumbUrl;
        }

        public Integer getId() {
            return this.id;
        }

        public String getThumbUrl() {
            return this.thumbUrl;
        }

    }

}
