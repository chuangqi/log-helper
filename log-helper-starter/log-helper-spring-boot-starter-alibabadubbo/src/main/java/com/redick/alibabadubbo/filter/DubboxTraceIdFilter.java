package com.redick.alibabadubbo.filter;

import static com.redick.constant.TraceTagConstant.DUBBO_INVOKE_AFTER;
import static com.redick.constant.TraceTagConstant.DUBBO_INVOKE_BEFORE;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;

/**
 * @author liupenghui
 *  2020/12/26 11:47 下午
 */
@Activate(group = {Constants.CONSUMER_SIDE, Constants.PROVIDER_SIDE})
@Slf4j
public class DubboxTraceIdFilter extends AbstractInterceptor implements Filter {

    @Trace
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            String side = invoker.getUrl().getParameter(Constants.SIDE_KEY);
            if (Constants.PROVIDER_SIDE.equals(side)) {
                // provider get trace information form attachment
                String traceId = RpcContext.getContext().getAttachment(LogUtil.kLOG_KEY_TRACE_ID);
                String spanId = RpcContext.getContext().getAttachment(Tracer.SPAN_ID);
                String parentId = RpcContext.getContext().getAttachment(Tracer.PARENT_ID);
                Tracer.trace(traceId, spanId, parentId);
            } else {
                log.info(LogUtil.marker(invocation.getArguments()), "调用接口[{}]的方法[{}]", invoker.getInterface().getSimpleName(),
                        invocation.getMethodName());
                executeBefore(DUBBO_INVOKE_BEFORE);
                // consumer set trace information to attachment
                String traceId = traceId();
                if (StringUtils.isNotBlank(traceId)) {
                    RpcContext.getContext().setAttachment(Tracer.TRACE_ID, traceId);
                    RpcContext.getContext().setAttachment(Tracer.SPAN_ID, spanId());
                    RpcContext.getContext().setAttachment(Tracer.PARENT_ID, parentId());
                }
            }
            return invoker.invoke(invocation);
        } finally {
            executeAfter(DUBBO_INVOKE_AFTER);
        }
    }
}
