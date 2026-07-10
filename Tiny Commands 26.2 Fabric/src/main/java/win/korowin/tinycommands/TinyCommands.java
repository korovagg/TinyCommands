package win.korowin.tinycommands;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import win.korowin.tinycommands.config.TinyCommandsConfig;

public class TinyCommands implements ModInitializer {
	public static final String MOD_ID = "tinycommands";
	private static TinyCommandsConfig config;

	@Override
	public void onInitialize() {
		AutoConfig.register(TinyCommandsConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(TinyCommandsConfig.class).getConfig();
	}

	public static TinyCommandsConfig getConfig() {
		return config;
	}
}
