package dev.prisonbreak.managers;

import dev.prisonbreak.PrisonBreak;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Tüm spawn/escape/arrest noktalarını config'e kaydeder ve yükler.
 */
public class LocationManager {

    private final PrisonBreak plugin;

    private Location prisonerSpawn;
    private Location policeSpawn;
    private Location escapePoint;
    private Location arrestPoint;
    private Location cellSpawn;

    public LocationManager(PrisonBreak plugin) {
        this.plugin = plugin;
        loadLocations();
    }

    // ─── Load ────────────────────────────────────────────────────────────────

    public void loadLocations() {
        FileConfiguration cfg = plugin.getConfig();
        prisonerSpawn = loadLocation(cfg, "locations.prisoner-spawn");
        policeSpawn   = loadLocation(cfg, "locations.police-spawn");
        escapePoint   = loadLocation(cfg, "locations.escape-point");
        arrestPoint   = loadLocation(cfg, "locations.arrest-point");
        cellSpawn     = loadLocation(cfg, "locations.cell-spawn");
    }

    private Location loadLocation(FileConfiguration cfg, String path) {
        if (!cfg.isConfigurationSection(path)) return null;
        String worldName = cfg.getString(path + ".world");
        if (worldName == null) return null;
        World world = plugin.getServer().getWorld(worldName);
        if (world == null) return null;
        double x = cfg.getDouble(path + ".x");
        double y = cfg.getDouble(path + ".y");
        double z = cfg.getDouble(path + ".z");
        float yaw   = (float) cfg.getDouble(path + ".yaw");
        float pitch = (float) cfg.getDouble(path + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    // ─── Save ────────────────────────────────────────────────────────────────

    public void saveLocation(String key, Location loc) {
        String path = "locations." + key;
        plugin.getConfig().set(path + ".world", loc.getWorld().getName());
        plugin.getConfig().set(path + ".x", loc.getX());
        plugin.getConfig().set(path + ".y", loc.getY());
        plugin.getConfig().set(path + ".z", loc.getZ());
        plugin.getConfig().set(path + ".yaw",   (double) loc.getYaw());
        plugin.getConfig().set(path + ".pitch", (double) loc.getPitch());
        plugin.saveConfig();
        loadLocations(); // Reload after save
    }

    // ─── Setters ─────────────────────────────────────────────────────────────

    public void setPrisonerSpawn(Location loc) {
        prisonerSpawn = loc;
        saveLocation("prisoner-spawn", loc);
    }

    public void setPoliceSpawn(Location loc) {
        policeSpawn = loc;
        saveLocation("police-spawn", loc);
    }

    public void setEscapePoint(Location loc) {
        escapePoint = loc;
        saveLocation("escape-point", loc);
    }

    public void setArrestPoint(Location loc) {
        arrestPoint = loc;
        saveLocation("arrest-point", loc);
    }

    public void setCellSpawn(Location loc) {
        cellSpawn = loc;
        saveLocation("cell-spawn", loc);
    }

    // ─── Getters ─────────────────────────────────────────────────────────────

    public Location getPrisonerSpawn() { return prisonerSpawn; }
    public Location getPoliceSpawn()   { return policeSpawn; }
    public Location getEscapePoint()   { return escapePoint; }
    public Location getArrestPoint()   { return arrestPoint; }
    public Location getCellSpawn()     { return cellSpawn; }

    // ─── Validation ──────────────────────────────────────────────────────────

    /**
     * Oyunu başlatmak için tüm noktalar ayarlanmış mı?
     */
    public boolean isFullySetup() {
        return prisonerSpawn != null
            && policeSpawn != null
            && escapePoint != null
            && arrestPoint != null
            && cellSpawn != null;
    }

    public String getMissingSetup() {
        StringBuilder sb = new StringBuilder();
        if (prisonerSpawn == null) sb.append("prisoner-spawn, ");
        if (policeSpawn == null)   sb.append("police-spawn, ");
        if (escapePoint == null)   sb.append("escape-point, ");
        if (arrestPoint == null)   sb.append("arrest-point, ");
        if (cellSpawn == null)     sb.append("cell-spawn, ");
        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
