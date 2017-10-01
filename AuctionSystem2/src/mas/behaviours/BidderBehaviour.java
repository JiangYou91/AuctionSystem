package mas.behaviours;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.Auction;
import mas.Bid;
import mas.Bidder;
import mas.agents.BidderAgent;
import mas.observer.Observer;
import mas.observer.ShillingBidsObserver;
import mas.observer.Subject;

public class BidderBehaviour extends SimpleBehaviour implements Subject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -216991741168440806L;
	private boolean finished = false;
	private int behaviour_state;
	
	//fields
	Auction currentAuction;
	ACLMessage currentMessage;

	//parameters
	int bidsPerAuctions=10; 
	double shillRatio = 0.7;
	
	int waitTime =3;
	
	int watcher_start_time=5000;
	
	
	public BidderBehaviour(Agent myAgent) {
		super(myAgent); 
		this.attach(new ShillingBidsObserver (""));
	}
	
	@Override
	public void action() {
		System.out.println(myAgent.getLocalName()  +" starts action" ); 
		
		
		if(receiveAuction()){
			generateBids(this.currentAuction);
			
			myAgent.send(currentMessage);
			((BidderAgent)myAgent).getFinishedAuction().add(this.currentAuction);

//			System.out.println(this.currentAuction);
			
		}else if (receiveShillBidsAuction()){
			generateShillBids(this.currentAuction);
			
			
			myAgent.send(currentMessage);
			((BidderAgent)myAgent).getFinishedAuction().add(this.currentAuction);

//			System.out.println(this.currentAuction);
		} 
		else if(waitTime>0){ 
			waitTime --;
		}else if( !(waitTime>0)) {
			
			
//			printBidderData();
			printBidderData("training_data.txt");
			finished =true;
			
			if(((BidderAgent)myAgent).isTraining()) {
				startTraining();
			}
		}
		
		
		if(((BidderAgent)myAgent).isTraining() == false) {
	
			//check the user average data by watcher. 
			if(this.watcher_start_time>0) {
				this.watcher_start_time--;
			}else {
	//			this.watcher_start_time+=1;
				Bid bid = null;
				Bidder bidder =null;
				String data=null;
				for (int i=0;i<this.currentAuction.getBidsList().size();i++) {
					BidderAgent ag = (BidderAgent)myAgent; 
					bid = this.currentAuction.getBidsList().get(i);
					if(bid.getUserId()<ag.getBidderList().size()) {
						bidder = ag.getBidderList().get(bid.getUserId());
					}else {
						bidder = ag.getFraudBidderList().get(bid.getUserId()-ag.getBidderList().size());	
					}
					data = bidder.getAverageData(ag.getFinishedAuction());
					System.out.println("studing the bidder: "+bidder.getBidderId());
					this.nodifyObservers(data);
				}
			}
		
		}
	}
	
	private void startTraining() {
        System.out.println("start Clustering...");

        
		Process p = null;
		String s=null;
		String filename = "training_data.txt";
		String filename_2 = "clustered_data.txt";
		
		try {
			p = Runtime.getRuntime().exec("python cluster.py " +filename+" "+ filename_2);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            if ((s = stdInput.readLine()) != null) {
                System.out.println("stdInput:\n "+s);
                while((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }
            }
            if ((s = stdError.readLine()) != null) {
                System.out.println("stdError:\n "+s);
                while((s = stdError.readLine()) != null) {
                    System.out.println(s);
                }
            }
		} catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
		}  
        System.out.println("start Training...");
		
		try {
			p = Runtime.getRuntime().exec("python training_tensor.py " +filename_2);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            if ((s = stdInput.readLine()) != null) {
                System.out.println("stdInput:\n "+s);
                while((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }
            }
            if ((s = stdError.readLine()) != null) {
                System.out.println("stdError:\n "+s);
                while((s = stdError.readLine()) != null) {
                    System.out.println(s);
                }
            }
		} catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
		}  

        System.out.println("finish Training...");
	}

	private void printBidderData() {
		BidderAgent ag = (BidderAgent)myAgent;
		Bidder bidder;
		for(int i = 0;i< ag.getBidderList().size();i++) {
			bidder = ag.getBidderList().get(i);
			System.out.println("bidder"+bidder.getBidderId()+": "+bidder.getAverageData(ag.getFinishedAuction())+" 0");
		}
		for(int i = 0;i< ag.getFraudBidderList().size();i++) {
			bidder = ag.getFraudBidderList().get(i);
			System.out.println("bidder"+bidder.getBidderId()+": "+bidder.getAverageData(ag.getFinishedAuction())+" 1");
		}
	}
	private void printBidderData( String filename) {
		BidderAgent ag = (BidderAgent)myAgent;
		Bidder bidder;
		
		String toWrite = "";
		
		File myFoo = new File(filename);
		FileWriter fooWriter;
		try {
			fooWriter = new FileWriter(myFoo, false); // true to append
		                                                     // false to overwrite.
		  
			for(int i = 0;i< ag.getBidderList().size();i++) {
				bidder = ag.getBidderList().get(i);
//				toWrite = "bidder"+bidder.getBidderId()+": "+bidder.getAverageData(ag.getFinishedAuction())+" 0\n";
				toWrite = bidder.getAverageData(ag.getFinishedAuction())+" \n";
				
				fooWriter.write(toWrite);
	
			}
			for(int i = 0;i< ag.getFraudBidderList().size();i++) {
				bidder = ag.getFraudBidderList().get(i);
//				toWrite =  "bidder"+bidder.getBidderId()+": "+bidder.getAverageData(ag.getFinishedAuction())+" 1\n";
				toWrite =  bidder.getAverageData(ag.getFinishedAuction())+" \n";
				fooWriter.write(toWrite);
			}
			
			fooWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	private boolean receiveAuction() {

		MessageTemplate inform = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		MessageTemplate auctionTplt = MessageTemplate.MatchProtocol("Auction");
		MessageTemplate template_auction = MessageTemplate.and(inform,auctionTplt); 

		//wait to receive the message. 
		ACLMessage receive =  myAgent.blockingReceive(template_auction ,100);  

		if(receive!=null){ 
			
			this.currentAuction = new Auction();
			this.currentAuction.createFromString(receive.getContent()); 
			
			this.currentMessage= receive.createReply();
			this.currentMessage.setContent("FinishedBidding");
			return true;
		}
		
		return false;
	} 
	private void generateBids(Auction auction) { 
		BidderAgent ag = (BidderAgent)myAgent;
		
		double sigma = 1;
		int bidsNumberPerAuction = this.bidsPerAuctions + getRandomGaussianValue(this.bidsPerAuctions, sigma)/5;
		
		//init bids
		for(int i = 0;i< bidsNumberPerAuction;i++) {
			Bid b = new Bid();
			b.setAuctionId(auction.getAutcionId());
			b.setUserId(ag.getBidderList().get((new Random()).nextInt(ag.getBidderList().size())).getBidderId());
			auction.getBidsList().add(b);
		}
		
		//generate time for bids
		ArrayList<Integer> timeSeries = new ArrayList<Integer> ();
		timeSeries.add(0,auction.getEndTime() - ag.getRandomFirstBidTime());	
		for(int i = 1;i< bidsNumberPerAuction;i++) {
			timeSeries.add((int)(timeSeries.get(0)+(auction.getEndTime()-timeSeries.get(0)) * ag.getRandomAverageBidTime()));
		}
		
		Collections.sort(timeSeries);
		
		//adjust the same time
		for(int i = 1;i< bidsNumberPerAuction;i++) {
//			System.out.println(timeSeries.get(i)+" "+timeSeries.get(i-1));
			if(timeSeries.get(i).equals( timeSeries.get(i-1))) {
				for(int j = i;j< bidsNumberPerAuction;j++) {
					timeSeries.set(j, timeSeries.get(j)+1);
				}
			}
 		} 
		
		//set time
		for(int i = 0;i< bidsNumberPerAuction;i++) {
			auction.getBidsList().get(i).setBidTime(timeSeries.get(i));
		}
		auction.setEndTime(auction.getBidsList().get(auction.getBidsList().size()-1).getBidTime());
		
		
		//generate amount
		ArrayList<Integer> amountSeries = new ArrayList<Integer> ();
		for(int i = 0;i< bidsNumberPerAuction;i++) {
			amountSeries.add(auction.getStartPrice()+(int)(auction.getItemValuation()*ag.getRandomAverageAmount()));
		} 
		Collections.sort(amountSeries);
		
		//adjust amount
		for(int i = 1;i< bidsNumberPerAuction;i++) {
			if(amountSeries.get(i) - amountSeries.get(i-1)<100) {
				for(int j = i;j< bidsNumberPerAuction;j++) {
					amountSeries.set(j, amountSeries.get(j)+100);
					
				}
			}
 		}
		
		//set amount
		for(int i = 0;i< bidsNumberPerAuction;i++) {
			auction.getBidsList().get(i).setBidAmount(amountSeries.get(i));
		}
		auction.setEndPrice(auction.getBidsList().get(auction.getBidsList().size()-1).getBidAmount());

		
		//add  bid to the bidder. 
		for(int i = 0;i< bidsNumberPerAuction;i++) {
			Bid b2 = auction.getBidsList().get(i);
			ag.getBidderList().get(b2.getUserId()).getBids().add(b2);
			
		}

		//add  auction to the bidder. 
		for(int i = 0;i< bidsNumberPerAuction;i++) {
			Bid b2 = auction.getBidsList().get(i);
			Bidder bidder  = ag.getBidderList().get(b2.getUserId());
			if(bidder.getAllAuction().size()>0 && ! bidder.getAllAuction().get( bidder.getAllAuction().size()-1).equals(auction)) {
				bidder.getAllAuction().add(auction);
			}
			if (i==bidsNumberPerAuction-1) {
				bidder.getAuctionWon().add(auction);
			}
		}
		
		
		
		//add bidder information to the auction
		int winner = auction.getBidsList().get(auction.getBidsList().size()-1).getUserId();
		auction.setWinner(winner);
		auction.setStars(ag.getRandomReputation());
		 
	} 

	public int getRandomGaussianValue(int average, double sigma ){
		return average + (int) (average*(new Random()).nextGaussian()*sigma);
	}
	 
	private boolean receiveShillBidsAuction(){

		MessageTemplate inform = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		MessageTemplate auctionTplt = MessageTemplate.MatchProtocol("ShillBidsAuction");
		MessageTemplate template_auction = MessageTemplate.and(inform,auctionTplt); 

		//wait to receive the message. 
		ACLMessage receive =  myAgent.blockingReceive(template_auction ,100);  

		if(receive!=null){ 
			
			this.currentAuction = new Auction();
			this.currentAuction.createFromString(receive.getContent()); 
			
			this.currentMessage= receive.createReply();
			this.currentMessage.setContent("FinishedBidding");
			return true;
		}
		
		return false; 
	}

	
	private void generateShillBids(Auction auction ) {

		BidderAgent ag = (BidderAgent)myAgent;
		
		double sigma = 1;
		int bidsNumberPerAuction = this.bidsPerAuctions + getRandomGaussianValue(this.bidsPerAuctions, sigma)/5;
		
		//init bids
		for(int i = 0;i< bidsNumberPerAuction;i++) {
			Bid b = new Bid();
			b.setAuctionId(auction.getAutcionId());
			b.setUserId(ag.getBidderList().get((new Random()).nextInt(ag.getBidderList().size())).getBidderId());
			auction.getBidsList().add(b);
		}
		

		//generate time for bids
		ArrayList<Integer> timeSeries = new ArrayList<Integer> ();
		timeSeries.add(0,auction.getEndTime() - ag.getRandomFirstBidTime());	
		for(int i = 1;i< bidsNumberPerAuction;i++) {
			timeSeries.add((int)(timeSeries.get(0)+(auction.getEndTime()-timeSeries.get(0)) * ag.getRandomAverageBidTime()));
		}
		
		Collections.sort(timeSeries);
		//adjust the same time
		for(int i = 1;i< bidsNumberPerAuction;i++) {
//			System.out.println(timeSeries.get(i)+" "+timeSeries.get(i-1));
			if(timeSeries.get(i).equals( timeSeries.get(i-1))) {
				for(int j = i;j< bidsNumberPerAuction;j++) {
					timeSeries.set(j, timeSeries.get(j)+1);
				} 
			}
 		}
		 
		//set time
		for(int i = 0;i< bidsNumberPerAuction;i++) {
			auction.getBidsList().get(i).setBidTime(timeSeries.get(i));
		}
		auction.setEndTime(auction.getBidsList().get(auction.getBidsList().size()-1).getBidTime());
		
		
		
		//generate amount
		ArrayList<Integer> amountSeries = new ArrayList<Integer> ();
		for(int i = 0;i< bidsNumberPerAuction;i++) {
			amountSeries.add(auction.getStartPrice()+(int)(auction.getItemValuation()*ag.getRandomAverageAmount()));
		} 
		Collections.sort(amountSeries);
		//adjust amount
		for(int i = 1;i< bidsNumberPerAuction;i++) {
			if(amountSeries.get(i) - amountSeries.get(i-1)<100) {
				for(int j = i;j< bidsNumberPerAuction;j++) {
					amountSeries.set(j, amountSeries.get(j)+100);
					
				}
			}
 		}
		//set amount
		for(int i = 0;i< bidsNumberPerAuction;i++) {
			auction.getBidsList().get(i).setBidAmount(amountSeries.get(i));
		}
		auction.setEndPrice(auction.getBidsList().get(auction.getBidsList().size()-1).getBidAmount());
		
		
		//add the shillbidders
		
		for(int i = 0;i< bidsNumberPerAuction;) {
			//override the id by fraudster
			auction.getBidsList().get(i).setUserId((new Random()).nextInt(ag.getFraudBidderList().size())+ag.getFraudBidderList().get(0).getBidderId());
			
			int increment = (new Random()).nextInt(2)+2;
			i+=increment;
			if (! (i< bidsNumberPerAuction))
				break;
			if (auction.getBidsList().get(i).getBidAmount()>auction.getItemValuation()*this.shillRatio)
				break;
		}
		
		//add  bid to the bidder. 
		for(int i = 0;i< bidsNumberPerAuction;i++) {
			Bid b2 = auction.getBidsList().get(i);
			if(b2.getUserId()>=ag.getBidderList().size())
				ag.getFraudBidderList().get(b2.getUserId() - ag.getBidderList().size()).getBids().add(b2);
			else
				ag.getBidderList().get(b2.getUserId()).getBids().add(b2);
		}

		//add  auction to the bidder. 
		for(int i = 0;i< bidsNumberPerAuction;i++) {
			Bid b2 = auction.getBidsList().get(i);
			Bidder bidder;
			if(b2.getUserId()>=ag.getBidderList().size()) {
				bidder =ag.getFraudBidderList().get(b2.getUserId() - ag.getBidderList().size());
			}else {
				bidder =ag.getBidderList().get(b2.getUserId());
			}
			
			if(bidder.getAllAuction().size()==0 ||! bidder.getAllAuction().get(bidder.getAllAuction().size()-1).equals(auction)) {
				bidder.getAllAuction().add(auction);
			}
			
			if (i==bidsNumberPerAuction-1) {
				bidder.getAuctionWon().add(auction);
			}
			
		}
		
		//add bidder information to the auction
		int winner = auction.getBidsList().get(auction.getBidsList().size()-1).getUserId();
		auction.setWinner(winner);
		auction.setStars(ag.getRandomReputation());

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