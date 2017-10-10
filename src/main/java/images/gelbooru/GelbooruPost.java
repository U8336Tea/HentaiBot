package images.gelbooru;

import org.jetbrains.annotations.NotNull;
import images.general.Image;
import images.general.ImageRating;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class GelbooruPost implements Image {
    @XmlAttribute
    private int score;

    @XmlAttribute
    private ImageRating rating;

    @XmlAttribute(name = "sample_url")
    private String sampleUrl;

    @XmlAttribute(name = "file_url")
    private String url;

    @XmlAttribute
    private String source;

    @XmlAttribute
    private List<String> tags;

    @Override
    public int getScore() {
        return score;
    }

    @Nullable
    @Override
    public String getUrl() {
        return sampleUrl;
    }

    @Nullable
    @Override
    public String getBigUrl() {
        return url;
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
