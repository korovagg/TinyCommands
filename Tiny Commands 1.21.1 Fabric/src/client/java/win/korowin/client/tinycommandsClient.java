package win.korowin.client;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import win.korowin.tinycommands;
import win.korowin.config.TinyCommandsConfig;

public class tinycommandsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            TinyCommandsConfig config = tinycommands.getConfig();

            // Time commands
            if (config.enableTimeCommands) {
                dispatcher.register(ClientCommandManager.literal("day").executes(context -> { executeCommand("time set day"); return 1; }));
                dispatcher.register(ClientCommandManager.literal("night").executes(context -> { executeCommand("time set night"); return 1; }));
                dispatcher.register(ClientCommandManager.literal("noon").executes(context -> { executeCommand("time set noon"); return 1; }));
                dispatcher.register(ClientCommandManager.literal("midnight").executes(context -> { executeCommand("time set midnight"); return 1; }));
            }

            // Weather commands
            if (config.enableWeatherCommands) {
                dispatcher.register(ClientCommandManager.literal("sun").executes(context -> { executeCommand("weather clear"); return 1; }));
                dispatcher.register(ClientCommandManager.literal("rain").executes(context -> { executeCommand("weather rain"); return 1; }));
                dispatcher.register(ClientCommandManager.literal("thunder").executes(context -> { executeCommand("weather thunder"); return 1; }));

                dispatcher.register(ClientCommandManager.literal("wtr")
                        .then(ClientCommandManager.literal("clear").executes(context -> { executeCommand("weather clear"); return 1; }))
                        .then(ClientCommandManager.literal("c").executes(context -> { executeCommand("weather clear"); return 1; }))
                        .then(ClientCommandManager.literal("rain").executes(context -> { executeCommand("weather rain"); return 1; }))
                        .then(ClientCommandManager.literal("r").executes(context -> { executeCommand("weather rain"); return 1; }))
                        .then(ClientCommandManager.literal("thunder").executes(context -> { executeCommand("weather thunder"); return 1; }))
                        .then(ClientCommandManager.literal("t").executes(context -> { executeCommand("weather thunder"); return 1; })));
            }

            // Gamemode commands
            if (config.enableGamemodeCommands) {
                dispatcher.register(ClientCommandManager.literal("gm")
                        .then(ClientCommandManager.literal("0").executes(context -> { executeCommand("gamemode survival"); return 1; }))
                        .then(ClientCommandManager.literal("1").executes(context -> { executeCommand("gamemode creative"); return 1; }))
                        .then(ClientCommandManager.literal("2").executes(context -> { executeCommand("gamemode adventure"); return 1; }))
                        .then(ClientCommandManager.literal("3").executes(context -> { executeCommand("gamemode spectator"); return 1; })));
            }

            // Difficulty commands
            if (config.enableDifficultyCommands) {
                dispatcher.register(ClientCommandManager.literal("diff")
                        .then(ClientCommandManager.literal("peaceful").executes(context -> { executeCommand("difficulty peaceful"); return 1; }))
                        .then(ClientCommandManager.literal("p").executes(context -> { executeCommand("difficulty peaceful"); return 1; }))
                        .then(ClientCommandManager.literal("easy").executes(context -> { executeCommand("difficulty easy"); return 1; }))
                        .then(ClientCommandManager.literal("e").executes(context -> { executeCommand("difficulty easy"); return 1; }))
                        .then(ClientCommandManager.literal("normal").executes(context -> { executeCommand("difficulty normal"); return 1; }))
                        .then(ClientCommandManager.literal("n").executes(context -> { executeCommand("difficulty normal"); return 1; }))
                        .then(ClientCommandManager.literal("hard").executes(context -> { executeCommand("difficulty hard"); return 1; }))
                        .then(ClientCommandManager.literal("h").executes(context -> { executeCommand("difficulty hard"); return 1; })));
            }

            // Spawnpoint command
            if (config.enableSpawnpointCommand) {
                dispatcher.register(ClientCommandManager.literal("sp").executes(context -> { executeCommand("spawnpoint"); return 1; }));
            }

            // Heal/Feed commands
            if (config.enableHealFeedCommands) {
                dispatcher.register(ClientCommandManager.literal("heal").executes(context -> { executeCommand("effect give @s minecraft:instant_health 1 10"); return 1; }));
                dispatcher.register(ClientCommandManager.literal("feed").executes(context -> { executeCommand("effect give @s minecraft:saturation 1 255"); return 1; }));
            }

            // Gamerule command (/gr)
            if (config.enableGameruleCommand) {
                var grNode = ClientCommandManager.literal("gr");
                
                grNode.then(ClientCommandManager.literal("keepinv")
                        .then(ClientCommandManager.literal("true").executes(context -> { executeCommand("gamerule keepInventory true"); return 1; }))
                        .then(ClientCommandManager.literal("t").executes(context -> { executeCommand("gamerule keepInventory true"); return 1; }))
                        .then(ClientCommandManager.literal("false").executes(context -> { executeCommand("gamerule keepInventory false"); return 1; }))
                        .then(ClientCommandManager.literal("f").executes(context -> { executeCommand("gamerule keepInventory false"); return 1; })));

                grNode.then(ClientCommandManager.literal("day")
                        .then(ClientCommandManager.literal("true").executes(context -> { executeCommand("gamerule doDaylightCycle true"); return 1; }))
                        .then(ClientCommandManager.literal("t").executes(context -> { executeCommand("gamerule doDaylightCycle true"); return 1; }))
                        .then(ClientCommandManager.literal("false").executes(context -> { executeCommand("gamerule doDaylightCycle false"); return 1; }))
                        .then(ClientCommandManager.literal("f").executes(context -> { executeCommand("gamerule doDaylightCycle false"); return 1; })));

                grNode.then(ClientCommandManager.literal("wtr")
                        .then(ClientCommandManager.literal("true").executes(context -> { executeCommand("gamerule doWeatherCycle true"); return 1; }))
                        .then(ClientCommandManager.literal("t").executes(context -> { executeCommand("gamerule doWeatherCycle true"); return 1; }))
                        .then(ClientCommandManager.literal("false").executes(context -> { executeCommand("gamerule doWeatherCycle false"); return 1; }))
                        .then(ClientCommandManager.literal("f").executes(context -> { executeCommand("gamerule doWeatherCycle false"); return 1; })));

                grNode.then(ClientCommandManager.literal("rts")
                        .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    int val = IntegerArgumentType.getInteger(context, "value");
                                    executeCommand("gamerule randomTickSpeed " + val);
                                    return 1;
                                })));

                grNode.then(ClientCommandManager.literal("pvp")
                        .then(ClientCommandManager.literal("true").executes(context -> { executeCommand("gamerule pvp true"); return 1; }))
                        .then(ClientCommandManager.literal("t").executes(context -> { executeCommand("gamerule pvp true"); return 1; }))
                        .then(ClientCommandManager.literal("false").executes(context -> { executeCommand("gamerule pvp false"); return 1; }))
                        .then(ClientCommandManager.literal("f").executes(context -> { executeCommand("gamerule pvp false"); return 1; })));

                grNode.then(ClientCommandManager.literal("grief")
                        .then(ClientCommandManager.literal("true").executes(context -> { executeCommand("gamerule mobGriefing true"); return 1; }))
                        .then(ClientCommandManager.literal("t").executes(context -> { executeCommand("gamerule mobGriefing true"); return 1; }))
                        .then(ClientCommandManager.literal("false").executes(context -> { executeCommand("gamerule mobGriefing false"); return 1; }))
                        .then(ClientCommandManager.literal("f").executes(context -> { executeCommand("gamerule mobGriefing false"); return 1; })));

                dispatcher.register(grNode);
            }

            // Night Vision (/nv)
            if (config.enableNightVisionCommand) {
                dispatcher.register(ClientCommandManager.literal("nv")
                        .executes(context -> {
                            executeCommand("effect give @s minecraft:night_vision infinite");
                            return 1;
                        }));
            }

            // Effect Clear (/eclear)
            if (config.enableEffectClearCommand) {
                dispatcher.register(ClientCommandManager.literal("eclear")
                        .executes(context -> {
                            executeCommand("effect clear @s");
                            return 1;
                        }));
            }

            // TP Here (/tphere)
            if (config.enableTpHereCommand) {
                dispatcher.register(ClientCommandManager.literal("tphere")
                        .then(ClientCommandManager.argument("player", StringArgumentType.word())
                                .executes(context -> {
                                    String target = StringArgumentType.getString(context, "player");
                                    executeCommand("tp " + target + " @s");
                                    return 1;
                                })));
            }
        });
    }

    private static void executeCommand(String command) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendCommand(command);
        }
    }
}
