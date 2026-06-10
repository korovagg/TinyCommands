package win.korowin.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import win.korowin.config.TinyCommandsConfig;

/**
 * ModMenu integration for Tiny Commands (1.21.1 Fabric).
 */
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(TinyCommandsConfig.class, parent).get();
    }
}
