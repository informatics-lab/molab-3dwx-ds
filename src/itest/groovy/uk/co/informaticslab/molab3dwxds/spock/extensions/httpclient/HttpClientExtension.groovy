package uk.co.informaticslab.molab3dwxds.spock.extensions.httpclient

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FieldInfo

class HttpClientExtension extends AbstractAnnotationDrivenExtension<HttpClient> {

    @Override
    void visitFieldAnnotation(HttpClient httpClient, FieldInfo fieldInfo) {

        def httpClientHelper = new HTTPClientHelper("http://localhost:9000")

        def methodInterceptor = new HttpClientMethodInterceptor(fieldInfo, httpClientHelper)
        methodInterceptor.install(fieldInfo.parent)
    }
}
