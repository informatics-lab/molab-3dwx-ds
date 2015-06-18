package uk.co.informaticslab.molab3dwxds.spock.extensions.mongodb

@interface Index {

    IndexKey[] keys() default []
    boolean unique() default false
    boolean sparse() default false
}