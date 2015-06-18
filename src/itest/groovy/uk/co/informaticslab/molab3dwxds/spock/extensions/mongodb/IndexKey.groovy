package uk.co.informaticslab.molab3dwxds.spock.extensions.mongodb

@interface IndexKey {

    String value()
    int direction() default 1
}