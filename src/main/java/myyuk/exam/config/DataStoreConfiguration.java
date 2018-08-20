package myyuk.exam.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.*;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Ignite configuration for spring framework.
 * @see IgniteConfiguration
 */
@Configuration
public class DataStoreConfiguration {

    @Value("${ignite.maxMemory}")
    private long maxMemory;

    @Autowired
    @Bean(destroyMethod = "close")
    public Ignite ignite(IgniteConfiguration igniteConfiguration) throws IgniteException {
        Ignite ignite = Ignition.getOrStart(igniteConfiguration);
        ignite.cluster().active();
        return ignite;
    }

    @Bean
    public IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration ignite = new IgniteConfiguration();
        ignite.setClientMode(false); // Server mode
        ignite.setPeerClassLoadingEnabled(true);

        ignite.setGridLogger(new Slf4jLogger());

        DataRegionConfiguration todoRegion = new DataRegionConfiguration();
        todoRegion.setName("TODO_REGION");
        todoRegion.setMaxSize(maxMemory);
        todoRegion.setPersistenceEnabled(false);

        DataStorageConfiguration dataStorage = new DataStorageConfiguration();
        dataStorage.setDefaultDataRegionConfiguration(todoRegion);
        ignite.setDataStorageConfiguration(dataStorage);

        CacheConfiguration todoCache = new CacheConfiguration();
        todoCache.setName("TODO");
        todoCache.setBackups(0);
        todoCache.setCacheMode(CacheMode.LOCAL);
        todoCache.setDataRegionName("TODO_REGION");
        ignite.setCacheConfiguration(todoCache);

        AtomicConfiguration atomic = new AtomicConfiguration();
        atomic.setCacheMode(CacheMode.LOCAL);
        atomic.setBackups(0);
        atomic.setAtomicSequenceReserveSize(5000);
        ignite.setAtomicConfiguration(atomic);

        TcpCommunicationSpi tcpCommunicationSpi = new TcpCommunicationSpi();
        tcpCommunicationSpi.setSlowClientQueueLimit(1000);
        ignite.setCommunicationSpi(tcpCommunicationSpi);

        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder = new TcpDiscoveryVmIpFinder();
        tcpDiscoveryVmIpFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47509"));
        tcpDiscoverySpi.setIpFinder(tcpDiscoveryVmIpFinder);
        ignite.setDiscoverySpi(tcpDiscoverySpi);

        return ignite;
    }
}
