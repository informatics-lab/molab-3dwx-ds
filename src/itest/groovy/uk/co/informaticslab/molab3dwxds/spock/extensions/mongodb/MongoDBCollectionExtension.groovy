package uk.co.informaticslab.molab3dwxds.spock.extensions.mongodb

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FieldInfo

class MongoDBCollectionExtension extends AbstractAnnotationDrivenExtension<MongoDBCollection> {

    @Override
    void visitFieldAnnotation(MongoDBCollection mongoDBCollection, FieldInfo fieldInfo) {

        def database = mongoDBCollection.database()
        def collection = mongoDBCollection.collection()
        def options = null
        if (mongoDBCollection.options().length > 0) {

            if (mongoDBCollection.options().length == 1) {

                def opts = mongoDBCollection.options()[0]

                def capped = opts.capped()
                def autoIndexId = opts.autoIndexId()
                def size = opts.size() > 0 ? opts.size() : null
                def max = opts.max() > 0 ? opts.max() : null

                options = [
                        capped : capped,
                        autoIndexId : autoIndexId ]

                if (size) {
                    options["size"] = size
                }
                if (max) {
                    options["max"] = max
                }
            } else {
                throw new IllegalArgumentException("Cannot define more tha one options")
            }
        }
        def indexes = null
        if (mongoDBCollection.indexes().length > 0) {
            indexes = []
            for (index in mongoDBCollection.indexes()) {

                def idxKeys = [:]
                for (key in index.keys()) {
                    idxKeys[key.value()] = key.direction()
                }

                def idxOpts = [
                        unique: index.unique(),
                        sparse: index.sparse()
                ]

                indexes.add([
                        keys: idxKeys,
                        options: idxOpts
                ])
            }
        }

        def methodInterceptor = new MongoDBCollectionMethodInterceptor(fieldInfo, database, collection, options, indexes)
        methodInterceptor.install(fieldInfo.parent)
    }
}
