/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.usecases;

import javax.jms.Topic;
import javax.jms.Connection;

import org.apache.hedwig.JmsTestBase;
import org.apache.hedwig.jms.SessionImpl;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import junit.framework.TestCase;
import org.apache.hedwig.jms.spi.HedwigConnectionFactoryImpl;
import org.apache.hedwig.jms.message.MessageImpl;


import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Useful base class for unit test cases
 */
// Does not contain any testcases.
@Ignore
public class TestSupport extends JmsTestBase {
    private static final Logger LOG = LoggerFactory.getLogger(TestSupport.class);

    protected HedwigConnectionFactoryImpl connectionFactory;
    protected boolean topic = true;

    public TestSupport() {
        super();
    }

    public TestSupport(String name) {
        super(name);
    }

    protected MessageImpl createMessage() {
        return new MessageImpl(null);
    }

    protected Destination createDestination(String subject) {
        if (topic) {
            return SessionImpl.asTopic(subject);
        } else {
            return SessionImpl.asTopic(subject);
        }
    }

    protected void assertTextMessagesEqual(Message[] firstSet, Message[] secondSet) throws JMSException {
        assertTextMessagesEqual("", firstSet, secondSet);
    }

    /**
     * @param messsage
     * @param firstSet
     * @param secondSet
     */
    protected void assertTextMessagesEqual(String messsage, Message[] firstSet,
                                           Message[] secondSet) throws JMSException {
        assertEquals("Message count does not match: " + messsage, firstSet.length, secondSet.length);
        for (int i = 0; i < secondSet.length; i++) {
            TextMessage m1 = (TextMessage)firstSet[i];
            TextMessage m2 = (TextMessage)secondSet[i];
            assertTextMessageEqual("Message " + (i + 1) + " did not match : ", m1, m2);
        }
    }

    protected void assertEquals(TextMessage m1, TextMessage m2) throws JMSException {
        assertEquals("", m1, m2);
    }

    /**
     * @param message
     * @param firstSet
     * @param secondSet
     */
    protected void assertTextMessageEqual(String message, TextMessage m1, TextMessage m2) throws JMSException {
        assertFalse(message + ": expected {" + m1 + "}, but was {" + m2 + "}", m1 == null ^ m2 == null);
        if (m1 == null) {
            return;
        }
        assertEquals(message, m1.getText(), m2.getText());
    }

    protected void assertEquals(Message m1, Message m2) throws JMSException {
        assertEquals("", m1, m2);
    }

    /**
     * @param message
     * @param firstSet
     * @param secondSet
     */
    protected void assertEquals(String message, Message m1, Message m2) throws JMSException {
        assertFalse(message + ": expected {" + m1 + "}, but was {" + m2 + "}", m1 == null ^ m2 == null);
        if (m1 == null) {
            return;
        }
        assertTrue(message + ": expected {" + m1 + "}, but was {" + m2 + "}", m1.getClass() == m2.getClass());
        if (m1 instanceof TextMessage) {
            assertTextMessageEqual(message, (TextMessage)m1, (TextMessage)m2);
        } else {
            assertEquals(message, m1, m2);
        }
    }

    protected HedwigConnectionFactoryImpl createConnectionFactory() throws Exception {
        return new HedwigConnectionFactoryImpl();
    }

    /**
     * Factory method to create a new connection
     */
    protected Connection createConnection() throws Exception {
        return getConnectionFactory().createConnection();
    }

    public HedwigConnectionFactoryImpl getConnectionFactory() throws Exception {
        if (connectionFactory == null) {
            connectionFactory = createConnectionFactory();
            assertTrue("Should have created a connection factory!", connectionFactory != null);
        }
        return connectionFactory;
    }

    protected String getConsumerSubject() {
        return getSubject();
    }

    protected String getProducerSubject() {
        return getSubject();
    }

    protected String getSubject() {
        return getClass().getName() + "." + getName();
    }
}
