package uk.co.informaticslab.molab3dwxds.spock.extensions.mongodb

@interface Options {

    boolean capped() default false
    boolean autoIndexId() default true
    int size() default 0
    int max() default 0
}
