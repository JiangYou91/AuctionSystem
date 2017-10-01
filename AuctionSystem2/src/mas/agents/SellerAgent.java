package mas.agents;

 
import java.util.ArrayList;

import env.Environment;
import mas.Auction;
import mas.abstractAgent;
import mas.behaviours.SellerBehaviour;  

public class SellerAgent extends abstractAgent{  
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8286564182216789267L;

	ArrayList<Integer> feedbacks;

	private ArrayList<Auction> auctionList;
	
	boolean is_training=false;
	
	protected void setup(){
		super.setup();
		final Object[] args = getArguments();
		if(args!=null && args.length>0 && args[0]!=null){

			deployAgent((Environment) args[0]); 
			setAuctionList(new ArrayList<Auction>());
			if(args.length==2 && args[1].equals("train")) {
				this.is_training = true;
			}
		}else{
			System.err.println("Malfunction during parameter's loading of agent "+ this.getClass().getName());
			System.exit(-1);
		}
		
		addBehaviour(new SellerBehaviour(this));
		doWait(2000);
	}

	public ArrayList<Auction> getAuctionList() {
		return auctionList;
	}

	public void setAuctionList(ArrayList<Auction> auctionList) {
		this.auctionList = auctionList;
	}	
}
