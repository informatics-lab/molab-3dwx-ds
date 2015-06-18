package uk.co.informaticslab.molab3dwxds.spock.extensions.mongodb

import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.Mongo
import com.mongodb.MongoURI
import com.mongodb.WriteConcern

class MongoDBCollectionHelper {

    private final Mongo mongo
    private final String database
    private final String collection
    private final DBObject options
    private final List<Map<String, DBObject>> indexes

    def MongoDBCollectionHelper(String uri, String database, String collection, Map<String, ?> options, List<Map<String, ?>> indexes) throws UnknownHostException {
        this.mongo = new Mongo(new MongoURI(uri))
        this.mongo.setWriteConcern(WriteConcern.SAFE)
        this.database = database
        this.collection = collection
        this.options = options != null ? new BasicDBObject(options) : null
        if (indexes) {
            this.indexes = []
            for (index in indexes) {
                this.indexes.add(
                        [
                                keys: new BasicDBObject(index.keys),
                                options: new BasicDBObject(index.options)
                        ]
                )
            }
        } else {
            this.indexes = null
        }
    }

    DB getDB() {
        return mongo.getDB(database)
    }

    DBCollection getCollection() {
        DB db = getDB()
        if (options != null && !db.collectionExists(collection)) {
            db.createCollection(collection, options)
        }
        if (indexes != null) {
            DBCollection col = db.getCollection(collection)
            for (index in indexes) {
                col.createIndex(index.keys, index.options)
            }
        }
        return getDB().getCollection(collection)
    }

    def drop() {
        // Drop the database to clear up the memory mapped files
        getDB().dropDatabase()
    }

    def insert(Map<String, Object> document) {
        if (document == null)
            return
        getCollection().insert(new BasicDBObject(document))
    }

    def insert(List<Map<String, Object>> documents) {
        if (documents == null)
            return
        def dbObjs = []
        for (document in documents) {
            insert(document)
        }
    }

    def contains(Map<String, Object> document) {
        insert(document)
    }

    def contains(List<Map<String, Object>> documents) {
        insert(documents)
    }

    Map<String, Object> findOne(Map<String, ?> query) {
        DBObject queryObj = new BasicDBObject()
        DBObject fieldsObj = new BasicDBObject()
        if (query != null) {
            if (query.containsKey("query")) {
                queryObj.putAll(query.get("query"))
            }
            if (query.containsKey("fields")) {
                fieldsObj.putAll(query.get("fields"))
            }
        }
        return getCollection().findOne(queryObj, fieldsObj)
    }

    List<Map<String, Object>> find(Map<String, ?> query) {
        DBObject queryObj = new BasicDBObject()
        DBObject fieldsObj = new BasicDBObject()
        if (query != null) {
            if (query.containsKey("query")) {
                queryObj.putAll(query.get("query"))
            }
            if (query.containsKey("fields")) {
                fieldsObj.putAll(query.get("fields"))
            }
        }

        def results = []
        def cursor = getCollection().find(queryObj, fieldsObj)
        try {
            while (cursor.hasNext()) {
                results.add(cursor.next() as Map)
            }
        } finally {
            cursor.close();
        }
        return results
    }
}
