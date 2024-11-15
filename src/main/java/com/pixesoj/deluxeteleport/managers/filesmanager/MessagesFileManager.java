package com.pixesoj.deluxeteleport.managers.filesmanager;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.utils.OtherUtils;
import com.pixesoj.deluxeteleport.utils.ServerInfo;
import com.pixesoj.deluxeteleport.utils.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class MessagesFileManager {
    private FileManager messageFile;
    private final DeluxeTeleport plugin;
    MessagesManager m;

    private String Prefix;
    private String PrefixLobby;
    private String PrefixSpawn;
    private String PrefixTPA;
    private String PrefixHome;
    private String PrefixWarp;


    private String GlobalInvalidArguments;
    private String GlobalPlayerOffline;
    private String GlobalNotExecuteInProxy;
    private String GlobalDependencyFound;
    private String GlobalPermissionDenied;
    private String GlobalConsoleDenied;
    private String GlobalServerNotExists;
    private List<String> GlobalHelp;
    private String GlobalReload;
    private String GlobalUsage;
    private String GlobalInTeleport;
    private String GlobalUpdatedConfig;
    private String GlobalUpdatingConfig;


    private String MySQLErrorConnecting;
    private String MySQLError;
    private String MySQLConnecting;
    private String MySQLErrorTables;


    private String UpdateErrorCheck;
    private String UpdateAvailable;
    private String UpdateItsUpdated;
    private String UpdateJarNotFound;
    private String UpdateDownloaded;
    private String UpdateUnableToDeleted;
    private String UpdateResponseCode;
    private String UpdateLooking;
    private List<String> UpdateNewUpdate;
    private String UpdateChangelogInvalidFormat;
    private String UpdateChangelogDisabledNotify;
    private String UpdateChangelogEnabledNotify;
    private String UpdateChangelogInvalidPage;
    private String UpdateChangelogNoChangelogsFound;
    private String UpdateChangelogErrorInfo;
    private String UpdateChangelogExeption;
    private String UpdateChangelogPages;
    private String UpdateChangelogPagesConsole;
    private String UpdateChangelogRegister;
    private String UpdateChangelogSeeFull;
    private String UpdateChangelogSeeFullDescription;
    private String UpdateChangelogOk;
    private String UpdateChangelogOkDescription;
    private String UpdateChangelogNext;
    private String UpdateChangelogNextDescription;
    private String UpdateChangelogPrevius;
    private String UpdateChangelogPreviusDescription;


    private String LobbyInvalidSpecified;
    private String LobbyOtherTeleported;
    private String LobbyOtherTeleport;
    private String LobbyInvalidMode;
    private String LobbyInCooldown;
    private String LobbyTeleporting;
    private String LobbyNotEnabled;
    private String LobbyEstablished;
    private String LobbyNotExists;
    private String LobbyInTeleport;
    private String LobbyDelayInTeleport;
    private String LobbyCanceledMove;
    private String LobbyDeletedSuccessfully;


    private String SpawnNotEnabled;
    private String SpawnNotExists;
    private String SpawnOtherTeleported;
    private String SpawnOtherTeleport;
    private String SpawnInCooldown;
    private String SpawnTeleporting;
    private String SpawnCanceledMove;
    private String SpawnDelayInTeleport;
    private String SpawnInTeleport;
    private String SpawnDeletedSuccessfully;
    private String SpawnEstablished;
    private String SpawnDeletedError;
    private String SpawnNoSpawns;
    private String SpawnSpawnsList;
    private String SpawnExeption;

    private String TPANotEnabled;
    private List<String> TPARequest;
    private List<String> TPAGeyserRequest;
    private String TPAClickAccept;
    private String TPAClickCancel;
    private String TPAClickAcceptDescription;
    private String TPAClickCancelDescription;
    private String TPASpecifyPlayer;
    private String TPAHimself;
    private String TPABlocked;
    private String TPAPending;
    private String TPASend;
    private String TPATeleportDefeated;
    private String TPATeleportDefeatedTargetPlayer;
    private String TPAToggleYes;
    private String TPAToggleNo;
    private String TPAToggleOther;
    private String TPAToggleOtherTargetPlayer;
    private String TPAPendingRequest;
    private String TPADeny;
    private String TPADenyTargetPlayer;
    private String TPANoRequest;
    private String TPANoRequestPlayer;
    private String TPADelayInTeleport;
    private String TPATeleporting;
    private String TPACanceledMoving;
    private String TPATeleport;
    private String TPATeleportTargetPlayer;
    private List<String> TPAHereRequest;
    private List<String> TPAHereGeyserRequest;
    private String TPAInCooldown;
    private String TPACancel;
    private String TPACancelTargetPlayer;
    private String TPANoRequestSent;
    private String TPANoRequestSentPlayer;
    private String TPAPlayerInTeleport;
    private String TPATargetPlayerInTeleport;


    private String VariablesEnabled;
    private String VariablesDisabled;
    private String VariablesUnlimited;
    private String VariablesSeconds;
    private String VariablesMinutes;
    private String VariablesHours;
    private String VariablesDays;


    private String HomeNotEnabled;
    private String HomeDelayInTeleport;
    private String HomeTeleporting;
    private String HomeCanceledMove;
    private String HomeInCooldown;
    private String HomeNotSpecify;
    private String HomeNotExist;
    private String HomeDeletedError;
    private String HomeDeletedSuccessfully;
    private String HomeSetError;
    private String HomeExists;
    private String HomeMaxCount;
    private String HomeSetSuccessfully;
    private String HomeHasNoHomes;
    private String HomeNoHomes;
    private String HomeHomesOf;
    private String HomeYourHomes;


    private String MigrateErrorPluginNotSpecified;
    private String MigrateErrorInvalidPlugin;
    private String MigrateErrorDataNotSpecified;
    private String MigrateErrorInvalidData;


    private String ResetErrorUnspecifiedValue;
    private String ResetErrorInvalidValue;
    private String ResetUnspecifiedPlayer;
    private String ResetSuccessfully;

    private String WarpNotEnabled;
    private String WarpNotExist;
    private String WarpDeletedError;
    private String WarpDeletedSuccessfully;
    private String WarpTeleporting;
    private String WarpSetError;
    private String WarpSetSuccessfully;
    private String WarpDelayInTeleport;
    private String WarpCanceledMove;
    private String WarpInCooldown;
    private String WarpNotSpecify;
    private String WarpExeption;
    private String WarpOtherTeleported;
    private String WarpOtherTeleport;
    private String WarpNoWarps;
    private String WarpWarpsList;


    public void reloadMessages(){
        messageFile.reloadConfig();
        loadMessages();
    }

    public void saveMessages(){
        messageFile.saveConfig();
        loadMessages();
    }

    public FileConfiguration getMessages(){
        return messageFile.getConfig();
    }

    public MessagesFileManager(DeluxeTeleport plugin){
        this.plugin = plugin;
        this.m = new com.pixesoj.deluxeteleport.managers.MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        messageFile = new FileManager("messages.yml", "lang/en-EN", plugin);
        loadMessages();
    }

    private <T> boolean setMessage(boolean changed, String path, T value) {
        FileConfiguration config = this.getMessages();
        if (!config.contains(path)){
            config.set(path, value);
            changed = true;
        }
        return changed;
    }

    public void updateMessages() {
        FileConfiguration config = getMessages();
        boolean changed = addMissingFields(config, config);
        String lang = plugin.getMainConfigManager().getConfig().getString("lang", "en-EN");

        changed = setMessage(changed, "messages_version", plugin.version);
        changed = setMessage(changed, "prefix.global", "&bDeluxeTeleport &8» ");
        changed = setMessage(changed, "prefix.lobby", "&bLobby &8» ");
        changed = setMessage(changed, "prefix.spawn", "&bSpawn &8» ");
        changed = setMessage(changed, "prefix.tpa", "&bTPA &8» ");
        changed = setMessage(changed, "prefix.home", "&bHome &8» ");
        changed = setMessage(changed, "prefix.warps", "&bWarps &8» ");

        if (lang.equalsIgnoreCase("es-ES")){
            changed = setMessage(changed, "variables.enabled", "&aHabilitado");
            changed = setMessage(changed, "variables.disabled", "&cDeshabilitado");
            changed = setMessage(changed, "variables.unlimited", "&aIlimitado");
            changed = setMessage(changed, "variables.seconds", "s");
            changed = setMessage(changed, "variables.minutes", "m");
            changed = setMessage(changed, "variables.hours", "h");
            changed = setMessage(changed, "variables.days", "d");

            changed = setMessage(changed, "global.player_offline", "&cEl jugador &b%player% &cno está en línea.");
            changed = setMessage(changed, "global.invalid_arguments", "&cArgumentos inválidos, modo de uso: &a%usage%");
            changed = setMessage(changed, "global.not_executed_in_proxy", "&cEl servidor no está siendo ejecutado en un proxy.");
            changed = setMessage(changed, "global.in_cooldown", "&cDebes esperar &b%time% &csegundos antes de volver a usar este comando.");
            changed = setMessage(changed, "global.dependency_found", "&aDependencia encontrada: &b%dependency%");
            changed = setMessage(changed, "global.unknown_software", "Software del servidor desconocido.");
            changed = setMessage(changed, "global.permission_denied", "&cNecesitas el permiso &b%permission% &cpara hacer eso.");
            changed = setMessage(changed, "global.console_denied", "&cEste comando solo puede ser ejecutado por un jugador.");
            changed = setMessage(changed, "global.null_sound", "&cEl sonido no puede estar vacío.");
            changed = setMessage(changed, "global.invalid_sound", "&6%sound% &cno es un sonido válido.");
            changed = setMessage(changed, "global.server_not_exists", "&cEl servidor &b%server% no existe.");
            changed = setMessage(changed, "global.reload", "&aTodos los archivos de configuración recargados en la versión &aV%version%. &8(&7%time%&8)");
            changed = setMessage(changed, "global.version", "&fLa versión de &bDeluxeTeleport &fes &aV%version% &8(&7La última versión es &a%last_version%&8).");
            changed = setMessage(changed, "global.usage", "&7Usa &a/dt help &7para obtener ayuda.");
            changed = setMessage(changed, "global.specify_player", "&cDebes especificar un jugador.");
            changed = setMessage(changed, "global.in_teleport", "&cYa estás siendo teletransportado.");
            changed = setMessage(changed, "global.updated_config", "&a¡Hecho! ¡Configuración &b%config% &aactualizada!");
            changed = setMessage(changed, "global.updating_config", "&aActualizando &b%config% &aa la última versión...");
            changed = setMessage(changed, "global.restart_error_arguments_0", "&cDebes especificar un tipo de tiempo para restablecer &8(&7%types%&8)");
            changed = setMessage(changed, "global.restart_error_arguments_1", "&c%type% no es un tipo de tiempo válido");
            changed = setMessage(changed, "global.restart_invalid_arguments", "&cDebes especificar un jugador para restablecer el tiempo &8(&7%type%&8)");
            changed = setMessage(changed, "global.restart_successfully", "&aRestableciendo %type% para %player%...");

            List<String> globalHelp = getStrings();
            changed = setMessage(changed, "global.help", globalHelp);


            changed = setMessage(changed, "migrate.error_plugin_not_specified", "&cDebes especificar de qué plugin importar los datos. &aPlugins compatibles: &7%compatible_plugins%");
            changed = setMessage(changed, "migrate.error_invalid_plugin", "&c%plugin% no es un plugin compatible. &aPlugins compatibles: &7%compatible_plugins%");
            changed = setMessage(changed, "migrate.error_data_not_specified", "&cDebes especificar qué tipo de datos importar. &aDatos compatibles: &7%compatible_data%");
            changed = setMessage(changed, "migrate.error_invalid_data", "&c%data% no es un tipo de dato compatible. &aDatos compatibles: &7%compatible_data%");


            changed = setMessage(changed, "reset.error_unspecified_value", "&cDebes especificar un valor para restablecer. &aValores disponibles: &7%available_values%");
            changed = setMessage(changed, "reset.error_invalid_value", "&c%value% no es un valor válido. &aValores disponibles: &7%available_values%");
            changed = setMessage(changed, "reset.unspecified_player", "&cDebes especificar un jugador para restablecer el valor %value%");
            changed = setMessage(changed, "reset.successfully", "&aRestableciendo %value% para %player%...");


            changed = setMessage(changed, "mysql.error_connecting", "&cError al conectarse a MySQL: la conexión ya está abierta.");
            changed = setMessage(changed, "mysql.error_tables", "&cError al comprobar o crear tablas en MySQL.");
            changed = setMessage(changed, "mysql.successfully_tables", "&aTablas creadas exitosamente.");
            changed = setMessage(changed, "mysql.error", "&aError en la conexión MySQL.");
            changed = setMessage(changed, "mysql.connected", "&aDeluxeTeleport conectado a MySQL.");


            changed = setMessage(changed, "update.error_check", "&cNo se pudo buscar actualizaciones: %error%.");
            changed = setMessage(changed, "update.is_empty", "&cLa última versión está vacía.");
            changed = setMessage(changed, "update.update_available", "&a¡Hay una actualización disponible! &8(&b%latest_version%&8).");
            changed = setMessage(changed, "update.its_updated", "&a¡Estás utilizando la versión más reciente! &8(&b%latest_version%&8).");
            changed = setMessage(changed, "update.jar_not_found", "&c¡No se pudo encontrar el archivo jar anterior del plugin! Asegúrate de que se llame: &b%jar_name%.");
            changed = setMessage(changed, "update.downloaded", "&aDescargando la última actualización...");
            changed = setMessage(changed, "update.unable_to_deleted", "&c¡No se puede eliminar el plugin antiguo! Debes eliminarlo manualmente antes de reiniciar.");
            changed = setMessage(changed, "update.response_code", "&cEl código de respuesta fue: &b%code%.");
            changed = setMessage(changed, "update.looking", "&bBuscando actualizaciones...");

            List<String> newUpdateMessage = new ArrayList<>();
            newUpdateMessage.add("&cHay una nueva versión disponible. &e(&7%last_version%&e)");
            newUpdateMessage.add("&cUsa &a/deluxeteleport update &cpara actualizar");
            newUpdateMessage.add("&co puedes descargarlo en: &f%plugin_url%");
            changed = setMessage(changed, "update.new_update", newUpdateMessage);
            changed = setMessage(changed, "update.changelog_invalid_format", "&cFormato de versión inválido. Usa el formato x.x.x (por ejemplo, 2.1.0)." );
            changed = setMessage(changed, "update.changelog_disabled_notify", "&cHas desactivado las notificaciones de changelog para la versión %version%");
            changed = setMessage(changed, "update.changelog_enabled_notify", "&aHas activado las notificaciones de changelog para la versión %version%");
            changed = setMessage(changed, "update.changelog_invalid_page", "&cNúmero de página inválido. Mostrando la página 1.");
            changed = setMessage(changed, "update.changelog_no_changelogs_found", "&cNo se encontró un registro para la versión &b%version%.");
            changed = setMessage(changed, "update.changelog_error_info", "&cError al obtener la información de la versión %version%. Inténtalo más tarde.");
            changed = setMessage(changed, "update.changelog_exeption", "&cError al procesar la solicitud.");
            changed = setMessage(changed, "update.changelog_pages", "&7Página: %page_number%&8/&7%total_pages%");
            changed = setMessage(changed, "update.changelog_pages_console", "&7Página: %page_number%&8/&7%total_pages% &8(&7/deluxeteleport changelog page&8)");
            changed = setMessage(changed, "update.changelog_register", "&aChangelog de la versión &b%version%&a:");
            changed = setMessage(changed, "update.changelog_see_full", "&b&lVER COMPLETO");
            changed = setMessage(changed, "update.changelog_see_full_description", "&bHaz clic para ver el changelog completo.");
            changed = setMessage(changed, "update.changelog_ok", "&e&lENTENDIDO");
            changed = setMessage(changed, "update.changelog_ok_description", "&eNo volver a mostrar este mensaje para esta versión.");
            changed = setMessage(changed, "update.changelog_next", "&a&lSIGUIENTE");
            changed = setMessage(changed, "update.changelog_next_description", "&aHaz clic para ver la siguiente página.");
            changed = setMessage(changed, "update.changelog_previus", "&c&lANTERIOR");
            changed = setMessage(changed, "update.changelog_previus_description", "&cHaz clic para ver la página anterior.");



            changed = setMessage(changed, "lobby.not_allowed", "&cEl lobby no está habilitado.");
            changed = setMessage(changed, "lobby.invalid_type", "&cEl tipo de lobby especificado &8(&7%type%&8) &cen lobby.yml no es válido.");
            changed = setMessage(changed, "lobby.invalid_mode", "&cEl modo de lobby &b%mode% &cno es válido. Usa &b%modes%.");
            changed = setMessage(changed, "lobby.other_teleported", "&aHas teletransportado a &b%player% &aal lobby.");
            changed = setMessage(changed, "lobby.other_teleport", "&aHas sido teletransportado al lobby por &b%sender%.");
            changed = setMessage(changed, "lobby.in_cooldown", "&cDebes esperar &b%time% &csegundos antes de volver al lobby.");
            changed = setMessage(changed, "lobby.insufficient_money", "&cNecesitas &a$%money%/%current_money% &cpara teletransportarte.");
            changed = setMessage(changed, "lobby.discounted_money", "&fSe han descontado &a$%money% &fde tu saldo.");
            changed = setMessage(changed, "lobby.teleporting", "&aTeletransportándote al lobby...");
            changed = setMessage(changed, "lobby.established", "&aLobby &b%lobby% &aestablecido correctamente en el mundo &b%world%.");
            changed = setMessage(changed, "lobby.not_exists", "&cEl lobby &b%lobby% &cno existe.");
            changed = setMessage(changed, "lobby.in_teleport", "&cYa estás siendo teletransportado al lobby.");
            changed = setMessage(changed, "lobby.delay_in_teleport", "&aTeletransportándote al lobby en &e%time% &asegundos.");
            changed = setMessage(changed, "lobby.canceled_move", "&cTeletransporte al lobby cancelado por movimiento.");
            changed = setMessage(changed, "lobby.deleted_canceled", "&6Tiempo de confirmación agotado. La eliminación del lobby ha sido cancelada.");
            changed = setMessage(changed, "lobby.deleted_successfully", "&aEl lobby &b%lobby% &ase ha eliminado correctamente.");


            changed = setMessage(changed, "spawn.not_allowed", "&cEl spawn no está habilitado.");
            changed = setMessage(changed, "spawn.not_exists", "&cEl spawn &b%spawn% &cno existe.");
            changed = setMessage(changed, "spawn.other_teleported", "&aHas teletransportado a &b%player% &aal spawn.");
            changed = setMessage(changed, "spawn.other_teleport", "&aHas sido teletransportado al spawn por &b%sender%.");
            changed = setMessage(changed, "spawn.in_cooldown", "&cDebes esperar &b%time% &csegundos antes de volver al spawn.");
            changed = setMessage(changed, "spawn.teleporting", "&aTeletransportándote al spawn...");
            changed = setMessage(changed, "spawn.canceled_move", "&cTeletransporte al spawn cancelado por movimiento.");
            changed = setMessage(changed, "spawn.delay_in_teleport", "&aTeletransportándote al spawn en &e%time% &asegundos.");
            changed = setMessage(changed, "spawn.in_teleport", "&cYa estás siendo teletransportado al spawn.");
            changed = setMessage(changed, "spawn.insufficient_money", "&cNecesitas &a$%money%/%current_money% &cpara teletransportarte.");
            changed = setMessage(changed, "spawn.discounted_money", "&fSe han descontado &a$%money% &fde tu saldo.");
            changed = setMessage(changed, "spawn.established", "&aSpawn establecido correctamente en el mundo &b%world%.");
            changed = setMessage(changed, "spawn.deleted_canceled", "&6Tiempo de confirmación agotado. La eliminación del spawn ha sido cancelada.");
            changed = setMessage(changed, "spawn.deleted_successfully", "&aEl spawn &b%spawn% &ase ha eliminado correctamente.");
            changed = setMessage(changed, "spawn.deleted_error", "&cDebes especificar un spawn para poder eliminarlo.");
            changed = setMessage(changed, "spawn.no_spawns", "&cNo existe ningun spawn.");
            changed = setMessage(changed, "spawn.spawns_list", "&aSpawns: &f%spawns%");
            changed = setMessage(changed, "spawn.exeption", "&cEste spawn no está correctamente establecido.");


            changed = setMessage(changed, "tpa.not_allowed", "&cEl tpa está deshabilitado en este servidor.");
            changed = setMessage(changed, "tpa.specify_player", "&cDebes especificar un jugador.");
            changed = setMessage(changed, "tpa.himself", "&cNo puedes enviarte una solicitud de tpa a ti mismo.");
            changed = setMessage(changed, "tpa.blocked", "&cEste jugador ha bloqueado las solicitudes de teletransporte.");
            changed = setMessage(changed, "tpa.pending", "&cYa has enviado una solicitud de teletransporte.");
            changed = setMessage(changed, "tpa.pending_request", "&cEste jugador aún tiene una solicitud de teletransporte pendiente.");
            changed = setMessage(changed, "tpa.send", "&aHas enviado una solicitud de teletransporte a &b%player%.");
            changed = setMessage(changed, "tpa.teleport_defeated", "&cSe ha cancelado la solicitud de teletransporte a &b%player%.");
            changed = setMessage(changed, "tpa.teleport_defeated_targetplayer", "&cLa solicitud de teletransporte ha vencido.");
            changed = setMessage(changed, "tpa.toggle_yes", "&aAhora recibes solicitudes de teletransporte.");
            changed = setMessage(changed, "tpa.toggle_no", "&cHas dejado de recibir solicitudes de teletransporte.");
            changed = setMessage(changed, "tpa.toggle_other", "&aHas &b%status% &ala recepción de solicitudes de teletransporte para &b%player%.");
            changed = setMessage(changed, "tpa.toggle_other_targetplayer", "&b%player% &aha alternado tus solicitudes de teletransporte.");
            changed = setMessage(changed, "tpa.deny", "&cHas rechazado la solicitud de teletransporte de &b%player%.");
            changed = setMessage(changed, "tpa.deny_targetplayer", "&b%player% &cha rechazado tu solicitud de teletransporte.");
            changed = setMessage(changed, "tpa.cancel", "&cHas cancelado la solicitud de teletransporte a &b%player%.");
            changed = setMessage(changed, "tpa.cancel_targetplayer", "&b%player% &cha cancelado tu solicitud de teletransporte.");
            changed = setMessage(changed, "tpa.no_requests", "&cNo tienes solicitudes de teletransporte.");
            changed = setMessage(changed, "tpa.no_requests_player", "&cNo tienes solicitudes de teletransporte de &b%player%.");
            changed = setMessage(changed, "tpa.delay_in_teleport", "&aTeletransportándote en &e%time% &asegundos.");
            changed = setMessage(changed, "tpa.teleporting", "&aTeletransportándote a %player%...");
            changed = setMessage(changed, "tpa.teleport", "&aHas sido teletransportado a &b%player%.");
            changed = setMessage(changed, "tpa.teleport_targetplayer", "&b%player% &aha sido teletransportado hacia ti.");
            changed = setMessage(changed, "tpa.canceled_move", "&cTeletransporte cancelado por movimiento.");
            changed = setMessage(changed, "tpa.in_cooldown", "&cDebes esperar &b%time% &csegundos antes de volver a enviar TPA.");
            changed = setMessage(changed, "tpa.no_request_sent", "&cNo has enviado ninguna solicitud de TPA.");
            changed = setMessage(changed, "tpa.no_request_sent_player", "&cNo has enviado ninguna solicitud de TPA a %player%.");
            changed = setMessage(changed, "tpa.currently_in_teleportation", "&cEstás actualmente en teletransporte hacia &b%player%.");
            changed = setMessage(changed, "tpa.player_in_teleport", "&cNo puedes recibir más solicitudes mientras recibes otra.");
            changed = setMessage(changed, "tpa.target_player_in_teleport", "&b%player% &cestá actualmente en teletransporte.");
            changed = setMessage(changed, "tpa.click_accept", "&a&lACEPTAR");
            changed = setMessage(changed, "tpa.click_cancel", "&c&lRECHAZAR");
            changed = setMessage(changed, "tpa.click_accept_description", "&aClick para aceptar.");
            changed = setMessage(changed, "tpa.click_cancel_description", "&cClick para rechazar.");

            List<String> tpaRequest = new ArrayList<>();
            tpaRequest.add(" ");
            tpaRequest.add("&aHas recibido una solicitud de teletransporte de &b%player%.");
            tpaRequest.add("&8[&4!&8] &c&lADVERTENCIA");
            tpaRequest.add("&7Ten cuidado, no aceptes tpa de desconocidos.");
            tpaRequest.add(" ");
            changed = setMessage(changed, "tpa.tpa_request", tpaRequest);

            List<String> tpaBedrockRequest = new ArrayList<>();
            tpaBedrockRequest.add(" ");
            tpaBedrockRequest.add("&aHas recibido una solicitud de teletransporte de &b%player%.");
            tpaBedrockRequest.add("&8[&4!&8] &c&lADVERTENCIA &8» &7Ten cuidado, no aceptes tpa de desconocidos.");
            tpaBedrockRequest.add("&7Usa &a/tpaccept &7para aceptar o &a/tpdeny &7para rechazar.");
            tpaBedrockRequest.add(" ");
            changed = setMessage(changed, "tpa.tpa_bedrock_request", tpaBedrockRequest);

            List<String> tpaHereRequest = new ArrayList<>();
            tpaHereRequest.add(" ");
            tpaHereRequest.add("&aHas recibido una solicitud para ir con &b%player%.");
            tpaHereRequest.add("&8[&4!&8] &c&lADVERTENCIA");
            tpaHereRequest.add("&7Ten cuidado, no aceptes tpa de desconocidos.");
            tpaHereRequest.add(" ");
            changed = setMessage(changed, "tpa.tpa_here_request", tpaHereRequest);

            List<String> tpaHereGeyserRequest = new ArrayList<>();
            tpaHereGeyserRequest.add(" ");
            tpaHereGeyserRequest.add("&aHas recibido una solicitud para ir con &b%player%.");
            tpaHereGeyserRequest.add("&8[&4!&8] &c&lADVERTENCIA &8» &7Ten cuidado, no aceptes tpa de desconocidos.");
            tpaHereGeyserRequest.add("&7Usa &a/tpaccept &7para aceptar o &a/tpdeny &7para rechazar.");
            tpaHereGeyserRequest.add(" ");
            changed = setMessage(changed, "tpa.tpa_here_geyser_request", tpaHereGeyserRequest);


            changed = setMessage(changed, "home.not_allowed", "&cLos homes están deshabilitados en este servidor.");
            changed = setMessage(changed, "home.not_exists", "&cEl home &b%home% &cno existe.");
            changed = setMessage(changed, "home.exists", "&cYa tienes un home con ese nombre.");
            changed = setMessage(changed, "home.deleted_error", "&cDebes especificar un home para poder eliminarlo.");
            changed = setMessage(changed, "home.deleted_successfully", "&6Se ha eliminado el home &b%home% &6correctamente.");
            changed = setMessage(changed, "home.not_specify", "&cDebes especificar un home.");
            changed = setMessage(changed, "home.teleporting", "&aTeletransportando a &b%home%.");
            changed = setMessage(changed, "home.set_error", "&cDebes especificar un nombre para poner un home.");
            changed = setMessage(changed, "home.set_successfully", "&aSe ha establecido tu home &b%home% &acorrectamente.");
            changed = setMessage(changed, "home.max_count", "&cHas alcanzado el máximo de homes que puedes tener.");
            changed = setMessage(changed, "home.delay_in_teleport", "&aTeletransportando en &e%time% &asegundos.");
            changed = setMessage(changed, "home.canceled_move", "&cTeletransporte cancelado por movimiento.");
            changed = setMessage(changed, "home.in_cooldown", "&cDebes esperar &b%time% &csegundos antes de volver a ir a un home.");
            changed = setMessage(changed, "home.has_no_homes", "&c%player% no tiene ningún home.");
            changed = setMessage(changed, "home.no_homes", "&cNo tienes ningún home.");
            changed = setMessage(changed, "home.homes_of", "&aHomes de %player%: ");
            changed = setMessage(changed, "home.your_homes", "&aTus homes: ");


            changed = setMessage(changed, "warps.not_allowed", "&cLos warps están deshabilitados en este servidor.");
            changed = setMessage(changed, "warps.not_exists", "&cEl warps &b%warp% &cno existe.");
            changed = setMessage(changed, "warps.deleted_error", "&cDebes especificar un warps para poder eliminarlo.");
            changed = setMessage(changed, "warps.deleted_successfully", "&6Se ha eliminado el warps &b%warp% &6correctamente.");
            changed = setMessage(changed, "warps.teleporting", "&aTeletransportando a &b%warp%.");
            changed = setMessage(changed, "warps.set_error", "&cDebes especificar un nombre para poner un warps.");
            changed = setMessage(changed, "warps.set_successfully", "&aSe ha establecido el warps &b%warp% &acorrectamente.");
            changed = setMessage(changed, "warps.delay_in_teleport", "&aTeletransportando en &e%warps_delay_time_formatted%&a.");
            changed = setMessage(changed, "warps.canceled_move", "&cTeletransporte cancelado por movimiento.");
            changed = setMessage(changed, "warps.in_cooldown", "&cDebes esperar &b%warps_cooldown_formatted% &cantes de volver a ir a un warps.");
            changed = setMessage(changed, "warps.not_specify", "&cDebes especificar un warp.");
            changed = setMessage(changed, "warps.exeption", "&cEl warp &b%warp% &cno está correctamente establecido.");
            changed = setMessage(changed, "warps.other_teleported", "&aTeletransportado a &b%player% &aa &b%warp%.");
            changed = setMessage(changed, "warps.other_teleport", "&aHas sido teletransportado a &b%warp% &apor &b%sender%.");
            changed = setMessage(changed, "warps.no_warps", "&cNo existe ningun warp.");
            changed = setMessage(changed, "warps.warps_list", "&aWarps: &f%warp%");

        } else {
            changed = setMessage(changed, "variables.enabled", "&aEnabled");
            changed = setMessage(changed, "variables.disabled", "&cDisabled");
            changed = setMessage(changed, "variables.unlimited", "&aUnlimited");
            changed = setMessage(changed, "variables.seconds", "s");
            changed = setMessage(changed, "variables.minutes", "m");
            changed = setMessage(changed, "variables.hours", "h");
            changed = setMessage(changed, "variables.days", "d");

            changed = setMessage(changed, "global.player_offline", "&cThe player &b%player% &cis not online.");
            changed = setMessage(changed, "global.invalid_arguments", "&cInvalid arguments, usage: &a%usage%");
            changed = setMessage(changed, "global.not_executed_in_proxy", "&cThe server is not running in a proxy.");
            changed = setMessage(changed, "global.in_cooldown", "&cYou must wait &b%time% &cseconds before using this command again.");
            changed = setMessage(changed, "global.dependency_found", "&aDependency found: &b%dependency%");
            changed = setMessage(changed, "global.unknown_software", "Unknown server software.");
            changed = setMessage(changed, "global.permission_denied", "&cYou need the permission &b%permission% &cto do that.");
            changed = setMessage(changed, "global.console_denied", "&cThis command can only be executed by a player.");
            changed = setMessage(changed, "global.null_sound", "&cThe sound cannot be empty.");
            changed = setMessage(changed, "global.invalid_sound", "&6%sound% &cis not a valid sound.");
            changed = setMessage(changed, "global.server_not_exists", "&cThe server &b%server% &cdoes not exist.");
            changed = setMessage(changed, "global.reload", "&aAll configuration files reloaded in version &aV%version%. &8(&7%time%&8)");
            changed = setMessage(changed, "global.version", "&fThe version of &bDeluxeTeleport &fis &aV%version% &8(&7The latest version is &a%last_version%&8).");
            changed = setMessage(changed, "global.usage", "&7Use &a/dt help &7to get help.");
            changed = setMessage(changed, "global.specify_player", "&cYou must specify a player.");
            changed = setMessage(changed, "global.in_teleport", "&cYou are already being teleported.");
            changed = setMessage(changed, "global.updated_config", "&aDone! Configuration &b%config% &aupdated!");
            changed = setMessage(changed, "global.updating_config", "&aUpdating &b%config% &ato the latest version...");
            changed = setMessage(changed, "global.restart_error_arguments_0", "&cYou must specify a time type to reset &8(&7%types%&8)");
            changed = setMessage(changed, "global.restart_error_arguments_1", "&c%type% is not a valid time type");
            changed = setMessage(changed, "global.restart_invalid_arguments", "&cYou must specify a player to reset time &8(&7%type%&8)");
            changed = setMessage(changed, "global.restart_successfully", "&aResetting %type% for %player%...");

            List<String> helpMessage = new ArrayList<>();
            helpMessage.add("&bDeluxeTeleport &8- &aHelp");
            helpMessage.add(" ");
            helpMessage.add("&a/deluxeteleport version &8- &7View the plugin version.");
            helpMessage.add("&a/deluxeteleport update &8- &7Update the plugin to the latest version.");
            helpMessage.add("&a/deluxeteleport reload &8- &7Reload the configurations.");
            helpMessage.add("&a/deluxeteleport help &8- &7Show this help message.");
            helpMessage.add("&a/deluxeteleport restart &8- &7Reset a player's time.");
            changed = setMessage(changed, "global.help", helpMessage);


            changed = setMessage(changed, "migrate.error_plugin_not_specified", "&cYou must specify from which plugin to import data. &aCompatible plugins: &7%compatible_plugins%");
            changed = setMessage(changed, "migrate.error_invalid_plugin", "&c%plugin% is not a compatible plugin. &aCompatible plugins: &7%compatible_plugins%");
            changed = setMessage(changed, "migrate.error_data_not_specified", "&cYou must specify the type of data to import. &aCompatible data types: &7%compatible_data%");
            changed = setMessage(changed, "migrate.error_invalid_data", "&c%data% is not a compatible data type. &aCompatible data types: &7%compatible_data%");


            changed = setMessage(changed, "reset.error_unspecified_value", "&cYou must specify a value to reset. &aAvailable values: &7%available_values%");
            changed = setMessage(changed, "reset.error_invalid_value", "&c%value% is not a valid value. &aAvailable values: &7%available_values%");
            changed = setMessage(changed, "reset.unspecified_player", "&cYou must specify a player to reset the %value% value");
            changed = setMessage(changed, "reset.successfully", "&aResetting %value% for %player%...");


            changed = setMessage(changed, "mysql.error_connecting", "&cError connecting to MySQL: connection already open.");
            changed = setMessage(changed, "mysql.error_tables", "&cError checking or creating tables in MySQL.");
            changed = setMessage(changed, "mysql.successfully_tables", "&aTables successfully created.");
            changed = setMessage(changed, "mysql.error", "&aMySQL connection error.");
            changed = setMessage(changed, "mysql.connected", "&aDeluxeTeleport connected to MySQL.");


            changed = setMessage(changed, "update.error_check", "&cCannot check for updates: %error%.");
            changed = setMessage(changed, "update.is_empty", "&cThe latest version is empty.");
            changed = setMessage(changed, "update.update_available", "&aAn update is available! &8(&b%latest_version%&8).");
            changed = setMessage(changed, "update.its_updated", "&aYou are up to date! &8(&b%latest_version%&8).");
            changed = setMessage(changed, "update.jar_not_found", "&cCould not find the old plugin jar file! Make sure it is named: &b%jar_name%.");
            changed = setMessage(changed, "update.downloaded", "&aDownloading the latest update...");
            changed = setMessage(changed, "update.unable_to_deleted", "&cCannot delete the old plugin! You must delete it manually before restarting.");
            changed = setMessage(changed, "update.response_code", "&cThe response code was: &b%code%.");
            changed = setMessage(changed, "update.looking", "&bSearching for updates...");

            List<String> newUpdateMessage = new ArrayList<>();
            newUpdateMessage.add("&cA new version is available. &e(&7%last_version%&e)");
            newUpdateMessage.add("&cUse &a/deluxeteleport update &cto update");
            newUpdateMessage.add("&cor you can download it at: &f%plugin_url%");
            changed = setMessage(changed, "update.new_update", newUpdateMessage);
            changed = setMessage(changed, "update.changelog_invalid_format", "&cInvalid version format. Use x.x.x format (for example, 2.1.0).");
            changed = setMessage(changed, "update.changelog_disabled_notify", "&cYou have disabled changelog notifications for version %version%");
            changed = setMessage(changed, "update.changelog_enabled_notify", "&aYou have enabled changelog notifications for version %version%");
            changed = setMessage(changed, "update.changelog_invalid_page", "&cInvalid page number. Showing page 1.");
            changed = setMessage(changed, "update.changelog_no_changelogs_found", "&cNo changelog found for version &b%version%.");
            changed = setMessage(changed, "update.changelog_error_info", "&cThere was an error fetching the version %version% info. Please try again later.");
            changed = setMessage(changed, "update.changelog_exeption", "&cAn error occurred while processing the request.");
            changed = setMessage(changed, "update.changelog_pages", "&7Page: %page_number%&8/&7%total_pages%");
            changed = setMessage(changed, "update.changelog_pages_console", "&7Page: %page_number%&8/&7%total_pages% &8(&7/deluxeteleport changelog page&8)");
            changed = setMessage(changed, "update.changelog_register", "&aChangelog for version &b%version%&a:");
            changed = setMessage(changed, "update.changelog_see_full", "&b&lSEE FULL");
            changed = setMessage(changed, "update.changelog_see_full_description", "&bClick to see the full changelog.");
            changed = setMessage(changed, "update.changelog_ok", "&e&lUNDERSTOOD");
            changed = setMessage(changed, "update.changelog_ok_description", "&eDo not show this message for this version again.");
            changed = setMessage(changed, "update.changelog_next", "&a&lNEXT");
            changed = setMessage(changed, "update.changelog_next_description", "&aClick to see the next page.");
            changed = setMessage(changed, "update.changelog_previus", "&c&lPREVIOUS");
            changed = setMessage(changed, "update.changelog_previus_description", "&cClick to see the previous page.");


            changed = setMessage(changed, "lobby.not_allowed", "&cLobby is not enabled.");
            changed = setMessage(changed, "lobby.invalid_type", "&cThe specified lobby type &8(&7%type%&8) &cin lobby.yml is invalid.");
            changed = setMessage(changed, "lobby.invalid_mode", "&cThe lobby mode &b%mode% &cis not valid, use &b%modes%.");
            changed = setMessage(changed, "lobby.other_teleported", "&aTeleported to &b%player% &ato the lobby.");
            changed = setMessage(changed, "lobby.other_teleport", "&aYou have been teleported to the lobby by &b%sender%.");
            changed = setMessage(changed, "lobby.in_cooldown", "&cYou must wait &b%time% &cseconds before going to the lobby again.");
            changed = setMessage(changed, "lobby.insufficient_money", "&cYou need &a$%money%/%current_money% &cto teleport.");
            changed = setMessage(changed, "lobby.discounted_money", "&f$%money% &ahave been deducted from your balance.");
            changed = setMessage(changed, "lobby.teleporting", "&aTeleporting to the lobby...");
            changed = setMessage(changed, "lobby.established", "&aLobby &b%lobby% &asuccessfully set in world &b%world%.");
            changed = setMessage(changed, "lobby.not_exists", "&cLobby &b%lobby% &cdoes not exist.");
            changed = setMessage(changed, "lobby.in_teleport", "&cYou are already being teleported to the lobby.");
            changed = setMessage(changed, "lobby.delay_in_teleport", "&aTeleporting to the lobby in &e%time% &aseconds.");
            changed = setMessage(changed, "lobby.canceled_move", "&cLobby teleport canceled due to movement.");
            changed = setMessage(changed, "lobby.deleted_canceled", "&6Confirmation time expired. Lobby deletion canceled.");
            changed = setMessage(changed, "lobby.deleted_successfully", "&aLobby &b%lobby% &adeleted successfully.");


            changed = setMessage(changed, "spawn.not_allowed", "&cSpawn is not enabled.");
            changed = setMessage(changed, "spawn.not_exists", "&cSpawn &b%spawn% &cdoes not exist.");
            changed = setMessage(changed, "spawn.other_teleported", "&aTeleported to &b%player% &aat spawn.");
            changed = setMessage(changed, "spawn.other_teleport", "&aYou have been teleported to the spawn by &b%sender%.");
            changed = setMessage(changed, "spawn.in_cooldown", "&cYou must wait &b%time% &cseconds before going to the spawn again.");
            changed = setMessage(changed, "spawn.teleporting", "&aTeleporting to the spawn...");
            changed = setMessage(changed, "spawn.canceled_move", "&cSpawn teleport canceled due to movement.");
            changed = setMessage(changed, "spawn.delay_in_teleport", "&aTeleporting to the spawn in &e%time% &aseconds.");
            changed = setMessage(changed, "spawn.in_teleport", "&cYou are already being teleported to the spawn.");
            changed = setMessage(changed, "spawn.insufficient_money", "&cYou need &a$%money%/%current_money% &cto teleport.");
            changed = setMessage(changed, "spawn.discounted_money", "&f$%money% &ahave been deducted from your balance.");
            changed = setMessage(changed, "spawn.established", "&aSpawn successfully set in world &b%world%.");
            changed = setMessage(changed, "spawn.deleted_canceled", "&6Confirmation time expired. Spawn deletion canceled.");
            changed = setMessage(changed, "spawn.deleted_successfully", "&aSpawn &b%spawn% &adeleted successfully.");
            changed = setMessage(changed, "spawn.deleted_error", "&cYou must specify a spawn in order to delete it.");
            changed = setMessage(changed, "spawn.no_spawns", "&cThere is no spawn.");
            changed = setMessage(changed, "spawn.spawns_list", "&aSpawns: &f%spawns%");
            changed = setMessage(changed, "spawn.exeption", "&cThis spawn is not set correctly.");


            changed = setMessage(changed, "tpa.not_allowed", "&cTPA is disabled on this server.");
            changed = setMessage(changed, "tpa.specify_player", "&cYou must specify a player.");
            changed = setMessage(changed, "tpa.himself", "&cYou cannot send TPA to yourself.");
            changed = setMessage(changed, "tpa.blocked", "&cThis player has blocked teleport requests.");
            changed = setMessage(changed, "tpa.pending", "&cYou have already sent a teleport request.");
            changed = setMessage(changed, "tpa.pending_request", "&cThis player still has a pending teleport request.");
            changed = setMessage(changed, "tpa.send", "&aYou have sent a teleport request to &b%player%.");
            changed = setMessage(changed, "tpa.teleport_defeated", "&cTeleport request to &b%player% &chas been canceled.");
            changed = setMessage(changed, "tpa.teleport_defeated_targetplayer", "&cThe teleport request has expired.");
            changed = setMessage(changed, "tpa.toggle_yes", "&aYou now receive teleport requests.");
            changed = setMessage(changed, "tpa.toggle_no", "&cYou have stopped receiving teleport requests.");
            changed = setMessage(changed, "tpa.toggle_other", "&aYou have &b%status% &aTPA requests for &b%player%.");
            changed = setMessage(changed, "tpa.toggle_other_targetplayer", "&b%player% &ahas toggled your teleport requests.");
            changed = setMessage(changed, "tpa.deny", "&cYou have rejected the teleport request from &b%player%.");
            changed = setMessage(changed, "tpa.deny_targetplayer", "&b%player% &cha rejected your teleport request.");
            changed = setMessage(changed, "tpa.cancel", "&cYou have canceled the teleport to &b%player%.");
            changed = setMessage(changed, "tpa.cancel_targetplayer", "&b%player% &cha canceled the teleport request.");
            changed = setMessage(changed, "tpa.no_requests", "&cYou have no teleport requests.");
            changed = setMessage(changed, "tpa.no_requests_player", "&cYou have no teleport requests from &b%player%.");
            changed = setMessage(changed, "tpa.delay_in_teleport", "&aTeleporting in &e%time% &aseconds.");
            changed = setMessage(changed, "tpa.teleporting", "&aTeleporting to %player%...");
            changed = setMessage(changed, "tpa.teleport", "&aYou have been teleported to &b%player%.");
            changed = setMessage(changed, "tpa.teleport_targetplayer", "&b%player% &ahas been teleported to you.");
            changed = setMessage(changed, "tpa.canceled_move", "&cTeleport canceled due to movement.");
            changed = setMessage(changed, "tpa.in_cooldown", "&cYou must wait &b%time% &cbefore sending another TPA.");
            changed = setMessage(changed, "tpa.no_request_sent", "&cYou have not sent any TPA requests.");
            changed = setMessage(changed, "tpa.no_request_sent_player", "&cYou have not sent any TPA requests to &b%player%.");
            changed = setMessage(changed, "tpa.currently_in_teleportation", "&cYou are currently teleporting to &b%player%.");
            changed = setMessage(changed, "tpa.player_in_teleport", "&cYou cannot receive more requests while you are receiving another one.");
            changed = setMessage(changed, "tpa.target_player_in_teleport", "&b%player% &cis currently teleporting.");
            changed = setMessage(changed, "tpa.click_accept", "&a&lACCEPT");
            changed = setMessage(changed, "tpa.click_cancel", "&c&lREJECT");
            changed = setMessage(changed, "tpa.click_accept_description", "&aClick to accept.");
            changed = setMessage(changed, "tpa.click_cancel_description", "&cClick to reject.");

            List<String> tpaRequest = new ArrayList<>();
            tpaRequest.add(" ");
            tpaRequest.add("&aYou have received a teleport request from &b%player%.");
            tpaRequest.add("&8[&4!&8] &c&lWARNING");
            tpaRequest.add("&7Be careful, do not accept TPA from strangers.");
            tpaRequest.add(" ");
            changed = setMessage(changed, "tpa.tpa_request", tpaRequest);

            List<String> tpaBedrockRequest = new ArrayList<>();
            tpaBedrockRequest.add(" ");
            tpaBedrockRequest.add("&aYou have received a teleport request from &b%player%.");
            tpaBedrockRequest.add("&8[&4!&8] &c&lWARNING &8» &7Be careful, do not accept TPA from strangers.");
            tpaBedrockRequest.add("&7Use &a/tpaccept &7to accept or &a/tpdeny &7to reject.");
            tpaBedrockRequest.add(" ");
            changed = setMessage(changed, "tpa.tpa_bedrock_request", tpaBedrockRequest);

            List<String> tpaHereRequest = new ArrayList<>();
            tpaHereRequest.add(" ");
            tpaHereRequest.add("&aYou have received a request to go with &b%player%.");
            tpaHereRequest.add("&8[&4!&8] &c&lWARNING");
            tpaHereRequest.add("&7Be careful, do not accept TPA from strangers.");
            tpaHereRequest.add(" ");
            changed = setMessage(changed, "tpa.tpa_here_request", tpaHereRequest);

            List<String> tpaHereGeyserRequest = new ArrayList<>();
            tpaHereGeyserRequest.add(" ");
            tpaHereGeyserRequest.add("&aYou have received a request to go with &b%player%.");
            tpaHereGeyserRequest.add("&8[&4!&8] &c&lWARNING &8» &7Be careful, do not accept TPA from strangers.");
            tpaHereGeyserRequest.add("&7Use &a/tpaccept &7to accept or &a/tpdeny &7to reject.");
            tpaHereGeyserRequest.add(" ");
            changed = setMessage(changed, "tpa.tpa_here_geyser_request", tpaHereGeyserRequest);


            changed = setMessage(changed, "home.not_allowed", "&cHomes are disabled on this server.");
            changed = setMessage(changed, "home.not_exists", "&cThe home &b%home% &cdoes not exist.");
            changed = setMessage(changed, "home.exists", "&cYou already have a home with that name.");
            changed = setMessage(changed, "home.deleted_error", "&cYou must specify a home to delete.");
            changed = setMessage(changed, "home.deleted_successfully", "&6The home &b%home% &6has been deleted successfully.");
            changed = setMessage(changed, "home.not_specify", "&cYou must specify a home.");
            changed = setMessage(changed, "home.teleporting", "&aTeleporting to &b%home%.");
            changed = setMessage(changed, "home.set_error", "&cYou must specify a name to set a home.");
            changed = setMessage(changed, "home.set_successfully", "&aYour home &b%home% &ahas been set successfully.");
            changed = setMessage(changed, "home.max_count", "&cYou have reached the maximum number of homes you can have.");
            changed = setMessage(changed, "home.delay_in_teleport", "&aTeleporting in &e%time% &aseconds.");
            changed = setMessage(changed, "home.canceled_move", "&cTeleport canceled due to movement.");
            changed = setMessage(changed, "home.in_cooldown", "&cYou must wait &b%time% &cseconds before teleporting to a home again.");
            changed = setMessage(changed, "home.has_no_homes", "&c%player% has no homes.");
            changed = setMessage(changed, "home.no_homes", "&cYou have no homes.");
            changed = setMessage(changed, "home.homes_of", "&aHomes of %player%: ");
            changed = setMessage(changed, "home.your_homes", "&aYour homes: ");


            changed = setMessage(changed, "warps.not_allowed", "&cWarps are disabled on this server.");
            changed = setMessage(changed, "warps.not_exists", "&cThe warps &b%warp% &cdoes not exist.");
            changed = setMessage(changed, "warps.deleted_error", "&cYou must specify a warps to delete it.");
            changed = setMessage(changed, "warps.deleted_successfully", "&6The warps &b%warp% &6has been successfully deleted.");
            changed = setMessage(changed, "warps.teleporting", "&aTeleporting to &b%warps%.");
            changed = setMessage(changed, "warps.set_error", "&cYou must specify a name to set a warps.");
            changed = setMessage(changed, "warps.set_successfully", "&aThe warps &b%warp% &ahas been successfully set.");
            changed = setMessage(changed, "warps.delay_in_teleport", "&aTeleporting in &e%warps_delay_time_formatted%&a.");
            changed = setMessage(changed, "warps.canceled_move", "&cTeleportation canceled due to movement.");
            changed = setMessage(changed, "warps.in_cooldown", "&cYou must wait &b%warps_cooldown_formatted% &cbefore using a warps again.");
            changed = setMessage(changed, "warps.not_specify", "&cYou must specify a warp.");
            changed = setMessage(changed, "warps.exeption", "&cThe warp &%warp% &Cis not set correctly.");
            changed = setMessage(changed, "warps.other_teleported", "&aTeleported to &b%player% &aat &b%warp%.");
            changed = setMessage(changed, "warps.other_teleport", "&aYou have been teleported to &b%warp% &aby &b%sender%.");
            changed = setMessage(changed, "warps.no_warps", "&cThere are no warps.");
            changed = setMessage(changed, "warps.warps_list", "&aWarps: &f%warp%");
        }

        ServerVersion serverVersion = ServerInfo.getServerVersion();
        /*if (ServerVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_18_1)) {
            addComments(config);
        }*/

        if (changed) {
            OtherUtils.updateConfig(plugin, "Messages");
            createFile("messages-new.yml", "lang/" + lang + "/messages.yml", plugin);
            File tempFile = new File(plugin.getDataFolder(), "messages-new.yml");

            try {
                YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(tempFile), StandardCharsets.UTF_8));
                this.saveMessages();
                String message = getGlobalUpdatedConfig() != null ? getGlobalUpdatedConfig() : "&aDone! Configuration &b%config% &aupdated!";
                m.sendMessage(Bukkit.getConsoleSender(), message
                        .replace("%config%", "Messages"), true);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                tempFile.delete();
            }
        }
    }

    private static @NotNull List<String> getStrings() {
        List<String> globalHelp = new ArrayList<>();
        globalHelp.add("&bDeluxeTeleport &8- &aAyuda");
        globalHelp.add(" ");
        globalHelp.add("&a/deluxeteleport version &8- &7Ver la versión del plugin.");
        globalHelp.add("&a/deluxeteleport update &8- &7Actualizar el plugin a la última versión.");
        globalHelp.add("&a/deluxeteleport reload &8- &7Recargar las configuraciones.");
        globalHelp.add("&a/deluxeteleport help &8- &7Mostrar este mensaje de ayuda.");
        globalHelp.add("&a/deluxeteleport restart &8- &7Reiniciar el tiempo de de un jugador.");
        return globalHelp;
    }

    private void createFile(String name, String from, DeluxeTeleport plugin) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            try {
                Files.copy(plugin.getResource(from), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                m.sendMessage(Bukkit.getConsoleSender(),name + " file for DeluxeTeleport!" + e, true);
            }
        }
    }

    private boolean addMissingFields(FileConfiguration currentConfig, FileConfiguration newConfig) {
        boolean changed = false;
        for (String key : newConfig.getKeys(true)) {
            if (!currentConfig.contains(key)) {
                currentConfig.set(key, newConfig.get(key));
                changed = true;
            }
        }
        return changed;
    }

    public void loadMessages() {
        FileConfiguration config = plugin.getConfig();
        String lang = config.getString("lang", "en-EN");

        messageFile = new FileManager("messages.yml", "lang/" + lang, plugin);
        messageFile.registerFile();

        if (!messageFile.getFile().exists()) {
            plugin.saveResource("lang/" + lang + "/messages.yml", false);
            messageFile.reloadConfig();
        }

        FileConfiguration messages = messageFile.getConfig();

        Prefix = messages.getString("prefix.global");
        PrefixLobby = messages.getString("prefix.lobby");
        PrefixSpawn = messages.getString("prefix.spawn");
        PrefixTPA = messages.getString("prefix.tpa");
        PrefixHome = messages.getString("prefix.home");
        PrefixWarp = messages.getString("prefix.warps");

        GlobalPlayerOffline = messages.getString("global.player_offline");
        GlobalInvalidArguments = messages.getString("global.invalid_arguments");
        GlobalNotExecuteInProxy = messages.getString("global.not_executed_in_proxy");
        GlobalDependencyFound = messages.getString("global.dependency_found");
        GlobalPermissionDenied = messages.getString("global.permission_denied");
        GlobalConsoleDenied = messages.getString("global.console_denied");
        GlobalServerNotExists = messages.getString("global.server_not_exists");
        GlobalHelp = messages.getStringList("global.help");
        GlobalReload = messages.getString("global.reload");
        GlobalUsage = messages.getString("global.usage");
        GlobalInTeleport = messages.getString("global.in_teleport");
        GlobalUpdatedConfig = messages.getString("global.updated_config");
        GlobalUpdatingConfig = messages.getString("global.updating_config");

        MySQLErrorConnecting = messages.getString("mysql.error_connecting");
        MySQLError = messages.getString("mysql.error");
        MySQLConnecting = messages.getString("mysql.connecting");
        MySQLErrorTables = messages.getString("mysql.error_tables");

        UpdateErrorCheck = messages.getString("update.error_check");
        UpdateAvailable = messages.getString("update.update_available");
        UpdateItsUpdated = messages.getString("update.its_updated");
        UpdateJarNotFound = messages.getString("update.jar_not_found");
        UpdateDownloaded = messages.getString("update.downloaded");
        UpdateUnableToDeleted = messages.getString("update.unable_to_deleted");
        UpdateResponseCode = messages.getString("update.response_code");
        UpdateLooking = messages.getString("update.looking");
        UpdateNewUpdate = messages.getStringList("update.new_update");
        UpdateChangelogInvalidFormat = messages.getString("update.changelog_invalid_format");
        UpdateChangelogDisabledNotify = messages.getString("update.changelog_disabled_notify");
        UpdateChangelogEnabledNotify = messages.getString("update.changelog_enabled_notify");
        UpdateChangelogInvalidPage = messages.getString("update.changelog_invalid_page");
        UpdateChangelogNoChangelogsFound = messages.getString("update.changelog_no_changelogs_found");
        UpdateChangelogErrorInfo = messages.getString("update.changelog_error_info");
        UpdateChangelogExeption = messages.getString("update.changelog_exeption");
        UpdateChangelogPages = messages.getString("update.changelog_pages");
        UpdateChangelogPagesConsole = messages.getString("update.changelog_pages_console");
        UpdateChangelogRegister = messages.getString("update.changelog_register");
        UpdateChangelogSeeFull = messages.getString("update.changelog_see_full");
        UpdateChangelogSeeFullDescription = messages.getString("update.changelog_see_full_description");
        UpdateChangelogOk = messages.getString("update.changelog_ok");
        UpdateChangelogOkDescription = messages.getString("update.changelog_ok_description");
        UpdateChangelogNext = messages.getString("update.changelog_next");
        UpdateChangelogNextDescription = messages.getString("update.changelog_next_description");
        UpdateChangelogPrevius = messages.getString("update.changelog_previus");
        UpdateChangelogPreviusDescription = messages.getString("update.changelog_previus_description");

        LobbyInvalidSpecified = messages.getString("lobby.invalid_type");
        LobbyOtherTeleported = messages.getString("lobby.other_teleported");
        LobbyOtherTeleport = messages.getString("lobby.other_teleport");
        LobbyInvalidMode = messages.getString("lobby.invalid_mode");
        LobbyInCooldown = messages.getString("lobby.in_cooldown");
        LobbyTeleporting = messages.getString("lobby.teleporting");
        LobbyNotEnabled = messages.getString("lobby.not_allowed");
        LobbyEstablished = messages.getString("lobby.established");
        LobbyNotExists = messages.getString("lobby.not_exists");
        LobbyInTeleport = messages.getString("lobby.in_teleport");
        LobbyDelayInTeleport = messages.getString("lobby.delay_in_teleport");
        LobbyCanceledMove = messages.getString("lobby.canceled_move");
        LobbyDeletedSuccessfully = messages.getString("lobby.deleted_successfully");

        SpawnNotEnabled = messages.getString("spawn.not_allowed");
        SpawnNotExists = messages.getString("spawn.not_exists");
        SpawnOtherTeleported = messages.getString("spawn.other_teleported");
        SpawnOtherTeleport = messages.getString("spawn.other_teleport");
        SpawnInCooldown = messages.getString("spawn.in_cooldown");
        SpawnTeleporting = messages.getString("spawn.teleporting");
        SpawnCanceledMove = messages.getString("spawn.canceled_move");
        SpawnDelayInTeleport = messages.getString("spawn.delay_in_teleport");
        SpawnInTeleport = messages.getString("spawn.in_teleport");
        SpawnDeletedSuccessfully = messages.getString("spawn.deleted_successfully");
        SpawnEstablished = messages.getString("spawn.established");
        SpawnDeletedError = messages.getString("spawn.deleted_error");
        SpawnNoSpawns = messages.getString("spawn.no_spawns");
        SpawnSpawnsList = messages.getString("spawn.spawns_list");
        SpawnExeption = messages.getString("spawn.exeption");

        TPANotEnabled = messages.getString("tpa.not_allowed");
        TPARequest = messages.getStringList("tpa.tpa_request");
        TPAGeyserRequest = messages.getStringList("tpa.tpa_geyser_request");
        TPAClickAccept = messages.getString("tpa.click_accept");
        TPAClickCancel = messages.getString("tpa.click_cancel");
        TPAClickAcceptDescription = messages.getString("tpa.click_accept_description");
        TPAClickCancelDescription = messages.getString("tpa.click_cancel_description");
        TPASpecifyPlayer = messages.getString("tpa.specify_player");
        TPAHimself = messages.getString("tpa.himself");
        TPABlocked = messages.getString("tpa.blocked");
        TPAPending = messages.getString("tpa.pending");
        TPASend = messages.getString("tpa.send");
        TPATeleportDefeated = messages.getString("tpa.teleport_defeated");
        TPATeleportDefeatedTargetPlayer = messages.getString("tpa.teleport_defeated_targetplayer");
        TPAToggleYes = messages.getString("tpa.toggle_yes");
        TPAToggleNo = messages.getString("tpa.toggle_no");
        TPAToggleOther = messages.getString("tpa.toggle_other");
        TPAToggleOtherTargetPlayer = messages.getString("tpa.toggle_other_targetplayer");
        TPAPendingRequest = messages.getString("tpa.pending_request");
        TPADeny = messages.getString("tpa.deny");
        TPADenyTargetPlayer = messages.getString("tpa.deny_targetplayer");
        TPANoRequest = messages.getString("tpa.no_requests");
        TPANoRequestPlayer = messages.getString("tpa.no_requests_player");
        TPADelayInTeleport = messages.getString("tpa.delay_in_teleport");
        TPATeleporting = messages.getString("tpa.teleporting");
        TPACanceledMoving = messages.getString("tpa.canceled_move");
        TPATeleport = messages.getString("tpa.teleport");
        TPATeleportTargetPlayer = messages.getString("tpa.teleport_targetplayer");
        TPAHereRequest = messages.getStringList("tpa.tpa_here_request");
        TPAHereGeyserRequest = messages.getStringList("tpa.tpa_here_bedrock_request");
        TPAInCooldown = messages.getString("tpa.in_cooldown");
        TPACancel = messages.getString("tpa.cancel");
        TPACancelTargetPlayer = messages.getString("tpa.cancel_targetplayer");
        TPANoRequestSent = messages.getString("tpa.no_request_sent");
        TPANoRequestSentPlayer = messages.getString("tpa.no_request_sent_player");
        TPAPlayerInTeleport = messages.getString("tpa.player_in_teleport");
        TPATargetPlayerInTeleport = messages.getString("tpa.target_player_in_teleport");

        VariablesEnabled = messages.getString("variables.enabled");
        VariablesDisabled = messages.getString("variables.disabled");
        VariablesUnlimited = messages.getString("variables.unlimited");
        VariablesSeconds = messages.getString("variables.seconds");
        VariablesMinutes = messages.getString("variables.minutes");
        VariablesHours = messages.getString("variables.hours");
        VariablesDays = messages.getString("variables.days");

        HomeNotEnabled = messages.getString("home.not_allowed");
        HomeDelayInTeleport = messages.getString("home.delay_in_teleport");
        HomeTeleporting = messages.getString("home.teleporting");
        HomeCanceledMove = messages.getString("home.canceled_move");
        HomeInCooldown = messages.getString("home.in_cooldown");
        HomeNotSpecify = messages.getString("home.not_specify");
        HomeNotExist = messages.getString("home.not_exists");
        HomeDeletedError = messages.getString("home.deleted_error");
        HomeDeletedSuccessfully = messages.getString("home.deleted_successfully");
        HomeSetError = messages.getString("home.set_error");
        HomeExists = messages.getString("home.exists");
        HomeMaxCount = messages.getString("home.max_count");
        HomeSetSuccessfully = messages.getString("home.set_successfully");
        HomeHasNoHomes = messages.getString("home.has_no_homes");
        HomeNoHomes = messages.getString("home.no_homes");
        HomeHomesOf = messages.getString("home.homes_of");
        HomeYourHomes = messages.getString("home.your_homes");

        MigrateErrorPluginNotSpecified = messages.getString("migrate.error_plugin_not_specified");
        MigrateErrorInvalidPlugin = messages.getString("migrate.error_invalid_plugin");
        MigrateErrorDataNotSpecified = messages.getString("migrate.error_data_not_specified");
        MigrateErrorInvalidData = messages.getString("migrate.error_invalid_data");

        ResetErrorUnspecifiedValue = messages.getString("reset.error_unspecified_value");
        ResetErrorInvalidValue = messages.getString("reset.error_invalid_value");
        ResetUnspecifiedPlayer = messages.getString("reset.unspecified_player");
        ResetSuccessfully = messages.getString("reset.successfully");

        WarpNotEnabled = messages.getString("warps.not_allowed");
        WarpNotExist = messages.getString("warps.not_exists");
        WarpDeletedError = messages.getString("warps.deleted_error");
        WarpDeletedSuccessfully = messages.getString("warps.deleted_successfully");
        WarpTeleporting = messages.getString("warps.teleporting");
        WarpSetError = messages.getString("warps.set_error");
        WarpSetSuccessfully = messages.getString("warps.set_successfully");
        WarpDelayInTeleport = messages.getString("warps.delay_in_teleport");
        WarpCanceledMove = messages.getString("warps.canceled_move");
        WarpInCooldown = messages.getString("warps.in_cooldown");
        WarpNotSpecify = messages.getString("warps.not_specify");
        WarpExeption = messages.getString("warps.exeption");
        WarpOtherTeleported = messages.getString("warps.other_teleported");
        WarpOtherTeleport = messages.getString("warps.other_teleport");
        WarpNoWarps = messages.getString("warps.no_warps");
        WarpWarpsList = messages.getString("warps.warps_list");
    }

    public String getPrefix() {
        return Prefix;
    }

    public String getPrefixLobby() {
        return PrefixLobby;
    }

    public String getPrefixSpawn() {
        return PrefixSpawn;
    }

    public String getPrefixWarp() {
        return PrefixWarp;
    }

    public String getLobbyInvalidSpecified() {
        return LobbyInvalidSpecified;
    }

    public String getGlobalPlayerOffline() {
        return GlobalPlayerOffline;
    }

    public String getLobbyOtherTeleported() {
        return LobbyOtherTeleported;
    }

    public String getLobbyOtherTeleport() {
        return LobbyOtherTeleport;
    }

    public String getGlobalNotExecuteInProxy() {
        return GlobalNotExecuteInProxy;
    }

    public String getGlobalInvalidArguments() {
        return GlobalInvalidArguments;
    }

    public String getLobbyInvalidMode() {
        return LobbyInvalidMode;
    }

    public String getLobbyInCooldown() {
        return LobbyInCooldown;
    }

    public String getLobbyTeleporting() {
        return LobbyTeleporting;
    }

    public String getMySQLErrorConnecting() {
        return MySQLErrorConnecting;
    }

    public String getMySQLError() {
        return MySQLError;
    }

    public String getMySQLConnecting() {
        return MySQLConnecting;
    }

    public String getMySQLErrorTables() {
        return MySQLErrorTables;
    }

    public String getGlobalDependencyFound() {
        return GlobalDependencyFound;
    }

    public String getUpdateErrorCheck() {
        return UpdateErrorCheck;
    }

    public String getUpdateAvailable() {
        return UpdateAvailable;
    }

    public String getUpdateItsUpdated() {
        return UpdateItsUpdated;
    }

    public String getUpdateJarNotFound() {
        return UpdateJarNotFound;
    }

    public String getUpdateDownloaded() {
        return UpdateDownloaded;
    }

    public String getUpdateUnableToDeleted() {
        return UpdateUnableToDeleted;
    }

    public String getUpdateResponseCode() {
        return UpdateResponseCode;
    }

    public String getLobbyNotEnabled() {
        return LobbyNotEnabled;
    }

    public String getLobbyEstablished() {
        return LobbyEstablished;
    }

    public String getLobbyNotExists() {
        return LobbyNotExists;
    }

    public String getGlobalPermissionDenied() {
        return GlobalPermissionDenied;
    }

    public String getGlobalConsoleDenied() {
        return GlobalConsoleDenied;
    }

    public String getLobbyInTeleport() {
        return LobbyInTeleport;
    }

    public String getLobbyDelayInTeleport() {
        return LobbyDelayInTeleport;
    }

    public String getLobbyCanceledMove() {
        return LobbyCanceledMove;
    }

    public String getGlobalServerNotExists() {
        return GlobalServerNotExists;
    }

    public List<String> getGlobalHelp() {
        return GlobalHelp;
    }

    public String getGlobalReload() {
        return GlobalReload;
    }

    public String getUpdateLooking() {
        return UpdateLooking;
    }

    public String getSpawnNotEnabled() {
        return SpawnNotEnabled;
    }

    public String getSpawnNotExists() {
        return SpawnNotExists;
    }

    public String getSpawnOtherTeleported() {
        return SpawnOtherTeleported;
    }

    public String getSpawnOtherTeleport() {
        return SpawnOtherTeleport;
    }

    public String getSpawnInCooldown() {
        return SpawnInCooldown;
    }

    public String getSpawnTeleporting() {
        return SpawnTeleporting;
    }

    public String getSpawnCanceledMove() {
        return SpawnCanceledMove;
    }

    public String getSpawnDelayInTeleport() {
        return SpawnDelayInTeleport;
    }

    public String getSpawnInTeleport() {
        return SpawnInTeleport;
    }

    public String getGlobalUsage() {
        return GlobalUsage;
    }

    public String getLobbyDeletedSuccessfully() {
        return LobbyDeletedSuccessfully;
    }

    public String getSpawnDeletedSuccessfully() {
        return SpawnDeletedSuccessfully;
    }

    public String getSpawnEstablished() {
        return SpawnEstablished;
    }

    public String getPrefixTPA() {
        return PrefixTPA;
    }

    public String getTPANotEnabled() {
        return TPANotEnabled;
    }

    public List<String> getTPARequest() {
        return TPARequest;
    }

    public String getTPAClickAccept() {
        return TPAClickAccept;
    }

    public String getTPAClickCancel() {
        return TPAClickCancel;
    }

    public String getTPAClickAcceptDescription() {
        return TPAClickAcceptDescription;
    }

    public String getTPAClickCancelDescription() {
        return TPAClickCancelDescription;
    }

    public List<String> getTPAGeyserRequest() {
        return TPAGeyserRequest;
    }

    public String getTPASpecifyPlayer() {
        return TPASpecifyPlayer;
    }

    public String getTPAHimself() {
        return TPAHimself;
    }

    public String getTPABlocked() {
        return TPABlocked;
    }

    public String getTPAPending() {
        return TPAPending;
    }

    public String getTPASend() {
        return TPASend;
    }

    public String getTPATeleportDefeated() {
        return TPATeleportDefeated;
    }

    public String getTPATeleportDefeatedTargetPlayer() {
        return TPATeleportDefeatedTargetPlayer;
    }

    public String getTPAToggleYes() {
        return TPAToggleYes;
    }

    public String getTPAToggleNo() {
        return TPAToggleNo;
    }

    public String getVariablesEnabled() {
        return VariablesEnabled;
    }

    public String getVariablesDisabled() {
        return VariablesDisabled;
    }

    public String getTPAToggleOther() {
        return TPAToggleOther;
    }

    public String getTPAToggleOtherTargetPlayer() {
        return TPAToggleOtherTargetPlayer;
    }

    public String getTPAPendingRequest() {
        return TPAPendingRequest;
    }

    public String getTPADeny() {
        return TPADeny;
    }

    public String getTPADenyTargetPlayer() {
        return TPADenyTargetPlayer;
    }

    public String getTPANoRequest() {
        return TPANoRequest;
    }

    public String getTPANoRequestPlayer() {
        return TPANoRequestPlayer;
    }

    public String getGlobalInTeleport() {
        return GlobalInTeleport;
    }

    public String getTPADelayInTeleport() {
        return TPADelayInTeleport;
    }

    public String getTPATeleporting() {
        return TPATeleporting;
    }

    public String getTPACanceledMoving() {
        return TPACanceledMoving;
    }

    public String getTPATeleport() {
        return TPATeleport;
    }

    public String getTPATeleportTargetPlayer() {
        return TPATeleportTargetPlayer;
    }

    public List<String> getTPAHereRequest() {
        return TPAHereRequest;
    }

    public List<String> getTPAHereGeyserRequest() {
        return TPAHereGeyserRequest;
    }

    public String getGlobalUpdatedConfig() {
        return GlobalUpdatedConfig;
    }

    public String getGlobalUpdatingConfig() {
        return GlobalUpdatingConfig;
    }

    public String getPrefixHome() {
        return PrefixHome;
    }

    public String getHomeNotEnabled() {
        return HomeNotEnabled;
    }

    public String getHomeDelayInTeleport() {
        return HomeDelayInTeleport;
    }

    public String getHomeTeleporting() {
        return HomeTeleporting;
    }

    public String getHomeCanceledMove() {
        return HomeCanceledMove;
    }

    public String getHomeInCooldown() {
        return HomeInCooldown;
    }

    public String getHomeNotSpecify() {
        return HomeNotSpecify;
    }

    public String getHomeNotExist() {
        return HomeNotExist;
    }

    public String getHomeDeletedError() {
        return HomeDeletedError;
    }

    public String getHomeDeletedSuccessfully() {
        return HomeDeletedSuccessfully;
    }

    public String getHomeSetError() {
        return HomeSetError;
    }

    public String getHomeExists() {
        return HomeExists;
    }

    public String getHomeMaxCount() {
        return HomeMaxCount;
    }

    public String getHomeSetSuccessfully() {
        return HomeSetSuccessfully;
    }

    public String getHomeHasNoHomes() {
        return HomeHasNoHomes;
    }

    public String getHomeNoHomes() {
        return HomeNoHomes;
    }

    public String getHomeHomesOf() {
        return HomeHomesOf;
    }

    public String getHomeYourHomes() {
        return HomeYourHomes;
    }

    public String getTPAInCooldown() {
        return TPAInCooldown;
    }

    public String getVariablesDays() {
        return VariablesDays;
    }

    public String getVariablesHours() {
        return VariablesHours;
    }

    public String getVariablesMinutes() {
        return VariablesMinutes;
    }

    public String getVariablesSeconds() {
        return VariablesSeconds;
    }

    public String getTPACancelTargetPlayer() {
        return TPACancelTargetPlayer;
    }

    public String getTPACancel() {
        return TPACancel;
    }

    public List<String> getUpdateNewUpdate() {
        return UpdateNewUpdate;
    }

    public String getTPANoRequestSentPlayer() {
        return TPANoRequestSentPlayer;
    }

    public String getTPANoRequestSent() {
        return TPANoRequestSent;
    }

    public String getTPATargetPlayerInTeleport() {
        return TPATargetPlayerInTeleport;
    }

    public String getTPAPlayerInTeleport() {
        return TPAPlayerInTeleport;
    }

    public String getVariablesUnlimited() {
        return VariablesUnlimited;
    }

    public String getMigrateErrorInvalidData() {
        return MigrateErrorInvalidData;
    }

    public String getMigrateErrorDataNotSpecified() {
        return MigrateErrorDataNotSpecified;
    }

    public String getMigrateErrorInvalidPlugin() {
        return MigrateErrorInvalidPlugin;
    }

    public String getMigrateErrorPluginNotSpecified() {
        return MigrateErrorPluginNotSpecified;
    }

    public String getResetSuccessfully() {
        return ResetSuccessfully;
    }

    public String getResetUnspecifiedPlayer() {
        return ResetUnspecifiedPlayer;
    }

    public String getResetErrorInvalidValue() {
        return ResetErrorInvalidValue;
    }

    public String getResetErrorUnspecifiedValue() {
        return ResetErrorUnspecifiedValue;
    }

    public String getUpdateChangelogPreviusDescription() {
        return UpdateChangelogPreviusDescription;
    }

    public String getUpdateChangelogPrevius() {
        return UpdateChangelogPrevius;
    }

    public String getUpdateChangelogNextDescription() {
        return UpdateChangelogNextDescription;
    }

    public String getUpdateChangelogNext() {
        return UpdateChangelogNext;
    }

    public String getUpdateChangelogOkDescription() {
        return UpdateChangelogOkDescription;
    }

    public String getUpdateChangelogOk() {
        return UpdateChangelogOk;
    }

    public String getUpdateChangelogSeeFullDescription() {
        return UpdateChangelogSeeFullDescription;
    }

    public String getUpdateChangelogSeeFull() {
        return UpdateChangelogSeeFull;
    }

    public String getUpdateChangelogRegister() {
        return UpdateChangelogRegister;
    }

    public String getUpdateChangelogPagesConsole() {
        return UpdateChangelogPagesConsole;
    }

    public String getUpdateChangelogPages() {
        return UpdateChangelogPages;
    }

    public String getUpdateChangelogExeption() {
        return UpdateChangelogExeption;
    }

    public String getUpdateChangelogErrorInfo() {
        return UpdateChangelogErrorInfo;
    }

    public String getUpdateChangelogNoChangelogsFound() {
        return UpdateChangelogNoChangelogsFound;
    }

    public String getUpdateChangelogInvalidPage() {
        return UpdateChangelogInvalidPage;
    }

    public String getUpdateChangelogEnabledNotify() {
        return UpdateChangelogEnabledNotify;
    }

    public String getUpdateChangelogDisabledNotify() {
        return UpdateChangelogDisabledNotify;
    }

    public String getUpdateChangelogInvalidFormat() {
        return UpdateChangelogInvalidFormat;
    }

    public String getWarpInCooldown() {
        return WarpInCooldown;
    }

    public String getWarpCanceledMove() {
        return WarpCanceledMove;
    }

    public String getWarpDelayInTeleport() {
        return WarpDelayInTeleport;
    }

    public String getWarpSetError() {
        return WarpSetError;
    }

    public String getWarpSetSuccessfully() {
        return WarpSetSuccessfully;
    }

    public String getWarpTeleporting() {
        return WarpTeleporting;
    }

    public String getWarpDeletedSuccessfully() {
        return WarpDeletedSuccessfully;
    }

    public String getWarpDeletedError() {
        return WarpDeletedError;
    }

    public String getWarpNotExist() {
        return WarpNotExist;
    }

    public String getWarpNotEnabled() {
        return WarpNotEnabled;
    }

    public String getWarpNotSpecify() {
        return WarpNotSpecify;
    }

    public String getWarpExeption() {
        return WarpExeption;
    }

    public String getWarpOtherTeleport() {
        return WarpOtherTeleport;
    }

    public String getWarpOtherTeleported() {
        return WarpOtherTeleported;
    }

    public String getWarpWarpsList() {
        return WarpWarpsList;
    }

    public String getWarpNoWarps() {
        return WarpNoWarps;
    }

    public String getSpawnDeletedError() {
        return SpawnDeletedError;
    }

    public String getSpawnSpawnsList() {
        return SpawnSpawnsList;
    }

    public String getSpawnNoSpawns() {
        return SpawnNoSpawns;
    }

    public String getSpawnExeption() {
        return SpawnExeption;
    }
}

