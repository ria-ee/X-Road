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

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UnhandledMessage;
import com.codahale.metrics.JmxReporter;
import com.google.common.collect.Lists;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import ee.ria.xroad.common.SystemProperties;
import ee.ria.xroad.common.SystemPropertiesLoader;
import ee.ria.xroad.monitor.common.SystemMetricNames;
import ee.ria.xroad.signer.protocol.SignerClient;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import static ee.ria.xroad.common.SystemProperties.CONF_FILE_ENV_MONITOR;

/**
 * Main class for monitor application
 */
@Slf4j
public final class MonitorMain {

    static {
        SystemPropertiesLoader.create()
                .withCommonAndLocal()
                .with(CONF_FILE_ENV_MONITOR)
                .load();
    }

    private static final String AKKA_PORT = "akka.remote.netty.tcp.port";

    private static ActorSystem actorSystem;

    /**
     * Main entry point
     *
     * @param args
     */
    public static void main(String args[]) throws Exception {

        log.info("Starting X-Road Environmental Monitoring");
        registerShutdownHook();
        initAkka();
        startReporters();
    }

    private MonitorMain() {
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdownAkka()));
    }

    private static void shutdownAkka() {
        if (actorSystem != null) {
            actorSystem.terminate();
            actorSystem = null;
        }
    }

    private static void initAkka() throws Exception {
        actorSystem = ActorSystem.create("xroad-monitor", loadAkkaConfiguration());
        SignerClient.init(actorSystem);

        ActorRef unhandled = actorSystem.actorOf(Props.create(UnhandledListenerActor.class), "UnhandledListenerActor");
        actorSystem.eventStream().subscribe(unhandled, UnhandledMessage.class);

        actorSystem.actorOf(Props.create(MetricsProviderActor.class), "MetricsProviderActor");
        actorSystem.actorOf(Props.create(SystemMetricsSensor.class), "SystemMetricsSensor");
        actorSystem.actorOf(Props.create(DiskSpaceSensor.class), "DiskSpaceSensor");
        actorSystem.actorOf(Props.create(ExecListingSensor.class), "ExecListingSensor");
        actorSystem.actorOf(Props.create(CertificateInfoSensor.class), "CertificateInfoSensor");

        log.info("akka init complete");
    }

    private static Config loadAkkaConfiguration() {
        log.info("loadAkkaConfiguration");
        final int port = SystemProperties.getEnvMonitorPort();
        return ConfigFactory.load()
                .withValue(AKKA_PORT, ConfigValueFactory.fromAnyRef(port));
    }

    private static void startReporters() {
        JmxReporter jmxReporter = JmxReporter.forRegistry(MetricRegistryHolder.getInstance().getMetrics())
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter((name, metric) -> !Lists.newArrayList(SystemMetricNames.PROCESSES,
                        SystemMetricNames.PACKAGES,
                        SystemMetricNames.CERTIFICATES
                        ).contains(name))
                .build();

        jmxReporter.start();
    }


}
