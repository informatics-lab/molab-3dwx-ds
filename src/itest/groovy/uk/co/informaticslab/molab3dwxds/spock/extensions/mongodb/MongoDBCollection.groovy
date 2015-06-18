package uk.co.informaticslab.molab3dwxds.spock.extensions.mongodb

import org.spockframework.runtime.extension.ExtensionAnnotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.FIELD])
@ExtensionAnnotation(MongoDBCollectionExtension)
@interface MongoDBCollection {

    String database()
    String collection()
    Options[] options() default []
    Index[] indexes() default []
}