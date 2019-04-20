package images.danbooru;

import org.jetbrains.annotations.NotNull;
import images.general.Image;
import images.general.ImageRating;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class DanbooruPost implements Image {
    @XmlElement
    private ImageRating rating;

    @XmlElement
    private int score;

    @XmlElement(name = "file-url")
    private String url;

    @XmlElement(name = "large-file-url")
    private String largeUrl;

    @XmlElement
    private String source;

    @XmlElement(name = "tag-string")
    @XmlList
    private List<String> tags;

    @Override
    public int getScore() {
        return score;
    }

    @Nullable
    @Override
    public String getUrl() {
        if (url == null || url.equals("null")) return null;

        return "https://danbooru.donmai.us" + url;
    }

    @Nullable
    @Override
    public String getBigUrl() {
        if (largeUrl == null || largeUrl.equals("null")) return null;

        return "https://danbooru.donmai.us" + largeUrl;
    }

    @NotNull
    @Override
    public ImageRating getRating() {
        return rating;
    }

    @Override
    public String getSource() {
        return source;
    }

    @NotNull
    @Override
    public List<String> getTags() {
        return tags;
    }
}
