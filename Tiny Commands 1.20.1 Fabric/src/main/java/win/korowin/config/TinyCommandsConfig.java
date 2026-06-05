package win.korowin.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

/**
 * Configuration data for Tiny Commands mod.
 */
@Config(name = "tinycommands")
public class TinyCommandsConfig implements ConfigData {

    public boolean enableTimeCommands = true;
    public boolean enableWeatherCommands = true;
    public boolean enableGamemodeCommands = true;
    public boolean enableDifficultyCommands = true;
    public boolean enableSpawnpointCommand = true;
    public boolean enableHealFeedCommands = true;
    public boolean enableGameruleCommand = true;
    public boolean enableNightVisionCommand = true;
    public boolean enableEffectClearCommand = true;
    public boolean enableTpHereCommand = true;
}
