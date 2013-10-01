/*
 * Licensed to Tuplejump Software Pvt. Ltd. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Tuplejump Software Pvt. Ltd. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tuplejump.calliope.streaming;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.apache.cassandra.db.IMutation;


/**
 * User: Suresh
 * Date: 13/8/13
 * Time: 11:04 AM
 */

@Aspect
public class StorageProxyTriggerAspect {

    private static Logger logger = LoggerFactory.getLogger(StorageProxyTriggerAspect.class);

    @Around("execution(* org.apache.cassandra.service.StorageProxy.mutateAtomically(..)) " +
            "|| execution(* org.apache.cassandra.service.StorageProxy.mutate(..))")
    public void processMutations(ProceedingJoinPoint thisJoinPoint) throws Throwable {

        logger.info("ASPECT HANDLER CALLED");

        @SuppressWarnings("unchecked")
        List<IMutation> mutations = (List<IMutation>) thisJoinPoint.getArgs()[0];
        thisJoinPoint.proceed(thisJoinPoint.getArgs());
        TriggerExecutor.instance.execute(mutations);

    }


}