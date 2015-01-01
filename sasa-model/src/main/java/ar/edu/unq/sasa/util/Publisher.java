package ar.edu.unq.sasa.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Publisher {

    private final Map<String, Collection<Subscriber>> subscribers;

    public Publisher() {
        subscribers = new HashMap<>();
    }

    public void addSubscriber(String aspect, Subscriber aSubscriber) {
        if (!subscribers.containsKey(aspect))
            subscribers.put(aspect, new HashSet<>());
        subscribers.get(aspect).add(aSubscriber);
    }

    public void removeSubscriber(String aspect, Subscriber aSubscriber) {
        // PRECONDICIÓN : el subscriber está en ese aspect
        subscribers.get(aspect).remove(aSubscriber);
    }

    public void changed(String aspect, Object value) {
        if (subscribers.containsKey(aspect))
            for (Subscriber aSubscriber : subscribers.get(aspect))
                aSubscriber.update(aspect, value);
    }
}
