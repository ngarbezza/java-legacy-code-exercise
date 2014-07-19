package ar.edu.unq.sasa.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Clase parte de la implementación del patrón Observer.
 * 
 * @author Nahuel Garbezza
 *
 */
public class Publisher {
	
	private final Map<String, Collection<Subscriber>> subscribers;
	
	public Publisher() {
		this.subscribers = new HashMap<String, Collection<Subscriber>>();
	}

	/**
	 * @return the subscribers
	 */
	public Map<String, Collection<Subscriber>> getSubscribers() {
		return subscribers;
	}
	
	public void addSubscriber(String aspect, Subscriber s) {
		if (!this.getSubscribers().containsKey(aspect))
			this.getSubscribers().put(aspect, new HashSet<Subscriber>());
		this.getSubscribers().get(aspect).add(s);
	}
	
	public void removeSubscriber(String aspect, Subscriber s) {
		// PRECONDICION : el subscriber está en ese aspect
		this.getSubscribers().get(aspect).remove(s);
	}
	
	public void changed(String aspect, Object value) {
		if (this.getSubscribers().containsKey(aspect))
			for (Subscriber s : this.getSubscribers().get(aspect))
				s.update(aspect, value);
	}
}
