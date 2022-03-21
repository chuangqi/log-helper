package com.redick.aop.proxy;

import org.aspectj.lang.Signature;

import java.lang.reflect.Method;

/**
 * JoinPoint Interface.
 * @author Redick01
 */
public interface AroundLogProxyChain {
    /**
     * get parameters
     * @return parameters
     */
    Object[] getArgs();

    /**
     * get target object.
     * @return target object.
     */
    Object getTarget();

    /**
     * get method.
     * @return Method
     */
    Method getMethod();

    /**
     * target class object.
     * @return class object
     */
    Class<?> getClazz();

    /**
     * exec JoinPoint
     * @return JoinPoint result
     */
    @SuppressWarnings("all")
    Object getProceed() throws Throwable;

    /**
     * Signature
     * @return Signature.
     */
    Signature getSignature();

    /**
     * exec proxy
     * @param arguments parameter
     * @return exec result
     * @throws Throwable Throwable
     */
    Object doProxyChain(Object[] arguments) throws Throwable;
}
