package bot.discord.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "bot")
@XmlAccessorType(XmlAccessType.FIELD)
public class Settings {
    //region XML Variables
    @XmlElement
    private String token;

    @XmlElement(name = "owner-id")
    private String ownerId;

    @XmlAttribute
    private String prefix = "!!";

    @XmlAttribute(name = "alt-prefix")
    private String altPrefix;

    @XmlElement(name = "database-url")
    private String databaseUrl;
    //endregion

    //region Accessors
    @NotNull
    public String getToken() {
        return token;
    }

    @NotNull
    public String getOwnerId() {
        return ownerId;
    }

    @NotNull
    public String getPrefix() {
        return prefix;
    }

    @Nullable
    public String getAltPrefix() {
        return altPrefix;
    }

    @NotNull
    public String getDatabaseUrl() {
        return databaseUrl;
    }
    //endregion
}
