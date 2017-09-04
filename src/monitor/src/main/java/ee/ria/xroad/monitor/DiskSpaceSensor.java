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

import java.io.File;
import java.util.concurrent.TimeUnit;

import ee.ria.xroad.common.SystemProperties;
import ee.ria.xroad.monitor.common.SystemMetricNames;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * Collects disk space information
 */
@Slf4j
public class DiskSpaceSensor extends AbstractSensor {


    public DiskSpaceSensor() {
        log.info("Creating sensor, measurement interval: {}", getInterval());
        scheduleSingleMeasurement(getInterval(), new DiskSpaceMeasure());
    }

    private void updateMetrics() {
        File[] roots = File.listRoots();
        if (roots != null && roots.length > 0) {
            final MetricRegistryHolder registryHolder = MetricRegistryHolder.getInstance();
            for (File drive: roots) {
                SimpleSensor<Long> total = registryHolder.getOrCreateSimpleSensor(
                        String.format("%s_%s", SystemMetricNames.DISK_SPACE_TOTAL, drive));

                SimpleSensor<Long> free = registryHolder.getOrCreateSimpleSensor(
                        String.format("%s_%s", SystemMetricNames.DISK_SPACE_FREE, drive));

                total.update(drive.getTotalSpace());
                free.update(drive.getFreeSpace());
            }
        }
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof DiskSpaceMeasure) {
            log.debug("Updating metrics");
            updateMetrics();
            scheduleSingleMeasurement(getInterval(), new DiskSpaceMeasure());
        }
    }

    @Override
    protected FiniteDuration getInterval() {
        return Duration.create(SystemProperties.getEnvMonitorDiskSpaceSensorInterval(), TimeUnit.SECONDS);
    }

    private static class DiskSpaceMeasure { }

    @AllArgsConstructor
    @Getter
    private static class SensorPair {
        private final SimpleSensor<Long> total;
        private final SimpleSensor<Long> free;
    }
}
