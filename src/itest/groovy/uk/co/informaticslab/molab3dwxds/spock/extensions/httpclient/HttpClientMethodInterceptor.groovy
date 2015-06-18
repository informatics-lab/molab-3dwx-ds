package uk.co.informaticslab.molab3dwxds.spock.extensions.httpclient

import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FieldInfo
import org.spockframework.runtime.model.SpecInfo

class HttpClientMethodInterceptor extends AbstractMethodInterceptor {

    private final FieldInfo fieldInfo
    private final HTTPClientHelper httpClientHelper

    HttpClientMethodInterceptor(FieldInfo fieldInfo, HTTPClientHelper httpClientHelper) {
        this.fieldInfo = fieldInfo
        this.httpClientHelper = httpClientHelper
    }

    void install(SpecInfo specInfo) {
        specInfo.addInitializerInterceptor(this)
    }

    @Override
    void interceptInitializerMethod(IMethodInvocation invocation) throws Throwable {
        fieldInfo.writeValue(invocation.instance, httpClientHelper)
    }
}
