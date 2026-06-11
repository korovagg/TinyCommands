package win.korowin;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import win.korowin.config.TinyCommandsConfig;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;

/**
 * Main mod class for Tiny Commands (26.1.2 NeoForge).
 */
@Mod(tinycommands.MODID)
public class tinycommands {
    public static final String MODID = "tinycommands";
    private static TinyCommandsConfig config;

    public tinycommands(IEventBus modEventBus, ModContainer modContainer) {
        // Initialize Cloth Config
        AutoConfig.register(TinyCommandsConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(TinyCommandsConfig.class).getConfig();

        // Register for events
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::onClientSetup);

        // Register Config Screen for NeoForge
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, 
            (client, parent) -> AutoConfigClient.getConfigScreen(TinyCommandsConfig.class, parent).get()
        );
    }

    public static TinyCommandsConfig getConfig() {
        return config;
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        // Client setup logic if needed
    }

    @SubscribeEvent
    public void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        var dispatcher = event.getDispatcher();

        // Time commands
        if (config.enableTimeCommands) {
            dispatcher.register(Commands.literal("day").executes(context -> { executeCommand("time set day"); return 1; }));
            dispatcher.register(Commands.literal("night").executes(context -> { executeCommand("time set night"); return 1; }));
            dispatcher.register(Commands.literal("noon").executes(context -> { executeCommand("time set noon"); return 1; }));
            dispatcher.register(Commands.literal("midnight").executes(context -> { executeCommand("time set midnight"); return 1; }));
        }

        // Weather commands
        if (config.enableWeatherCommands) {
            dispatcher.register(Commands.literal("sun").executes(context -> { executeCommand("weather clear"); return 1; }));
            dispatcher.register(Commands.literal("rain").executes(context -> { executeCommand("weather rain"); return 1; }));
            dispatcher.register(Commands.literal("thunder").executes(context -> { executeCommand("weather thunder"); return 1; }));

            dispatcher.register(Commands.literal("wtr")
                    .then(Commands.literal("clear").executes(context -> { executeCommand("weather clear"); return 1; }))
                    .then(Commands.literal("c").executes(context -> { executeCommand("weather clear"); return 1; }))
                    .then(Commands.literal("rain").executes(context -> { executeCommand("weather rain"); return 1; }))
                    .then(Commands.literal("r").executes(context -> { executeCommand("weather rain"); return 1; }))
                    .then(Commands.literal("thunder").executes(context -> { executeCommand("weather thunder"); return 1; }))
                    .then(Commands.literal("t").executes(context -> { executeCommand("weather thunder"); return 1; })));
        }

        // Gamemode commands
        if (config.enableGamemodeCommands) {
            dispatcher.register(Commands.literal("gm")
                    .then(Commands.literal("0").executes(context -> { executeCommand("gamemode survival"); return 1; }))
                    .then(Commands.literal("1").executes(context -> { executeCommand("gamemode creative"); return 1; }))
                    .then(Commands.literal("2").executes(context -> { executeCommand("gamemode adventure"); return 1; }))
                    .then(Commands.literal("3").executes(context -> { executeCommand("gamemode spectator"); return 1; })));
        }

        // Difficulty commands
        if (config.enableDifficultyCommands) {
            dispatcher.register(Commands.literal("diff")
                    .then(Commands.literal("peaceful").executes(context -> { executeCommand("difficulty peaceful"); return 1; }))
                    .then(Commands.literal("p").executes(context -> { executeCommand("difficulty peaceful"); return 1; }))
                    .then(Commands.literal("easy").executes(context -> { executeCommand("difficulty easy"); return 1; }))
                    .then(Commands.literal("e").executes(context -> { executeCommand("difficulty easy"); return 1; }))
                    .then(Commands.literal("normal").executes(context -> { executeCommand("difficulty normal"); return 1; }))
                    .then(Commands.literal("n").executes(context -> { executeCommand("difficulty normal"); return 1; }))
                    .then(Commands.literal("hard").executes(context -> { executeCommand("difficulty hard"); return 1; }))
                    .then(Commands.literal("h").executes(context -> { executeCommand("difficulty hard"); return 1; })));
        }

        // Spawnpoint command
        if (config.enableSpawnpointCommand) {
            dispatcher.register(Commands.literal("sp").executes(context -> { executeCommand("spawnpoint"); return 1; }));
        }

        // Heal/Feed commands
        if (config.enableHealFeedCommands) {
            dispatcher.register(Commands.literal("heal").executes(context -> { executeCommand("effect give @s minecraft:instant_health 1 10"); return 1; }));
            dispatcher.register(Commands.literal("feed").executes(context -> { executeCommand("effect give @s minecraft:saturation 1 255"); return 1; }));
        }

        // Gamerule command (/gr)
        if (config.enableGameruleCommand) {
            var grNode = Commands.literal("gr");
            
            grNode.then(Commands.literal("keepinv")
                    .then(Commands.literal("true").executes(context -> { executeCommand("gamerule keep_inventory true"); return 1; }))
                    .then(Commands.literal("t").executes(context -> { executeCommand("gamerule keep_inventory true"); return 1; }))
                    .then(Commands.literal("false").executes(context -> { executeCommand("gamerule keep_inventory false"); return 1; }))
                    .then(Commands.literal("f").executes(context -> { executeCommand("gamerule keep_inventory false"); return 1; })));

            grNode.then(Commands.literal("day")
                    .then(Commands.literal("true").executes(context -> { executeCommand("gamerule advance_time true"); return 1; }))
                    .then(Commands.literal("t").executes(context -> { executeCommand("gamerule advance_time true"); return 1; }))
                    .then(Commands.literal("false").executes(context -> { executeCommand("gamerule advance_time false"); return 1; }))
                    .then(Commands.literal("f").executes(context -> { executeCommand("gamerule advance_time false"); return 1; })));

            grNode.then(Commands.literal("wtr")
                    .then(Commands.literal("true").executes(context -> { executeCommand("gamerule advance_weather true"); return 1; }))
                    .then(Commands.literal("t").executes(context -> { executeCommand("gamerule advance_weather true"); return 1; }))
                    .then(Commands.literal("false").executes(context -> { executeCommand("gamerule advance_weather false"); return 1; }))
                    .then(Commands.literal("f").executes(context -> { executeCommand("gamerule advance_weather false"); return 1; })));

            grNode.then(Commands.literal("rts")
                    .then(Commands.argument("value", IntegerArgumentType.integer(0))
                            .executes(context -> {
                                int val = IntegerArgumentType.getInteger(context, "value");
                                executeCommand("gamerule random_tick_speed " + val);
                                return 1;
                            })));

            grNode.then(Commands.literal("pvp")
                    .then(Commands.literal("true").executes(context -> { executeCommand("gamerule pvp true"); return 1; }))
                    .then(Commands.literal("t").executes(context -> { executeCommand("gamerule pvp true"); return 1; }))
                    .then(Commands.literal("false").executes(context -> { executeCommand("gamerule pvp false"); return 1; }))
                    .then(Commands.literal("f").executes(context -> { executeCommand("gamerule pvp false"); return 1; })));

            grNode.then(Commands.literal("grief")
                    .then(Commands.literal("true").executes(context -> { executeCommand("gamerule mob_griefing true"); return 1; }))
                    .then(Commands.literal("t").executes(context -> { executeCommand("gamerule mob_griefing true"); return 1; }))
                    .then(Commands.literal("false").executes(context -> { executeCommand("gamerule mob_griefing false"); return 1; }))
                    .then(Commands.literal("f").executes(context -> { executeCommand("gamerule mob_griefing false"); return 1; })));

            dispatcher.register(grNode);
        }

        // Night Vision (/nv)
        if (config.enableNightVisionCommand) {
            dispatcher.register(Commands.literal("nv")
                    .executes(context -> {
                        executeCommand("effect give @s minecraft:night_vision infinite");
                        return 1;
                    }));
        }

        // Effect Clear (/eclear)
        if (config.enableEffectClearCommand) {
            dispatcher.register(Commands.literal("eclear")
                    .executes(context -> {
                        executeCommand("effect clear @s");
                        return 1;
                    }));
        }

        // TP Here (/tphere)
        if (config.enableTpHereCommand) {
            dispatcher.register(Commands.literal("tphere")
                    .then(Commands.argument("player", EntityArgument.player())
                            .executes(context -> {
                                String target = context.getInput().split(" ")[1];
                                executeCommand("tp " + target + " @s");
                                return 1;
                            })));
        }
    }

    /**
     * Executes a command on the client.
     */
    private static void executeCommand(String command) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null && client.player.connection != null) {
            client.player.connection.sendCommand(command);
        }
    }
}
