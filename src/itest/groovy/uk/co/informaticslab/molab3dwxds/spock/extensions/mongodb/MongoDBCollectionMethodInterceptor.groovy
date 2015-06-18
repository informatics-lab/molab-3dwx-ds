package uk.co.informaticslab.molab3dwxds.spock.extensions.mongodb

import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FieldInfo
import org.spockframework.runtime.model.SpecInfo

class MongoDBCollectionMethodInterceptor extends AbstractMethodInterceptor {

    static MONGO_DB_URL = "mongodb://localhost:27017"

    private final FieldInfo fieldInfo
    private final MongoDBCollectionHelper collectionHelper

    MongoDBCollectionMethodInterceptor(FieldInfo fieldInfo, String database, String collection, Map<String, ?> options, List<Map<String, ?>> indexes) {
        this.fieldInfo = fieldInfo
        this.collectionHelper = new MongoDBCollectionHelper(MONGO_DB_URL, database, collection, options, indexes)
        collectionHelper.drop()
    }

    void install(SpecInfo specInfo) {
        specInfo.addSetupInterceptor(this)
        specInfo.addCleanupInterceptor(this)
    }

    @Override
    void interceptSetupMethod(IMethodInvocation invocation) throws Throwable {
        fieldInfo.writeValue(invocation.instance, collectionHelper)
        invocation.proceed()
    }

    @Override
    void interceptCleanupMethod(IMethodInvocation invocation) throws Throwable {
        collectionHelper.drop()
        invocation.proceed()
    }
}
