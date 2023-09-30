package io.github.ran.minecraft.ranitils.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.*;


@Config(name = "ranitils")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip // This might be called anyArmor
    public boolean wearableItems = true;

    @ConfigEntry.Gui.Tooltip
    public boolean bowInstantKill = false;
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 16, max = 512)
    public int bowInstantKillCharges = 100;

    @ConfigEntry.Gui.Tooltip
    public boolean autoSwitchElytra = true;

    @ConfigEntry.Gui.Tooltip // This might be called elytraBounce
    public boolean elytraRecast = false;

    @ConfigEntry.Gui.Tooltip
    public boolean roadRunner = false;

    @ConfigEntry.Gui.Tooltip
    public boolean waypointCoordinateConvert = true;
    @ConfigEntry.Gui.Tooltip
    public boolean noWaypointBeam = false;
    @ConfigEntry.Gui.Tooltip
    public boolean waypointSquareIcon = false;

    @ConfigEntry.Gui.Tooltip
    public boolean verticalTP = false;
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 5)
    public double verticalTPHorizontalDistance = 0.8;

    @ConfigEntry.Gui.Tooltip
    public boolean mineFree = false;

    @ConfigEntry.Gui.Tooltip
    public boolean tridentExploit = false;

    @ConfigEntry.Gui.Tooltip
    public boolean noTrample = true;

    @ConfigEntry.Gui.Tooltip
    public boolean perfectHorse = true;

    @ConfigEntry.Gui.Tooltip
    public boolean bambooNoCollide = true;

    public static void init() {
        System.out.println("hewoalo");
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
    }

    public static ModConfig getInstance() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static ConfigHolder<?> getConfigHolder() {
        return AutoConfig.getConfigHolder(ModConfig.class);
    }
}