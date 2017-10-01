package mas.observer;

import java.util.ArrayList;

public interface Subject {
	ArrayList<Observer> list = new ArrayList<Observer>();
	public default void attach(Observer observer) {
        list.add(observer);
	}
	public default void dettach(Observer observer) {
        list.remove(observer);
	}
	public default void nodifyObservers(String newState){
	        
	        for(Observer observer : list){
	            observer.update(newState);
	        }
	    }
}
