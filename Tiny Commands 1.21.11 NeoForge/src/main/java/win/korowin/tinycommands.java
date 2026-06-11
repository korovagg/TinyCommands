package win.korowin;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import win.korowin.config.TinyCommandsConfig;

/**
 * Main entry point for Tiny Commands mod (NeoForge 1.21.11).
 */
@Mod(tinycommands.MODID)
public class tinycommands {
    public static final String MODID = "tinycommands";
    private static TinyCommandsConfig config;

    public tinycommands(ModContainer container) {
        // Initialize Cloth Config
        AutoConfig.register(TinyCommandsConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(TinyCommandsConfig.class).getConfig();

        // Register the client command event on the NeoForge event bus
        NeoForge.EVENT_BUS.register(this);

        // Register Config Screen for NeoForge
        container.registerExtensionPoint(IConfigScreenFactory.class, 
            (client, parent) -> AutoConfigClient.getConfigScreen(TinyCommandsConfig.class, parent).get()
        );
    }

    public static TinyCommandsConfig getConfig() {
        return config;
    }

    /**
     * Event listener for registering client-side commands in NeoForge.
     */
    @SubscribeEvent
    public void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        TinyCommandsConfig config = getConfig();

        // Time commands
        if (config.enableTimeCommands) {
            dispatcher.register(net.minecraft.commands.Commands.literal("day").executes(context -> { executeCommand("time set day"); return 1; }));
            dispatcher.register(net.minecraft.commands.Commands.literal("night").executes(context -> { executeCommand("time set night"); return 1; }));
            dispatcher.register(net.minecraft.commands.Commands.literal("noon").executes(context -> { executeCommand("time set noon"); return 1; }));
            dispatcher.register(net.minecraft.commands.Commands.literal("midnight").executes(context -> { executeCommand("time set midnight"); return 1; }));
        }

        // Weather commands
        if (config.enableWeatherCommands) {
            dispatcher.register(net.minecraft.commands.Commands.literal("sun").executes(context -> { executeCommand("weather clear"); return 1; }));
            dispatcher.register(net.minecraft.commands.Commands.literal("rain").executes(context -> { executeCommand("weather rain"); return 1; }));
            dispatcher.register(net.minecraft.commands.Commands.literal("thunder").executes(context -> { executeCommand("weather thunder"); return 1; }));

            dispatcher.register(net.minecraft.commands.Commands.literal("wtr")
                    .then(net.minecraft.commands.Commands.literal("clear").executes(context -> { executeCommand("weather clear"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("c").executes(context -> { executeCommand("weather clear"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("rain").executes(context -> { executeCommand("weather rain"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("r").executes(context -> { executeCommand("weather rain"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("thunder").executes(context -> { executeCommand("weather thunder"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("t").executes(context -> { executeCommand("weather thunder"); return 1; })));
        }

        // Gamemode commands
        if (config.enableGamemodeCommands) {
            dispatcher.register(net.minecraft.commands.Commands.literal("gm")
                    .then(net.minecraft.commands.Commands.literal("0").executes(context -> { executeCommand("gamemode survival"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("1").executes(context -> { executeCommand("gamemode creative"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("2").executes(context -> { executeCommand("gamemode adventure"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("3").executes(context -> { executeCommand("gamemode spectator"); return 1; })));
        }

        // Difficulty commands
        if (config.enableDifficultyCommands) {
            dispatcher.register(net.minecraft.commands.Commands.literal("diff")
                    .then(net.minecraft.commands.Commands.literal("peaceful").executes(context -> { executeCommand("difficulty peaceful"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("p").executes(context -> { executeCommand("difficulty peaceful"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("easy").executes(context -> { executeCommand("difficulty easy"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("e").executes(context -> { executeCommand("difficulty easy"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("normal").executes(context -> { executeCommand("difficulty normal"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("n").executes(context -> { executeCommand("difficulty normal"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("hard").executes(context -> { executeCommand("difficulty hard"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("h").executes(context -> { executeCommand("difficulty hard"); return 1; })));
        }

        // Spawnpoint command
        if (config.enableSpawnpointCommand) {
            dispatcher.register(net.minecraft.commands.Commands.literal("sp").executes(context -> { executeCommand("spawnpoint"); return 1; }));
        }

        // Heal/Feed commands
        if (config.enableHealFeedCommands) {
            dispatcher.register(net.minecraft.commands.Commands.literal("heal").executes(context -> { executeCommand("effect give @s minecraft:instant_health 1 10"); return 1; }));
            dispatcher.register(net.minecraft.commands.Commands.literal("feed").executes(context -> { executeCommand("effect give @s minecraft:saturation 1 255"); return 1; }));
        }

        // Gamerule command (/gr)
        if (config.enableGameruleCommand) {
            var grNode = net.minecraft.commands.Commands.literal("gr");
            
            grNode.then(net.minecraft.commands.Commands.literal("keepinv")
                    .then(net.minecraft.commands.Commands.literal("true").executes(context -> { executeCommand("gamerule keep_inventory true"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("t").executes(context -> { executeCommand("gamerule keep_inventory true"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("false").executes(context -> { executeCommand("gamerule keep_inventory false"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("f").executes(context -> { executeCommand("gamerule keep_inventory false"); return 1; })));

            grNode.then(net.minecraft.commands.Commands.literal("day")
                    .then(net.minecraft.commands.Commands.literal("true").executes(context -> { executeCommand("gamerule advance_time true"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("t").executes(context -> { executeCommand("gamerule advance_time true"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("false").executes(context -> { executeCommand("gamerule advance_time false"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("f").executes(context -> { executeCommand("gamerule advance_time false"); return 1; })));

            grNode.then(net.minecraft.commands.Commands.literal("wtr")
                    .then(net.minecraft.commands.Commands.literal("true").executes(context -> { executeCommand("gamerule advance_weather true"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("t").executes(context -> { executeCommand("gamerule advance_weather true"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("false").executes(context -> { executeCommand("gamerule advance_weather false"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("f").executes(context -> { executeCommand("gamerule advance_weather false"); return 1; })));

            grNode.then(net.minecraft.commands.Commands.literal("rts")
                    .then(net.minecraft.commands.Commands.argument("value", IntegerArgumentType.integer(0))
                            .executes(context -> {
                                int val = IntegerArgumentType.getInteger(context, "value");
                                executeCommand("gamerule random_tick_speed " + val);
                                return 1;
                            })));

            grNode.then(net.minecraft.commands.Commands.literal("pvp")
                    .then(net.minecraft.commands.Commands.literal("true").executes(context -> { executeCommand("gamerule pvp true"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("t").executes(context -> { executeCommand("gamerule pvp true"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("false").executes(context -> { executeCommand("gamerule pvp false"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("f").executes(context -> { executeCommand("gamerule pvp false"); return 1; })));

            grNode.then(net.minecraft.commands.Commands.literal("grief")
                    .then(net.minecraft.commands.Commands.literal("true").executes(context -> { executeCommand("gamerule mob_griefing true"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("t").executes(context -> { executeCommand("gamerule mob_griefing true"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("false").executes(context -> { executeCommand("gamerule mob_griefing false"); return 1; }))
                    .then(net.minecraft.commands.Commands.literal("f").executes(context -> { executeCommand("gamerule mob_griefing false"); return 1; })));

            dispatcher.register(grNode);
        }

        // Night Vision (/nv)
        if (config.enableNightVisionCommand) {
            dispatcher.register(net.minecraft.commands.Commands.literal("nv")
                    .executes(context -> {
                        executeCommand("effect give @s minecraft:night_vision infinite");
                        return 1;
                    }));
        }

        // Effect Clear (/eclear)
        if (config.enableEffectClearCommand) {
            dispatcher.register(net.minecraft.commands.Commands.literal("eclear")
                    .executes(context -> {
                        executeCommand("effect clear @s");
                        return 1;
                    }));
        }

        // TP Here (/tphere)
        if (config.enableTpHereCommand) {
            dispatcher.register(net.minecraft.commands.Commands.literal("tphere")
                    .then(net.minecraft.commands.Commands.argument("player", EntityArgument.player())
                            .executes(context -> {
                                String target = context.getInput().split(" ")[1];
                                executeCommand("tp " + target + " @s");
                                return 1;
                            })));
        }
    }

    /**
     * Executes a command on the client as if the player typed it.
     */
    private static void executeCommand(String command) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null && client.getConnection() != null) {
            client.getConnection().sendCommand(command);
        }
    }
}
