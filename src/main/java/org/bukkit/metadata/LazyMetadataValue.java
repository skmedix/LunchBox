package org.bukkit.metadata;

import java.lang.ref.SoftReference;
import java.util.concurrent.Callable;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

public class LazyMetadataValue extends MetadataValueAdapter implements MetadataValue {

    private Callable lazyValue;
    private LazyMetadataValue.CacheStrategy cacheStrategy;
    private SoftReference internalValue;
    private static final Object ACTUALLY_NULL = new Object();

    public LazyMetadataValue(Plugin owningPlugin, Callable lazyValue) {
        this(owningPlugin, LazyMetadataValue.CacheStrategy.CACHE_AFTER_FIRST_EVAL, lazyValue);
    }

    public LazyMetadataValue(Plugin owningPlugin, LazyMetadataValue.CacheStrategy cacheStrategy, Callable lazyValue) {
        super(owningPlugin);
        Validate.notNull(cacheStrategy, "cacheStrategy cannot be null");
        Validate.notNull(lazyValue, "lazyValue cannot be null");
        this.internalValue = new SoftReference((Object) null);
        this.lazyValue = lazyValue;
        this.cacheStrategy = cacheStrategy;
    }

    protected LazyMetadataValue(Plugin owningPlugin) {
        super(owningPlugin);
    }

    public Object value() {
        this.eval();
        Object value = this.internalValue.get();

        return value == LazyMetadataValue.ACTUALLY_NULL ? null : value;
    }

    private synchronized void eval() throws MetadataEvaluationException {
        if (this.cacheStrategy == LazyMetadataValue.CacheStrategy.NEVER_CACHE || this.internalValue.get() == null) {
            try {
                Object e = this.lazyValue.call();

                if (e == null) {
                    e = LazyMetadataValue.ACTUALLY_NULL;
                }

                this.internalValue = new SoftReference(e);
            } catch (Exception exception) {
                throw new MetadataEvaluationException(exception);
            }
        }

    }

    public synchronized void invalidate() {
        if (this.cacheStrategy != LazyMetadataValue.CacheStrategy.CACHE_ETERNALLY) {
            this.internalValue.clear();
        }

    }

    public static enum CacheStrategy {

        CACHE_AFTER_FIRST_EVAL, NEVER_CACHE, CACHE_ETERNALLY;
    }
}
