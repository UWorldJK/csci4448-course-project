package ticketsystem.observers;

import ticketsystem.events.Event;

public interface IObserver {
    void update(Event event, String message);
}
