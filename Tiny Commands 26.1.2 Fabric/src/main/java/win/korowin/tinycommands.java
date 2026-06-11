package win.korowin;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import win.korowin.config.TinyCommandsConfig;

public class tinycommands implements ModInitializer {
    private static TinyCommandsConfig config;

    @Override
    public void onInitialize() {
        // Initialize Cloth Config
        AutoConfig.register(TinyCommandsConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(TinyCommandsConfig.class).getConfig();
    }

    public static TinyCommandsConfig getConfig() {
        return config;
    }
}
