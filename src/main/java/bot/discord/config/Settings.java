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

    @XmlElement(name = "database-driver")
    private String databaseDriver;
    //endregion

    @NotNull
    private String access(@NotNull String var) {
        if (var.startsWith("$")) return System.getenv(var.substring(1));
        return var;
    }

    //region Accessors
    @NotNull
    public String getToken() {
        return access(token);
    }

    @NotNull
    public String getOwnerId() {
        return access(ownerId);
    }

    @NotNull
    public String getPrefix() {
        return access(prefix);
    }

    @Nullable
    public String getAltPrefix() {
        return access(altPrefix);
    }

    @NotNull
    public String getDatabaseUrl() {
        return access(databaseUrl);
    }

    @NotNull
    public String getDatabaseDriver() {
        return access(databaseDriver);
    }
    //endregion
}
