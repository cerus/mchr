package dev.cerus.mcheadrender.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Represents a registry for objects
 *
 * @param <T> The registry type
 */
public class Registry<T> {

    private final Map<String, T> map = new HashMap<>();
    private final Function<T, String> idRetrieverFun;
    private final String fallback;

    @SafeVarargs
    public Registry(final Function<T, String> idRetrieverFun, final String fallback, final T... defaults) {
        this.idRetrieverFun = idRetrieverFun;
        this.fallback = fallback;

        for (final T t : defaults) {
            this.map.put(idRetrieverFun.apply(t), t);
        }
    }

    /**
     * Register an object
     *
     * @param o The object to register
     */
    public void register(final T o) {
        this.map.put(this.idRetrieverFun.apply(o), o);
    }

    /**
     * Get a registered object or return the fallback object
     *
     * @param id The id of the registered object
     *
     * @return The registered object or the fallback
     */
    public T getOrFallback(final String id) {
        return Optional.ofNullable(this.get(id)).orElse(this.get(this.fallback));
    }

    /**
     * Get a registered object
     *
     * @param id The id of the registered object
     *
     * @return The registered object
     */
    public T get(final String id) {
        return this.map.get(id);
    }

    /**
     * Get a collection of all the registered id's
     *
     * @return The registered id's
     */
    public Collection<String> ids() {
        return this.map.keySet();
    }

}
