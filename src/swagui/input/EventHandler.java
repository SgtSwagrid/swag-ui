package swagui.input;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Event handling.
 * @author Alec Dorrington
 */
public class EventHandler {
    
    private static Set<EventCallback<?>> callbacks = new HashSet<>();
    
    /**
     * Register a new event callback.
     * @param <T> the type of event for which this should be triggered.
     * @param type the class of T.
     * @param callback the function to call upon event occurrence.
     */
    public static <T> void register(Class<T> event, Consumer<T> callback) {
        callbacks.add(new EventCallback<>(event, callback, null));
    }
    
    /**
     * Register a new event callback.
     * @param <T> the type of event for which this should be triggered.
     * @param type the class of T.
     * @param key used for identification upon removal.
     * @param callback the function to call upon event occurrence.
     */
    public static <T> void register(Class<T> event,
            Object key, Consumer<T> callback) {
        callbacks.add(new EventCallback<>(event, callback, key));
    }
    
    /**
     * Remove all callbacks associated with a key.
     * @param key used for identification.
     */
    public static void remove(Object key) {
        callbacks.removeIf(c -> c.key == key);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> void trigger(T event) {
        callbacks.stream()
            .filter(c -> c.event.isInstance(event))
            .map(c -> (Consumer<T>)c.callback)
            .forEach(c -> c.accept(event));
    }
    
    /**
     * Callback function to be called upon triggering an event.
     */
    private static class EventCallback<T> {
        
        /** Event type upon which the callback is called. */
        private Class<T> event;
        
        /** The callback function. */
        private Consumer<T> callback;
        
        /** Key used to identify for removal. */
        private Object key;
        
        private EventCallback(Class<T> event,
                Consumer<T> callback, Object key) {
            this.event = event;
            this.callback = callback;
            this.key = key;
        }
    }
}