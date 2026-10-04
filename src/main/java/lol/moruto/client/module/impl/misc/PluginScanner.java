package lol.moruto.client.module.impl.misc;

import com.mojang.brigadier.tree.RootCommandNode;
import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import net.minecraft.client.MinecraftClient;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.text.Text;

import java.util.*;

public class PluginScanner extends Module {
    private static final Set<String> VANILLA = Set.of(
            "advancement", "attribute", "ban", "ban-ip", "banlist", "bossbar",
            "clear", "clone", "data", "datapack", "debug", "defaultgamemode",
            "deop", "difficulty", "effect", "enchant", "execute", "experience",
            "fill", "forceload", "function", "gamemode", "gamerule", "give",
            "help", "item", "kick", "kill", "list", "locate", "loot", "me",
            "msg", "op", "pardon", "pardon-ip", "particle", "playsound",
            "publish", "recipe", "reload", "ride", "save-all", "save-off",
            "save-on", "say", "schedule", "scoreboard", "seed", "setblock",
            "setidletimeout", "setworldspawn", "spawnpoint", "spectate",
            "spreadplayers", "stop", "stopsound", "summon", "tag", "team",
            "teammsg", "teleport", "tell", "tellraw", "time", "title", "tm",
            "tp", "trigger", "weather", "whitelist", "worldborder", "xp"
    );

    private static final Map<String, String> ALIASES = Map.ofEntries(
            Map.entry("essentials", "Essentials"),
            Map.entry("essentialsx", "EssentialsX"),
            Map.entry("worldedit", "WorldEdit"),
            Map.entry("worldguard", "WorldGuard"),
            Map.entry("luckperms", "LuckPerms"),
            Map.entry("lp", "LuckPerms"),
            Map.entry("vault", "Vault"),
            Map.entry("citizens", "Citizens"),
            Map.entry("cmi", "CMI"),
            Map.entry("multiverse", "Multiverse"),
            Map.entry("viaversion", "ViaVersion"),
            Map.entry("protocollib", "ProtocolLib"),
            Map.entry("coreprotect", "CoreProtect"),
            Map.entry("placeholderapi", "PlaceholderAPI"),
            Map.entry("spark", "spark"),
            Map.entry("skinsrestorer", "SkinsRestorer"),
            Map.entry("authme", "AuthMe"),
            Map.entry("geyser", "Geyser"),
            Map.entry("floodgate", "Floodgate"),
            Map.entry("plotsquared", "PlotSquared"),
            Map.entry("tab", "TAB"),
            Map.entry("deluxemenus", "DeluxeMenus"),
            Map.entry("crazycrates", "CrazyCrates"),
            Map.entry("vulcan", "Vulcan"),
            Map.entry("grimac", "GrimAC"),
            Map.entry("matrix", "Matrix"),
            Map.entry("spartan", "Spartan")
    );

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final Set<String> found = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    private int requestId;
    private boolean scanning;

    public PluginScanner() {
        super("Plugin Scanner", "Scans the server's plugins", Category.MISC);
    }

    @Override
    public void onEnable() {
        found.clear();
        requestId = 0;

        if (mc.getNetworkHandler() == null)
            return;

        scanning = true;

        msg("§fScanning...");

        RootCommandNode<?> root = mc.getNetworkHandler()
                .getCommandDispatcher()
                .getRoot();

        for (CommandNode<?> node : root.getChildren())
            check(node.getName());

        probe();
    }

    @Override
    public void onDisable() {
        scanning = false;
    }

    public void onUpdate() {
        if (!scanning)
            return;

        finish();
    }

    private void probe() {
        if (mc.getNetworkHandler() == null)
            return;

        mc.getNetworkHandler().getConnection().send(
                new RequestCommandCompletionsC2SPacket(++requestId, "/")
        );
    }

    public void handleSuggestions(com.mojang.brigadier.suggestion.Suggestions suggestions) {
        if (!scanning)
            return;

        suggestions.getList().forEach(s -> check(s.getText()));
    }

    private void check(String value) {
        if (value == null || value.isBlank())
            return;

        value = value.replace("/", "").trim();

        if (!value.contains(":"))
            return;

        String namespace = value.substring(0, value.indexOf(':')).toLowerCase(Locale.ROOT);

        if (namespace.isBlank() || namespace.equals("minecraft") || namespace.equals("brigadier") || namespace.equals("bukkit") || namespace.equals("spigot") || namespace.equals("paper"))
            return;

        found.add(ALIASES.getOrDefault(namespace, capitalize(namespace)));
    }

    private void finish() {
        scanning = false;

        if (found.isEmpty()) {
            msg("§fNo plugins detected.");
            return;
        }

        msg("§fPlugins §8(" + found.size() + "§8): §a" + String.join("§7, §a", found));
        toggle();
    }

    private void msg(String message) {
        mc.inGameHud.getChatHud().addMessage(
                Text.literal("§8[§bPluginScanner§8] " + message)
        );
    }

    private String capitalize(String value) {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    public Set<String> getFoundPlugins() {
        return Collections.unmodifiableSet(found);
    }
}
