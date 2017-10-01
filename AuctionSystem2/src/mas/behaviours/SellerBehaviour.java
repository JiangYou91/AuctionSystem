package mas.behaviours;

import java.util.ArrayList;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.Auction; 
import mas.agents.SellerAgent;

public class SellerBehaviour extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean finished = false;
	private int behaviour_state;
	
	//seller parameters
	int seller_number=10;
	int fraud_seller_number=1;
	int auction_number=10000;
	
	//auction parameters;
	int startTime=0;
	int average_duration=3000;
	int average_item_value=2500;
	int average_start_price=250;
	double sigma = 0.2;
	
	
	public SellerBehaviour(Agent myAgent) {
		super(myAgent); 
	}
	
	@Override
	public void action() {
		if(auction_number>0) {
			auction_number--;
			
			System.out.println(myAgent.getLocalName()  +" starts action" );
			
			if ((new Random()).nextInt(100)>5){
				publishAuction();
			} else{
				publishShillBidsAuction(); 
			}
		}else {
			//end
			finished =true;
		}

		
		} 
	
	public void publishAuction(){
		System.out.println(myAgent.getLocalName()  +" starts publishAuction" );
 
		int duration = getRandomGaussianValue(  average_duration,   sigma );
		int itemValue = getRandomGaussianValue(  average_item_value,   sigma );  
		int startPrice = getRandomGaussianValue(  average_start_price,   sigma );   
		
		String auction_str =  startTime+"_"+duration+"_"+itemValue+"_"+startPrice;
		
		
		//send auction to the bidder agent
		final ACLMessage aclmsg = new ACLMessage(ACLMessage.INFORM);  
		aclmsg.setSender(myAgent.getAID());
		aclmsg.addReceiver(new AID("BidderAgent", AID.ISLOCALNAME) );
		aclmsg.setProtocol("Auction"); 
		aclmsg.setContent(auction_str);		
		
		//send auction to the bidderagent
		myAgent.send(aclmsg);
 

		MessageTemplate inform = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		MessageTemplate auctionTplt = MessageTemplate.MatchProtocol("Auction");
		MessageTemplate template_auction = MessageTemplate.and(inform,auctionTplt); 

		//wait to receive the message. 
		ACLMessage receive =  myAgent.blockingReceive(template_auction  ); ; 
		 
//		System.out.println(receive);
		  
		 
	}  
	public void publishShillBidsAuction(){
		System.out.println(myAgent.getLocalName()  +" starts publishShillBidsAuction" );
		 
		int duration = getRandomGaussianValue(  average_duration,   sigma );
		int itemValue = getRandomGaussianValue(  average_item_value,   sigma );  
		int startPrice = getRandomGaussianValue(  average_start_price,   sigma );   
		
		String auction_str =  startTime+"_"+duration+"_"+itemValue+"_"+startPrice;
		
		
		//send auction to the bidder agent
		final ACLMessage aclmsg = new ACLMessage(ACLMessage.INFORM);  
		aclmsg.setSender(myAgent.getAID());
		aclmsg.addReceiver(new AID("BidderAgent", AID.ISLOCALNAME) );
		aclmsg.setProtocol("ShillBidsAuction"); 
		aclmsg.setContent(auction_str);		
		
		//send auction to the bidderagent
		myAgent.send(aclmsg);
 

		MessageTemplate inform = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		MessageTemplate auctionTplt = MessageTemplate.MatchProtocol("ShillBidsAuction");
		MessageTemplate template_auction = MessageTemplate.and(inform,auctionTplt); 

		//wait to receive the message. 
		ACLMessage receive =  myAgent.blockingReceive(template_auction  ); ; 
		 
//		System.out.println(receive); 
		
		 
	}

	public int getRandomGaussianValue(int average, double sigma ){
		return average + (int) (average*(new Random()).nextGaussian()*sigma);
	}
	

	public int getRandomValueByDistribution(int start, int end, double[] distribution){
		//check if the distribution starts at 0 and ends at 1;
		if(distribution.length>0 && distribution[0]==0 && distribution[distribution.length-1]==1){
			double v = (new Random()).nextDouble();
			for(int i = 0;i<distribution.length-1;i++){
				if( v>=distribution[i] && v<=distribution[+1]){
					return (int) ((double)i/distribution.length*(end-start));
				}
			}
		}else {
			System.out.println("distribution not legal, it should start with 0 and end with 1"); 
		}
		return 0 ;
			
			
		
	}
	

	@Override
	public boolean done() { 
		System.out.println(myAgent.getLocalName()  +" starts done" );
		return finished;
	}
	
	@Override
	public int onEnd(){ 
		System.out.println(myAgent.getLocalName()  +" starts onEnd" );
		finished =false; 
		return behaviour_state;
	}
}