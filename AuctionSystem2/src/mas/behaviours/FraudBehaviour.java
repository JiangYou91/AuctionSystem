package mas.behaviours;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class FraudBehaviour extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2215143165300008990L;
	private boolean finished = false;
	private int behaviour_state;
	
	public FraudBehaviour(Agent myAgent) {
		super(myAgent); 
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}
 

	@Override
	public boolean done() { 
		return finished;
	}
	
	@Override
	public int onEnd(){ 
		finished =false; 
		return behaviour_state;
	}
}