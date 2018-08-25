package be.rony.musicconverter.util;

import be.rony.musicconverter.domain.TagInfo;
import org.jflac.metadata.Picture;

import java.util.Map;

public final class TagUtil {

    private static final String KEY_TITLE ="TITLE";
    private static final String KEY_ARTIST="ARTIST";
    private static final String KEY_ALBUMARTIST="ALBUMARTIST";
    private static final String KEY_ALBUM="ALBUM";
    private static final String KEY_YEAR="DATE";
    private static final String KEY_TRACK="TRACKNUMBER";
    private static final String KEY_GENRE="GENRE";
    private static final String KEY_COMMENT="DESCRIPTION";
    private static final String KEY_COMPOSER="COMPOSER";
    private static final String KEY_PERFORMER="PERFORMER";
    private static final String KEY_COPYRIGHT="COPYRIGHT";


    private TagUtil() {
    }

    public static final TagInfo createTagInfo(Map<String,String> tags, Picture picture) {
        TagInfo tagInfo = new TagInfo();
        tagInfo.setTitle(tags.get(KEY_TITLE));
        tagInfo.setArtist(tags.get(KEY_ARTIST));
        tagInfo.setAlbumArtist(tags.get(KEY_ALBUMARTIST));
        tagInfo.setAlbum(tags.get(KEY_ALBUM));
        tagInfo.setYear(tags.get(KEY_YEAR));
        try {
            tagInfo.setTrack(Integer.valueOf(tags.get(KEY_TRACK)));
        } catch (NumberFormatException e) {
            tagInfo.setTrack(null);
        }
        tagInfo.setGenre(tags.get(KEY_GENRE));
        tagInfo.setComment(tags.get(KEY_COMMENT));
        tagInfo.setComposer(tags.get(KEY_COMPOSER));

        tagInfo.setCover(picture.);
    }
}
