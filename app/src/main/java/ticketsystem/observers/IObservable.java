package ticketsystem.observers;

import ticketsystem.events.Event;

public interface IObservable {
    void attach(IObserver observer);
    void remove(IObserver observer);

    void notifyObservers(Event event, String message);
}
