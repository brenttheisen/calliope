/**
 * Licensed to Tuplejump Software Pvt. Ltd. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Tuplejump Software Pvt. Ltd. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.cassandra;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraClientHolder
{
    private static final Logger log = LoggerFactory.getLogger(CassandraClientHolder.class);

    private Cassandra.Client client;
    private final TTransport transport;
    private String keyspace;

    public CassandraClientHolder(TTransport transport) throws CassandraException
    {
        this(transport, null);
    }

    public CassandraClientHolder(TTransport transport, String keyspace) throws CassandraException

    {
        this.transport = transport;
        this.keyspace = keyspace;
        initClient();
    }

    public boolean isOpen()
    {
        return client != null && transport != null && transport.isOpen();
    }

    public String getKeyspace()
    {
        return keyspace;
    }

    private void initClient() throws CassandraException
    {
        try
        {
            transport.open();
        } catch (TTransportException e)
        {
            throw new CassandraException("unable to connect to server", e);
        }

        client = new Cassandra.Client(new TBinaryProtocol(transport));

        // connect to last known keyspace
        setKeyspace(keyspace);
    }

    /**
     * Set the client with the (potentially) new keyspace. Safe to call this
     * repeatedly with the same keyspace.
     * @param keyspace
     * @return
     * @throws CassandraException
     */
    public void setKeyspace(String ks) throws CassandraException
    {
        if ( ks == null )
        {
            return;
        }

        if (keyspace == null || !StringUtils.equals(keyspace, ks))
        {
            try
            {
                this.keyspace = ks;
                client.set_keyspace(keyspace);
            } catch (InvalidRequestException e)
            {
                throw new CassandraException(e);
            } catch (TException e)
            {
                throw new CassandraException(e);
            }
        }
    }

    public Cassandra.Client getClient()
    {
        return client;
    }



    public void close()
    {
        if ( transport == null || !transport.isOpen() )
        {
            return;
        }
        try
        {
            transport.flush();
        } catch (Exception e)
        {
            log.error("Could not flush transport for client holder: "+ toString(), e);
        } finally
        {
            try
            {
                if (transport.isOpen())
                {
                    transport.close();
                }
            } catch (Exception e)
            {
                log.error("Error on transport close for client: " + toString(),e);
            }
        }
    }




}
