package hbs;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import clojure.lang.Keyword;

import com.github.jknack.handlebars.ValueResolver;

public enum KeywordMapValueResolver implements ValueResolver {
    INSTANCE;

    @Override
    public Object resolve(Object context, String name) {
        Object value = null;
        if (context instanceof Map) {
            value = ((Map) context).get(name);
            if (value == null) {
                value = ((Map) context).get(Keyword.intern(null, name));
            }
        }
        return value == null ? UNRESOLVED : value;
    }

    @Override
    public Set<Map.Entry<String, Object>> propertySet(Object context) {
        if (context instanceof Map) {
            return ((Map) context).entrySet();
        }
        return Collections.emptySet();
    }
}