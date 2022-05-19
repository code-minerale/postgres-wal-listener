package com.fiko.cdc.listener;

import io.debezium.config.Configuration;
import io.debezium.embedded.EmbeddedEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.fiko.cdc.service.TestService;
import com.fiko.cdc.utils.Operation;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static io.debezium.data.Envelope.FieldName.*;

/**
 * This class creates, starts and stops the EmbeddedEngine, which starts the Debezium engine. The engine also
 * loads and launches the connectors setup in the configuration.
 * <p>
 * The class uses @PostConstruct and @PreDestroy functions to perform needed operations.
 *
 * @author Fiko
 */
@Slf4j
@Component
public class CDCListener {

    /**
     * Single thread pool which will run the Debezium engine asynchronously.
     */
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * The Debezium engine which needs to be loaded with the configurations, Started and Stopped - for the
     * CDC to work.
     */
    private final EmbeddedEngine engine;

    /**
     * Handle to the Service layer
     */
    private final TestService testService;

    /**
     * Constructor which loads the configurations and sets a callback method 'handleEvent', which is invoked when
     * a DataBase transactional operation is performed.
     *
     * @param testConnector
     * @param testService
     */
    private CDCListener(Configuration testConnector, TestService testService) {
        this.engine = EmbeddedEngine
                .create()
                .using(testConnector)
                .notifying(this::handleEvent).build();

        this.testService = testService;
    }

    /**
     * The method is called after the Debezium engine is initialized and started asynchronously using the Executor.
     */
    @PostConstruct
    private void start() {
        this.executor.execute(engine);
    }

    /**
     * This method is called when the container is being destroyed. This stops the debezium, merging the Executor.
     */
    @PreDestroy
    private void stop() {
        if (this.engine != null) {
            this.engine.stop();
        }
    }

    /**
     * This method is invoked when a transactional action is performed on any of the tables that were configured.
     *
     * @param sourceRecord
     */
    private void handleEvent(SourceRecord sourceRecord) {
        Struct sourceRecordValue = (Struct) sourceRecord.value();
        log.info("Data : {}", sourceRecordValue);

        Operation operation = Operation.forCode((String) sourceRecordValue.get(OPERATION));
        
        Struct afterData = (Struct) sourceRecordValue.get(AFTER);
        log.info("Data Changed: {} with Operation: {}", afterData, operation.name());

        // call service here
        
    }
}