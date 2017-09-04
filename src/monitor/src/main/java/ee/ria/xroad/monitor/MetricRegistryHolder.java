/**
 * The MIT License
 * Copyright (c) 2015 Estonian Information System Authority (RIA), Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ee.ria.xroad.monitor;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SlidingTimeWindowReservoir;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Global access point for {@link MetricRegistry}
 */
public final class MetricRegistryHolder {

    private static final int MINUTES_IN_HOUR = 60;
    private static final MetricRegistryHolder INSTANCE = new MetricRegistryHolder();

    private MetricRegistry metrics;

    private MetricRegistryHolder() {
        metrics = new MetricRegistry();
    }

    /**
     * Get Singleton instance
     * @return instance
     */
    public static MetricRegistryHolder getInstance() {
        return INSTANCE;
    }

    /**
     * Get {@link MetricRegistry}
     * @return {@link MetricRegistry}
     */
    public MetricRegistry getMetrics() {
        return metrics;
    }

    /**
     * Set Singleton instance (for testing purposes)
     */
    public void setMetrics(MetricRegistry metricRegistry) {
        this.metrics = metricRegistry;
    }



    /**
     * Either registers a new sensor to metricRegistry, or reuses already registered one
     */
    public <T extends Serializable> SimpleSensor<T> getOrCreateSimpleSensor(String metricName) {
        SimpleSensor<T> typeDefiningSensor = (SimpleSensor) metrics.getMetrics().get(metricName);
        if (typeDefiningSensor == null) {
            typeDefiningSensor = new SimpleSensor<>();
            metrics.register(metricName, typeDefiningSensor);
        }
        return typeDefiningSensor;
    }

    /**
     * If histogram doesn't exist, register default
     */
    public Histogram getOrCreateHistogram(String metricName) {
        Histogram histogram = metrics.getHistograms().get(metricName);
        if (histogram == null) {
            metrics.register(metricName, createDefaultHistogram());
        }
        return histogram;
    }



    private Histogram createDefaultHistogram() {
        return new Histogram(new SlidingTimeWindowReservoir(MINUTES_IN_HOUR, TimeUnit.MINUTES));
    }
}
