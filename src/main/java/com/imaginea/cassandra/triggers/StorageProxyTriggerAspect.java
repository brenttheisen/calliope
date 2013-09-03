package com.imaginea.cassandra.triggers;

import org.apache.cassandra.db.IMutation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


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