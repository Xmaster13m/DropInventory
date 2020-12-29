package java.me.xmaster13.dropinventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

    int currentConfigVersion = 1;

    BlockDropWrapper blockDropWrapper;
    DropHandler dropHandler;
    PluginUpdateChecker updateChecker;
    Messages messages;
    Utils utils;
    MendingUtils mendingUtils;
    IngotCondenser ingotCondenser;
    ItemSpawnListener itemSpawnListener;

    HashMap<String, PlayerSetting> perPlayerSettings;

    ArrayList<Material> disabledBlocks;
    ArrayList<String> disabledWorlds;

    boolean blocksIsWhitelist = false;

    ArrayList<String> disableMobs;

    boolean mobsIsWhitelist = false;
    boolean autoCondense = false;

    final int mcVerison = Utils.getMcVersion(Bukkit.getBukkitVersion());
    boolean usingMatchingConfig = true;
    boolean enabledByDefault = false;
    boolean showMessageWhenBreakingBlock = true;
    boolean showMessageWhenBreakingBlockAndCollectionIsDisabled = false;
    boolean showMessageAgainAfterLogout = true;

    private static final int updateCheckInterval = 86400;

    boolean debug = false;

    public void onEnabled() {

        createConfig();

        perPlayerSettings = new HashMap<String, PlayerSetting>();
        messages = new Messages(this);
        ingotCondenser = new IngotCondenser(this);
        itemSpawnListener = new ItemSpawnListener(this);
        CommandDropinv commandDropinv = new CommandDropinv(this);

        enabledByDefault = getConfig().getBoolean("enabled-by-default");
        showMessageWhenBreakingBlock = getConfig().getBoolean("show-message-when-breaking-block");
        showMessageWhenBreakingBlockAndCollectionIsDisabled = getConfig().getBoolean("show-message-when-breaking-block-and-collection-is-disabled");
        showMessageAgainAfterLogout = getConfig().getBoolean("show-message-again-after-logout");
        autoCondense = getConfig().getBoolean("auto-condense");

        this.getServer().getPluginManager().registerEvents(new Listener(this), this);
        this.getServer().getPluginManager().registerEvents(itemSpawnListener,this);
        if(mcVerison>=13) {
            this.getServer().getPluginManager().registerEvents(new DropListener(this),this);
            debug("La version del MC esta por encima de la 1.13 usando BlocDropItemEvent");
        } else {
            blockDropWrapper = new BlockDropWrapper();
            dropHandler = new DropHandler(this);
            this.getServer().getPluginManager().registerEvents(new DropListenerLegacy(this),this);
            debug("La version del MC es 1.12 o inferior usando BlockBreakEvent");
            
        }

    }

    void debug(String t) {
        if(debug) getLogger().warning("[DEBUG] "+t);
    }
}
