package com.github.achmadns.lab;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by achmad on 11/03/16.
 */
public class MetricsTest {

    @Test(groups = "metrics")
    public void report() throws InterruptedException {
        final MetricRegistry registry = new MetricRegistry();
        final Graphite graphite = new Graphite(new InetSocketAddress("graphite.example.com", 2003));
        final GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
                .prefixedWith("com.github.achmadns.lab")
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .build(graphite);
        reporter.start(1, TimeUnit.SECONDS);
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        while (true) {
            final Timer.Context timer = registry.timer("timer.wait").time();
            Thread.sleep(random.nextInt(3000));
            timer.stop();
        }
    }
}
