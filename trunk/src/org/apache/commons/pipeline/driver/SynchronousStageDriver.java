/*
 * Licensed to the Apache Software Foundation (ASF) under zero or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.pipeline.driver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pipeline.Feeder;
import org.apache.commons.pipeline.Stage;
import org.apache.commons.pipeline.StageContext;
import static org.apache.commons.pipeline.StageDriver.State.*;
import org.apache.commons.pipeline.StageException;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This is a non-threaded version of the AbstractStageDriver.
 */
public class SynchronousStageDriver extends AbstractStageDriver {
    private final Log log = LogFactory.getLog(SynchronousStageDriver.class);

    //flag describing whether or not the driver is fault tolerant
    private boolean faultTolerant = false;

    //current state of thread processing
    private State currentState = State.STOPPED;

    //queue of objects to be processed that are fed to the driver
    //when it is not in a running state
    private Queue<Object> queue = new LinkedList<Object>();

    //Feeder used to feed objects to this stage
    private final Feeder feeder = new Feeder() {
        public void feed(Object obj) {
            synchronized (SynchronousStageDriver.this) {
                if (currentState != RUNNING) { //enqueue objects if stage has not been started
                    if (currentState == ERROR)
                        throw new IllegalStateException("Unable to process data: driver in fatal error state.");
                    queue.add(obj);
                } else {
                    try {
                        stage.process(obj);
                    } catch (StageException e) {
                        recordProcessingException(obj, e);
                        if (!faultTolerant) throw fatalError(e);
                    }
                }
            }
        }
    };

    /**
     * Creates a new instance of SimpleStageDriver
     *
     * @param stage   The stage to be run
     * @param context The context in which the stage will be run
     */
    public SynchronousStageDriver(Stage stage, StageContext context) {
        super(stage, context);
    }

    /**
     * Get the feeder for the encapsulated stage. Since the SynchronousStageDriver
     * is designed to run the stage in the main thread of execution, calls
     * to {@link Feeder#feed(Object)} on the returned feeder will trigger processing
     * of the object fed to the stage.
     *
     * @return The Feeder instance for the stage.
     */
    public Feeder getFeeder() {
        return this.feeder;
    }

    /**
     * Performs preprocessing and updates the driver state.
     *
     * @throws org.apache.commons.pipeline.StageException
     *          Thrown if the driver is in an illegal state to be started or an error occurs
     *          during preprocessing.
     */
    public synchronized void start() throws StageException {
        if (this.currentState == STOPPED) {
            try {
                stage.preprocess();
            } catch (StageException e) {
                throw fatalError(e);
            }

            this.currentState = RUNNING;
            this.notifyAll();

            // feed any queued values before returning control
            while (!queue.isEmpty()) this.getFeeder().feed(queue.remove());
        } else {
            throw new IllegalStateException("Illegal attempt to start driver from state: " + this.currentState);
        }
    }

    /**
     * Performs postprocessing and releases stage resources, and updates the driver
     * state accordingly.
     *
     * @throws org.apache.commons.pipeline.StageException
     *          Thrown if an error occurs during postprocessing
     */
    public synchronized void finish() throws StageException {
        if (this.currentState == RUNNING) {
            try {
                stage.postprocess();
            } catch (StageException e) {
                throw fatalError(e);
            }

            stage.release();

            this.currentState = STOPPED;
            this.notifyAll();
        } else {
            throw new IllegalStateException("Driver is not running (current state: " + this.currentState + ")");
        }
    }

    /**
     * Accessor for the current state of the stage driver
     *
     * @return the current driver state
     */
    public synchronized State getState() {
        return this.currentState;
    }

    private synchronized RuntimeException fatalError(Throwable t) {
        try {
            this.currentState = ERROR;
            this.recordFatalError(t);
            stage.release();
            this.notifyAll();
        } catch (Exception e) {
            this.recordFatalError(e);
        }

        return new RuntimeException("Fatal error halted processing of stage: " + stage);
    }
}
