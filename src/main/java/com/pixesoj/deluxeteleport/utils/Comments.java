package com.pixesoj.deluxeteleport.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Comments {
    public static final List<String> LobbyEnabled = new ArrayList<>();
    static {
        String headerString =
                "====================================================================================#\n" +
                "                            __    _____  ____  ____  _  _                           #\n" +
                "                           (  )  (  _  )(  _ \\(  _ \\( \\/ )                          #\n" +
                "                            )(__  )(_)(  ) _ < ) _ < \\  /                           #\n" +
                "                           (____)(_____)(____/(____/ (__)                           #\n" +
                "                                                                                    #\n" +
                " This is the configuration file for the lobby                                       #\n" +
                " If you would like any new features or need help let me know on my discord server:  #\n" +
                " https://discord.com/invite/gcGarEbbbb                                              #\n" +
                "====================================================================================#\n" +
                " \n" +
                "Sets whether /lobby and /setlobby will be enabled.\n";

        String[] lines = headerString.split("\n");
        LobbyEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyForceDisable = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "By setting this to true, this function will be completely disabled as if it did not exist in the plugin.\n" +
                "This is useful for when you have another plugin that takes care of this. If this is not the case, do not activate it.\n" +
                "A reboot is required for it to take effect.\n";

        String[] lines = headerString.split("\n");
        LobbyForceDisable.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCommandAlias = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Here you can add alias commands in addition to /lobby\n" +
                        "Restart your server after adding or removing an alias\n";

        String[] lines = headerString.split("\n");
        LobbyCommandAlias.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyMode = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Lobby mode settings\n";

        String[] lines = headerString.split("\n");
        LobbyMode.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyModeMode = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "In this option you can choose what mode the /lobby command is in\n" +
                "Available options:\n" +
                "Server: This means that you could establish one or more lobbies on the same server\n" +
                "Proxy: This means that you will be able to send the player to another server in a BungeeCord environment\n";

        String[] lines = headerString.split("\n");
        LobbyModeMode.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbySendServers = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Server that will be used as a lobby\n";

        String[] lines = headerString.split("\n");
        LobbySendServers.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyMultiple = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "=========================================================================================#\n" +
                        "                                                                                          #\n" +
                "                                     ATTENTION                                           #\n" +
                "The following configuration will only be taken into account if lobby_mode.mode is Server #\n" +
                "                                                                                         #\n" +
                "=========================================================================================#\n" +
                " \n" +
                "If this is enabled you will be able to establish several lobbies\n";

        String[] lines = headerString.split("\n");
        LobbyMultiple.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportInMultiple = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "This is for when multiple_lobbies is true and the /lobby command is used without specifying a lobby\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportInMultiple.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportInMultipleEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Will this function be enabled? If this is not true and multiple_lobbies is true\n" +
                "then you will have to specify a lobby in the /lobby commande\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportInMultipleEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportInMultipleLobby = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Available options\n" +
                        "General: Take to the general lobby (if there is one)\n" +
                "Specify: lead to a specific lobby\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportInMultipleLobby.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportInMultipleSpecify = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "If lobby is Specify then specify the name of the lobby here\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportInMultipleSpecify.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportDelay = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Teleport delay settings\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportDelay.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportDelayEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Enable teleport delay\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportDelayEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportDelaySeconds = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "How long will the delay be in teleportation?\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportDelaySeconds.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportDelayCancelOnMove = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Should teleport be canceled with movement?\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportDelayCancelOnMove.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportDelayBlindness = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "If Blindness effect was applied when teleporting\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportDelayBlindness.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportDelayBlindnessTime = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "How many seconds will the effect last?\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportDelayBlindnessTime.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportDelayMessageType = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Select what type of message will be sent to the player at the time of teleportation,\n" +
                "the message is repetitive for every second\n" +
                "Available options:\n" +
                "Chat: will be sent in chat every second until teleportation\n" +
                "(leave blank to disable)\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportDelayMessageType.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCooldown = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Cooldown settings between each use of the /lobby command\n" +
                "Restart the server if the counter reaches 0 and nothing happens from there\n";

        String[] lines = headerString.split("\n");
        LobbyCooldown.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCooldownEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Will it be enabled?\n";

        String[] lines = headerString.split("\n");
        LobbyCooldownEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCooldownTime = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Time between each use of the command\n";

        String[] lines = headerString.split("\n");
        LobbyCooldownTime.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbySound = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Sound settings\n";

        String[] lines = headerString.split("\n");
        LobbySound.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbySoundEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Whether or not a sound would be sent when teleporting the player\n";

        String[] lines = headerString.split("\n");
        LobbySoundEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbySoundSound = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Sound that will be sent to the player\n" +
                "You can find a list of sounds at: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html\n";

        String[] lines = headerString.split("\n");
        LobbySoundSound.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbySoundVolume = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Sound volume\n";

        String[] lines = headerString.split("\n");
        LobbySoundVolume.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbySoundPitch = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Sound tone\n";

        String[] lines = headerString.split("\n");
        LobbySoundPitch.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCommands = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Here you can assign commands that are executed when teleporting the player\n" +
                "Run commands from the player or from the console, you can use the %player% variable\n";

        String[] lines = headerString.split("\n");
        LobbyCommands.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCommandsEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Enable or disable this option\n";

        String[] lines = headerString.split("\n");
        LobbyCommandsEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCommandsPlayer = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Run commands from the player\n";

        String[] lines = headerString.split("\n");
        LobbyCommandsPlayer.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCommandsConsole = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Run commands from the console\n";

        String[] lines = headerString.split("\n");
        LobbyCommandsConsole.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportOtherPlayer = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Here you can adjust which actions will also be executed when the player is teleported by another player or by the console.\n" +
                        "Attention: each option must also be activated separately\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportOtherPlayer.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportOtherPlayerBlindness = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        " Will you be blinded when teleporting?\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportOtherPlayerBlindness.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportOtherPlayerCooldown = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Will a cooldown be added to the player as if he had executed the command?\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportOtherPlayerCooldown.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportOtherPlayerCommands = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "The respective commands will be executed?\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportOtherPlayerCommands.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyTeleportOtherPlayerSound = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Will the sound be sent to the player?\n";

        String[] lines = headerString.split("\n");
        LobbyTeleportOtherPlayerSound.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyConfigVersion = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "This is the version of this configuration, this number updates itself, do not delete or change it\n";

        String[] lines = headerString.split("\n");
        LobbyConfigVersion.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCost = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Here you can charge the player something\n";

        String[] lines = headerString.split("\n");
        LobbyCost.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCostMoney = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Settings to collect money\n" +
                "Vault is needed as a dependency for this\n";

        String[] lines = headerString.split("\n");
        LobbyCostMoney.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCostMoneyEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Will it be enabled?\n";

        String[] lines = headerString.split("\n");
        LobbyCostMoneyEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCostMoneyAmount = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "How much will the player be charged?\n";

        String[] lines = headerString.split("\n");
        LobbyCostMoneyAmount.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCostMoneyIsOptional = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Is it optional to have the money?\n" +
                "If this option and enabled are true and the player does not have money, the teleport will be allowed\n" +
                "and obviously no money will be taken away but if he has money then the money will be taken away\n";

        String[] lines = headerString.split("\n");
        LobbyCostMoneyIsOptional.addAll(Arrays.asList(lines));
    }





    public static final List<String> SpawnEnabled = new ArrayList<>();
    static {
        String headerString =
                "====================================================================================#\n" +
                        "                          ___  ____   __    _    _  _  _                            #\n" +
                        "                        / __)(  _ \\ /__\\  ( \\/\\/ )( \\( )                            #\n" +
                        "                        \\__ \\ )___//(__)\\  )    (  )  (                             #\n" +
                        "                        (___/(__) (__)(__)(__/\\__)(_)\\_)                            #\n" +
                        "                                                                                    #\n" +
                        " This is the configuration file for the spawn's                                     #\n" +
                        " If you would like any new features or need help let me know on my discord server:  #\n" +
                        " https://discord.com/invite/gcGarEbbbb                                              #\n" +
                        "====================================================================================#\n" +
                        " \n" +
                        "Sets whether /spawn, /delspawn and /setspawn will be enabled.\n";

        String[] lines = headerString.split("\n");
        SpawnEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnForceDisable = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "By setting this to true, this function will be completely disabled as if it did not exist in the plugin.\n" +
                        "This is useful for when you have another plugin that takes care of this. If this is not the case, do not activate it.\n" +
                        "A reboot is required for it to take effect.\n";

        String[] lines = headerString.split("\n");
        SpawnForceDisable.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCommandAlias = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Here you can add alias commands in addition to /spawn\n" +
                        "Restart your server after adding or removing an alias\n";

        String[] lines = headerString.split("\n");
        SpawnCommandAlias.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnAlias = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Add an alias to certain worlds to display in messages\n";

        String[] lines = headerString.split("\n");
        SpawnAlias.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnByWorld = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "If this is true you can put one spawn per world\n";

        String[] lines = headerString.split("\n");
        SpawnByWorld.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportInByWorld = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "This is for when by_world is true and the /spawn command is used without specifying a spawn\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportInByWorld.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportInByWorldEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Will this function be enabled? If this is not true and by_world is true\n" +
                        "then you will have to specify a spawn in the /spawn command\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportInByWorldEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportInByWorldSpawn = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Available options\n" +
                        "General: Take to the general spawn (if there is one)\n" +
                        "Specify: lead to a specific spawn\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportInByWorldSpawn.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportInByWorldSpecify = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "If spawn is Specify then specify the name of the spawn here\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportInByWorldSpecify.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportDelay = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Teleport delay settings\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportDelay.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportDelayEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Enable teleport delay\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportDelayEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportDelaySeconds = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "How long will the delay be in teleportation?\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportDelaySeconds.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportDelayCancelOnMove = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Should teleport be canceled with movement?\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportDelayCancelOnMove.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportDelayBlindness = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "If Blindness effect was applied when teleporting\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportDelayBlindness.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportDelayBlindnessTime = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "How many seconds will the effect last?\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportDelayBlindnessTime.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportDelayMessageType = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Select what type of message will be sent to the player at the time of teleportation,\n" +
                        "the message is repetitive for every second\n" +
                        "Available options:\n" +
                        "Chat: will be sent in chat every second until teleportation\n" +
                        "(leave blank to disable)\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportDelayMessageType.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCooldown = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Cooldown settings between each use of the /spawn command\n" +
                        "Restart the server if the counter reaches 0 and nothing happens from there\n";

        String[] lines = headerString.split("\n");
        SpawnCooldown.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCooldownEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Will it be enabled?\n";

        String[] lines = headerString.split("\n");
        SpawnCooldownEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCooldownTime = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Time between each use of the command\n";

        String[] lines = headerString.split("\n");
        SpawnCooldownTime.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnSound = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Sound settings\n";

        String[] lines = headerString.split("\n");
        SpawnSound.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnSoundEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Whether or not a sound would be sent when teleporting the player\n";

        String[] lines = headerString.split("\n");
        SpawnSoundEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnSoundSound = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Sound that will be sent to the player\n" +
                        "You can find a list of sounds at: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html\n";

        String[] lines = headerString.split("\n");
        SpawnSoundSound.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnSoundVolume = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Sound volume\n";

        String[] lines = headerString.split("\n");
        SpawnSoundVolume.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnSoundPitch = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Sound tone\n";

        String[] lines = headerString.split("\n");
        SpawnSoundPitch.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCommands = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Here you can assign commands that are executed when teleporting the player\n" +
                        "Run commands from the player or from the console, you can use the %player% variable\n";

        String[] lines = headerString.split("\n");
        SpawnCommands.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCommandsEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Enable or disable this option\n";

        String[] lines = headerString.split("\n");
        SpawnCommandsEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCommandsPlayer = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Run commands from the player\n";

        String[] lines = headerString.split("\n");
        SpawnCommandsPlayer.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCommandsConsole = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Run commands from the console\n";

        String[] lines = headerString.split("\n");
        SpawnCommandsConsole.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportOtherPlayer = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Here you can adjust which actions will also be executed when the player is teleported by another player or by the console.\n" +
                        "Attention: each option must also be activated separately\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportOtherPlayer.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportOtherPlayerBlindness = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        " Will you be blinded when teleporting?\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportOtherPlayerBlindness.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportOtherPlayerCooldown = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Will a cooldown be added to the player as if he had executed the command?\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportOtherPlayerCooldown.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportOtherPlayerCommands = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "The respective commands will be executed?\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportOtherPlayerCommands.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnTeleportOtherPlayerSound = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Will the sound be sent to the player?\n";

        String[] lines = headerString.split("\n");
        SpawnTeleportOtherPlayerSound.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnConfigVersion = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "This is the version of this configuration, this number updates itself, do not delete or change it\n";

        String[] lines = headerString.split("\n");
        SpawnConfigVersion.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCost = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Here you can charge the player something\n";

        String[] lines = headerString.split("\n");
        SpawnCost.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCostMoney = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Settings to collect money\n" +
                        "Vault is needed as a dependency for this\n";

        String[] lines = headerString.split("\n");
        SpawnCostMoney.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCostMoneyEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Will it be enabled?\n";

        String[] lines = headerString.split("\n");
        SpawnCostMoneyEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCostMoneyAmount = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "How much will the player be charged?\n";

        String[] lines = headerString.split("\n");
        SpawnCostMoneyAmount.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnCostMoneyIsOptional = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Is it optional to have the money?\n" +
                        "If this option and enabled are true and the player does not have money, the teleport will be allowed\n" +
                        "and obviously no money will be taken away but if he has money then the money will be taken away\n";

        String[] lines = headerString.split("\n");
        SpawnCostMoneyIsOptional.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnMenu = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Menu setting\n";

        String[] lines = headerString.split("\n");
        SpawnMenu.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnMenuAdmin = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Administration menu\n";

        String[] lines = headerString.split("\n");
        SpawnMenuAdmin.addAll(Arrays.asList(lines));
    }

    public static final List<String> SpawnMenuAdminEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Will the administration menu be enabled?\n" +
                "ATTENTION: this menu is still under development and may not be complete\n" +
                "Use command: /spawn menu\n";

        String[] lines = headerString.split("\n");
        SpawnMenuAdminEnabled.addAll(Arrays.asList(lines));
    }


    public static final List<String> TPAEnabled = new ArrayList<>();
    static {
        String headerString =
                "=====================================================================================#\n" +
                        "                                    ____  ____   __                                  #\n" +
                        "                                   (_  _)(  _ \\ /__\\                                 #\n" +
                        "                                     )(   )___//(__)\\                                #\n" +
                        "                                    (__) (__) (__)(__)                               #\n" +
                        "                                                                                     #\n" +
                        "  This is the configuration file for the tpa                                         #\n" +
                        "  If you would like any new features or need help let me know on my discord server:  #\n" +
                        "  https://discord.com/invite/gcGarEbbbb                                              #\n" +
                        "=====================================================================================#\n" +
                        " \n" +
                        "Sets whether /tpa, /tpacancel, /tpaccept, /tpahere and /tpatoggle will be enabled.\n";

        String[] lines = headerString.split("\n");
        TPAEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPAForceDisable = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "By setting this to true, this function will be completely disabled as if it did not exist in the plugin.\n" +
                        "This is useful for when you have another plugin that takes care of this. If this is not the case, do not activate it.\n" +
                        "A reboot is required for it to take effect.\n";

        String[] lines = headerString.split("\n");
        TPAForceDisable.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPACommandsAlias = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Here you can add alias commands in addition to the original ones\n" +
                        "Restart your server after adding or removing an alias\n";

        String[] lines = headerString.split("\n");
        TPACommandsAlias.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPAStatusDefault = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "This will indicate if by default the player will have teleport requests enabled (this changes after using /tpatoggle)\n";

        String[] lines = headerString.split("\n");
        TPAStatusDefault.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPAListPosition = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "If a player is not specified in the /tpaccept, /tpahere and /tpacancel commands, which player will be taken into account\n" +
                "Available options:\n" +
                "First: select the first player who sent a teleport request\n" +
                "Last: select the last player who sent a teleport request\n" +
                "None: this means that a player must necessarily be specified\n";

        String[] lines = headerString.split("\n");
        TPAListPosition.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPASend = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Settings for when the /tpa command is used\n";

        String[] lines = headerString.split("\n");
        TPASend.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPASendHimSelf = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "If this is activated, players will be able to send themselves (I do not recommend it)\n";

        String[] lines = headerString.split("\n");
        TPASendHimSelf.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPASendIgnoreStatus = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "If this is active, it will not be taken into account if the player has tpa requests disabled\n";

        String[] lines = headerString.split("\n");
        TPASendIgnoreStatus.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPAMultipleTPA = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "If this is enabled, players may receive more than one teleport request.\n";

        String[] lines = headerString.split("\n");
        TPAMultipleTPA.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPARequest = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Settings for when a player receives a teleport request\n";

        String[] lines = headerString.split("\n");
        TPARequest.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPARequestClickTPA = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "If this is active, a clickable message will be sent to the player to accept or reject\n";

        String[] lines = headerString.split("\n");
        TPARequestClickTPA.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPACenteredMessage = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "This will attempt to focus the teleport request message.\n";

        String[] lines = headerString.split("\n");
        TPACenteredMessage.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPAExpiration = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Here you can configure whether the teleport requests and the time will expire.\n";

        String[] lines = headerString.split("\n");
        TPAExpiration.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPAExpirationEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Will it be enabled?\n";

        String[] lines = headerString.split("\n");
        TPAExpirationEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPAExpirationTime = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "How long will it take to expire (in seconds)\n";

        String[] lines = headerString.split("\n");
        TPAExpirationTime.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPADelay = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Teleport delay settings (This only applies to the player who will be teleported)\n";

        String[] lines = headerString.split("\n");
        TPADelay.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPADelayEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Will the delay be enabled?\n";

        String[] lines = headerString.split("\n");
        TPADelayEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPADelayTime = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Time between acceptance and teleportation\n";

        String[] lines = headerString.split("\n");
        TPADelayTime.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPADelayCancelOnMove = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Should teleport be canceled with movement?\n";

        String[] lines = headerString.split("\n");
        TPADelayCancelOnMove.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPASound = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Sound settings\n";

        String[] lines = headerString.split("\n");
        TPASound.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPASoundEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Whether or not a sound would be sent when teleporting the player\n";

        String[] lines = headerString.split("\n");
        TPASoundEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPASoundSound = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Sound that will be sent to the player\n" +
                        "You can find a list of sounds at: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html\n";

        String[] lines = headerString.split("\n");
        TPASoundSound.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPASoundVolume = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Sound volume\n";

        String[] lines = headerString.split("\n");
        TPASoundVolume.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPASoundPitch = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Sound tone\n";

        String[] lines = headerString.split("\n");
        TPASoundPitch.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPASoundSendTo = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                "Who will this sound be sent to?\n" +
                "Available options:\n" +
                "Player: is the player who will be teleported\n" +
                "TargetPlayer: is the player to whom you will teleport\n" +
                "Both: send to both\n";

        String[] lines = headerString.split("\n");
        TPASoundSendTo.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPACommands = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Here you can assign commands that are executed when teleporting the player\n" +
                        "Run commands from the player or from the console, available variables:\n" +
                "%player%: this is the player who will be teleported\n" +
                "%target_player%: is the player to whom you will teleport\n";

        String[] lines = headerString.split("\n");
        TPACommands.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPACommandsEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Enable or disable this option\n";

        String[] lines = headerString.split("\n");
        TPACommandsEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPACommandsPlayer = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Run commands from the player\n";

        String[] lines = headerString.split("\n");
        TPACommandsPlayer.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPACommandsTargetPlayer = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Run commands from the target player\n";

        String[] lines = headerString.split("\n");
        TPACommandsTargetPlayer.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPACommandsConsole = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Run commands from the console\n";

        String[] lines = headerString.split("\n");
        TPACommandsConsole.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPAGeyserSupport = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Support for bedrock edition, you must have Floodgate and Geyser installed\n";

        String[] lines = headerString.split("\n");
        TPAGeyserSupport.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPAGeyserSupportEnabled = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "You can activate this option if you use Geyser\n";

        String[] lines = headerString.split("\n");
        TPAGeyserSupportEnabled.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPAGeyserSupportClickTPA = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "Since clickable messages are not supported in bedrock edition, this function will prevent the buttons from being sent.\n";

        String[] lines = headerString.split("\n");
        TPAGeyserSupportClickTPA.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPAGeyserSupportDifferetntMessage = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "If this is active then a different message will be sent when they receive a teleport request\n";

        String[] lines = headerString.split("\n");
        TPAGeyserSupportDifferetntMessage.addAll(Arrays.asList(lines));
    }

    public static final List<String> TPAConfigVersion = new ArrayList<>();
    static {
        String headerString =
                " \n" +
                        "This is the version of this configuration, this number updates itself, do not delete or change it\n";

        String[] lines = headerString.split("\n");
        TPAConfigVersion.addAll(Arrays.asList(lines));
    }
}
