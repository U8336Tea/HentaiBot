package images.gelbooru;

import org.jetbrains.annotations.NotNull;
import images.general.Image;
import images.general.ImageSet;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

@XmlRootElement(name = "posts")
@XmlAccessorType(XmlAccessType.FIELD)
public class GelbooruRoot implements ImageSet {
    @XmlElement(name = "post")
    private ArrayList<GelbooruPost> posts;

    @NotNull
    @Override
    public ArrayList<Image> getImages() {
        if (posts == null) return new ArrayList<>();

        //Necessary to cast to Image
        ArrayList<Image> images = new ArrayList<>();
        images.addAll(posts);
        return images;
    }
}
