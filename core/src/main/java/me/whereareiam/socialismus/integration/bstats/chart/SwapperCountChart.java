package me.whereareiam.socialismus.integration.bstats.chart;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.feature.FeatureLoader;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;

@Singleton
public class SwapperCountChart implements Chart {
    private final FeatureLoader featureLoader;
    private Metrics metrics;

    @Inject
    public SwapperCountChart(FeatureLoader featureLoader) {
        this.featureLoader = featureLoader;
    }

    @Override
    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public void addChart() {
        metrics.addCustomChart(
                new SimplePie("swapperCount", this::getData)
        );
    }

    @Override
    public String getData() {
        return String.valueOf(featureLoader.getSwapperCount());
    }
}
