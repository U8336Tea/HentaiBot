package images.danbooru;

//https://stackoverflow.com/a/17767419
//http://techgarage.io/index.php/2017/01/07/jaxb-with-kotlin/

import org.jetbrains.annotations.NotNull;
import images.general.Image;
import images.general.ImageSet;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

@XmlRootElement(name = "posts")
@XmlAccessorType(XmlAccessType.FIELD)
public class DanbooruRoot implements ImageSet {
    @XmlElement(name = "post")
    private ArrayList<DanbooruPost> posts;

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