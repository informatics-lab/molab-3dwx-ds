package uk.co.informaticslab.molab3dwxds.api.binders;


import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import javax.inject.Singleton;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class DateTimeBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(DateTimeParamConverterProvider.class).to(ParamConverterProvider.class).in(Singleton.class);
    }

    public static class DateTimeParamConverterProvider implements ParamConverter<DateTime>, ParamConverterProvider {

        @Override
        public DateTime fromString(String s) {
            return ISODateTimeFormat.dateTimeParser().parseDateTime(s);
        }

        @Override
        public String toString(DateTime dateTime) {
            return ISODateTimeFormat.dateTime().print(dateTime);
        }

        @Override
        public <T> ParamConverter<T> getConverter(Class<T> aClass, Type type, Annotation[] annotations) {
            if (aClass == DateTime.class) {
                return (ParamConverter<T>) this;
            }
            return null;
        }
    }
}