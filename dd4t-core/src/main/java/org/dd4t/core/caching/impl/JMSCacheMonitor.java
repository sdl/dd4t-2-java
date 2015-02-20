package org.dd4t.core.caching.impl;

import org.dd4t.core.caching.CacheInvalidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Mihai Cadariu
 */
public class JMSCacheMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(JMSCacheMonitor.class);
    private int monitorServiceInterval = 30000; // milliseconds

	@Autowired
    private CacheInvalidator cacheInvalidator;
    private MQServerStatus serverStatus = MQServerStatus.UP; // assume it's down

    private final Runnable monitor = new Runnable() {
        @Override
        public void run() {
            LOG.debug("Monitor thread running");

            MQServerStatus previousServerStatus = MQServerStatus.UP;
            try {
                while (true) {
                    LOG.debug("JMS MQ server is {} ", serverStatus);

                    if (cacheInvalidator != null) {
                        if (isMQServerDown()) {
                            cacheInvalidator.flush();
                        } else if (previousServerStatus == MQServerStatus.DOWN) {
                            LOG.debug("JMS MQ server recovered. Flush local cache one more time.");
                            cacheInvalidator.flush();
                        }
                        previousServerStatus = serverStatus;
                    }

                    Thread.sleep(monitorServiceInterval);
                }
            } catch (InterruptedException e) {
                LOG.debug("Cache monitor thread interrupted");
            }
        }
    };

    private final Thread thread;

    private JMSCacheMonitor () {
        LOG.debug("Create new instance");

        LOG.debug("Using Monitor interval (or cache refresh time when JMS is down) = {} seconds", monitorServiceInterval, monitorServiceInterval / 1000);

        LOG.debug("Start cache monitor thread");
        thread = new Thread(monitor);
        thread.setName("Dd4tWebAppJMSMonitorThread");
        thread.start();
    }

    public int getMonitorServiceInterval() {
		return monitorServiceInterval;
	}

	public void setMonitorServiceInterval(int monitorServiceInterval) {
		this.monitorServiceInterval = monitorServiceInterval;
	}
	
    public boolean isMQServerUp() {
        return serverStatus == MQServerStatus.UP;
    }

    public boolean isMQServerDown() {
        return serverStatus == MQServerStatus.DOWN;
    }

    public MQServerStatus getMQServerStatus() {
        return serverStatus;
    }

    public void setMQServerStatus(MQServerStatus status) {
        this.serverStatus = status;
    }

    public void shutdown() throws InterruptedException {
        LOG.debug("Stopping thread monitor");

        thread.interrupt();
        thread.join();

        LOG.debug("Thread monitor stopped successfully");
    }

    public void setMQServerStatusDown() {
        if (isMQServerUp()) {
            LOG.debug("Detected MQ server connection dropped. Setting status DOWN");
            setMQServerStatus(MQServerStatus.DOWN);
        }
    }

    public void setMQServerStatusUp() {
        if (isMQServerDown()) {
            LOG.debug("Detected MQ server activity. Setting status UP");
            setMQServerStatus(MQServerStatus.UP);
        }
    }

    public void setCacheInvalidator(CacheInvalidator cacheInvalidator) {
        this.cacheInvalidator = cacheInvalidator;
    }

    public enum MQServerStatus {
        UP, DOWN
    }
}
