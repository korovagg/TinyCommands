package win.korowin.client;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityArgument;
import win.korowin.tinycommands;
import win.korowin.config.TinyCommandsConfig;

/**
 * Client-side entry point for Tiny Commands (26.1.2 Fabric).
 */
public class tinycommandsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            TinyCommandsConfig config = tinycommands.getConfig();

            // Time commands
            if (config.enableTimeCommands) {
                dispatcher.register(ClientCommands.literal("day").executes(context -> { executeCommand("time set day"); return 1; }));
                dispatcher.register(ClientCommands.literal("night").executes(context -> { executeCommand("time set night"); return 1; }));
                dispatcher.register(ClientCommands.literal("noon").executes(context -> { executeCommand("time set noon"); return 1; }));
                dispatcher.register(ClientCommands.literal("midnight").executes(context -> { executeCommand("time set midnight"); return 1; }));
            }

            // Weather commands
            if (config.enableWeatherCommands) {
                dispatcher.register(ClientCommands.literal("sun").executes(context -> { executeCommand("weather clear"); return 1; }));
                dispatcher.register(ClientCommands.literal("rain").executes(context -> { executeCommand("weather rain"); return 1; }));
                dispatcher.register(ClientCommands.literal("thunder").executes(context -> { executeCommand("weather thunder"); return 1; }));

                dispatcher.register(ClientCommands.literal("wtr")
                        .then(ClientCommands.literal("clear").executes(context -> { executeCommand("weather clear"); return 1; }))
                        .then(ClientCommands.literal("c").executes(context -> { executeCommand("weather clear"); return 1; }))
                        .then(ClientCommands.literal("rain").executes(context -> { executeCommand("weather rain"); return 1; }))
                        .then(ClientCommands.literal("r").executes(context -> { executeCommand("weather rain"); return 1; }))
                        .then(ClientCommands.literal("thunder").executes(context -> { executeCommand("weather thunder"); return 1; }))
                        .then(ClientCommands.literal("t").executes(context -> { executeCommand("weather thunder"); return 1; })));
            }

            // Gamemode commands
            if (config.enableGamemodeCommands) {
                dispatcher.register(ClientCommands.literal("gm")
                        .then(ClientCommands.literal("0").executes(context -> { executeCommand("gamemode survival"); return 1; }))
                        .then(ClientCommands.literal("1").executes(context -> { executeCommand("gamemode creative"); return 1; }))
                        .then(ClientCommands.literal("2").executes(context -> { executeCommand("gamemode adventure"); return 1; }))
                        .then(ClientCommands.literal("3").executes(context -> { executeCommand("gamemode spectator"); return 1; })));
            }

            // Difficulty commands
            if (config.enableDifficultyCommands) {
                dispatcher.register(ClientCommands.literal("diff")
                        .then(ClientCommands.literal("peaceful").executes(context -> { executeCommand("difficulty peaceful"); return 1; }))
                        .then(ClientCommands.literal("p").executes(context -> { executeCommand("difficulty peaceful"); return 1; }))
                        .then(ClientCommands.literal("easy").executes(context -> { executeCommand("difficulty easy"); return 1; }))
                        .then(ClientCommands.literal("e").executes(context -> { executeCommand("difficulty easy"); return 1; }))
                        .then(ClientCommands.literal("normal").executes(context -> { executeCommand("difficulty normal"); return 1; }))
                        .then(ClientCommands.literal("n").executes(context -> { executeCommand("difficulty normal"); return 1; }))
                        .then(ClientCommands.literal("hard").executes(context -> { executeCommand("difficulty hard"); return 1; }))
                        .then(ClientCommands.literal("h").executes(context -> { executeCommand("difficulty hard"); return 1; })));
            }

            // Spawnpoint command
            if (config.enableSpawnpointCommand) {
                dispatcher.register(ClientCommands.literal("sp").executes(context -> { executeCommand("spawnpoint"); return 1; }));
            }

            // Heal/Feed commands
            if (config.enableHealFeedCommands) {
                dispatcher.register(ClientCommands.literal("heal").executes(context -> { executeCommand("effect give @s minecraft:instant_health 1 10"); return 1; }));
                dispatcher.register(ClientCommands.literal("feed").executes(context -> { executeCommand("effect give @s minecraft:saturation 1 255"); return 1; }));
            }

            // Gamerule command (/gr)
            if (config.enableGameruleCommand) {
                var grNode = ClientCommands.literal("gr");
                
                grNode.then(ClientCommands.literal("keepinv")
                        .then(ClientCommands.literal("true").executes(context -> { executeCommand("gamerule keep_inventory true"); return 1; }))
                        .then(ClientCommands.literal("t").executes(context -> { executeCommand("gamerule keep_inventory true"); return 1; }))
                        .then(ClientCommands.literal("false").executes(context -> { executeCommand("gamerule keep_inventory false"); return 1; }))
                        .then(ClientCommands.literal("f").executes(context -> { executeCommand("gamerule keep_inventory false"); return 1; })));

                grNode.then(ClientCommands.literal("day")
                        .then(ClientCommands.literal("true").executes(context -> { executeCommand("gamerule advance_time true"); return 1; }))
                        .then(ClientCommands.literal("t").executes(context -> { executeCommand("gamerule advance_time true"); return 1; }))
                        .then(ClientCommands.literal("false").executes(context -> { executeCommand("gamerule advance_time false"); return 1; }))
                        .then(ClientCommands.literal("f").executes(context -> { executeCommand("gamerule advance_time false"); return 1; })));

                grNode.then(ClientCommands.literal("wtr")
                        .then(ClientCommands.literal("true").executes(context -> { executeCommand("gamerule advance_weather true"); return 1; }))
                        .then(ClientCommands.literal("t").executes(context -> { executeCommand("gamerule advance_weather true"); return 1; }))
                        .then(ClientCommands.literal("false").executes(context -> { executeCommand("gamerule advance_weather false"); return 1; }))
                        .then(ClientCommands.literal("f").executes(context -> { executeCommand("gamerule advance_weather false"); return 1; })));

                grNode.then(ClientCommands.literal("rts")
                        .then(ClientCommands.argument("value", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    int val = IntegerArgumentType.getInteger(context, "value");
                                    executeCommand("gamerule random_tick_speed " + val);
                                    return 1;
                                })));

                grNode.then(ClientCommands.literal("pvp")
                        .then(ClientCommands.literal("true").executes(context -> { executeCommand("gamerule pvp true"); return 1; }))
                        .then(ClientCommands.literal("t").executes(context -> { executeCommand("gamerule pvp true"); return 1; }))
                        .then(ClientCommands.literal("false").executes(context -> { executeCommand("gamerule pvp false"); return 1; }))
                        .then(ClientCommands.literal("f").executes(context -> { executeCommand("gamerule pvp false"); return 1; })));

                grNode.then(ClientCommands.literal("grief")
                        .then(ClientCommands.literal("true").executes(context -> { executeCommand("gamerule mob_griefing true"); return 1; }))
                        .then(ClientCommands.literal("t").executes(context -> { executeCommand("gamerule mob_griefing true"); return 1; }))
                        .then(ClientCommands.literal("false").executes(context -> { executeCommand("gamerule mob_griefing false"); return 1; }))
                        .then(ClientCommands.literal("f").executes(context -> { executeCommand("gamerule mob_griefing false"); return 1; })));

                dispatcher.register(grNode);
            }

            // Night Vision (/nv)
            if (config.enableNightVisionCommand) {
                dispatcher.register(ClientCommands.literal("nv")
                        .executes(context -> {
                            executeCommand("effect give @s minecraft:night_vision infinite");
                            return 1;
                        }));
            }

            // Effect Clear (/eclear)
            if (config.enableEffectClearCommand) {
                dispatcher.register(ClientCommands.literal("eclear")
                        .executes(context -> {
                            executeCommand("effect clear @s");
                            return 1;
                        }));
            }

            // TP Here (/tphere)
            if (config.enableTpHereCommand) {
                dispatcher.register(ClientCommands.literal("tphere")
                        .then(ClientCommands.argument("player", EntityArgument.player())
                                .executes(context -> {
                                    String target = context.getInput().split(" ")[1];
                                    executeCommand("tp " + target + " @s");
                                    return 1;
                                })));
            }
        });
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
