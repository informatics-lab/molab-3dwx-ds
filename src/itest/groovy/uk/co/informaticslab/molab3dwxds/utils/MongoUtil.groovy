package uk.co.informaticslab.molab3dwxds.utils

import org.joda.time.DateTime

class MongoUtil {

    static date(String iso8601) {
        return new DateTime(iso8601).toDate()
    }
}
