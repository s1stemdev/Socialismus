package me.whereareiam.socialismus.integration.bstats;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.whereareiam.socialismus.integration.Integration;
import me.whereareiam.socialismus.integration.IntegrationType;
import me.whereareiam.socialismus.integration.bstats.chart.HookCountChart;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class bStats implements Integration {
    private final Injector injector;
    private final Plugin plugin;
    private Metrics metrics;

    private boolean isEnabled;

    @Inject
    public bStats(Injector injector, Plugin plugin) {
        this.injector = injector;
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        metrics = new Metrics((JavaPlugin) plugin, 19855);

        try {
            File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bstats/config.yml");
            if (!bStatsFolder.exists()) {
                isEnabled = false;
            } else {
                FileConfiguration bStatsConfig = YamlConfiguration.loadConfiguration(bStatsFolder);
                isEnabled = bStatsConfig.getBoolean("enabled", false);
            }
        } catch (NullPointerException e) {
            isEnabled = false;
        }

        registerCharts();
    }

    @Override
    public String getName() {
        return "bStats";
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IntegrationType getType() {
        return IntegrationType.INTERNAL;
    }

    private void registerCharts() {
        new HookCountChart(injector, metrics).addChart();
    }
}

