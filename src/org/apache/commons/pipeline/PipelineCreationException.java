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

package org.apache.commons.pipeline;

/**
 * This is a wrapper exception for use by {@link PipelineFactory}s.
 */
public class PipelineCreationException extends java.lang.Exception {

    /**
     * Creates a new instance of <code>PipelineCreationException</code> without detail message.
     */
    public PipelineCreationException() {
    }

    /**
     * Constructs an instance of <code>PipelineCreationException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PipelineCreationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>PipelineCreationException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PipelineCreationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an instance of <code>PipelineCreationException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PipelineCreationException(Throwable cause) {
        super(cause);
    }
}
