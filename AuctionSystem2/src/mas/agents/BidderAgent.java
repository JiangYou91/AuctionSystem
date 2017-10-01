package mas.agents;
 
import java.util.ArrayList;
import java.util.Random;

import env.Environment;
import mas.Auction;
import mas.Bid;
import mas.Bidder;
import mas.abstractAgent;
import mas.behaviours.BidderBehaviour;  

public class BidderAgent extends abstractAgent{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 2608700878178109236L;
	
	//fields
	int BidderNumber=1000;
	int FraudBidderNumber=40;
	
	int requiredMinimumBidAmounts;
	

	private ArrayList<Bidder> bidderList;
	private ArrayList<Bidder> fraudBidderList;

	//bidder's parameters
	//int reputation;
	//int bidsPerAuctions;
	//int fisrtBidTime;
	//int averageBidAmount; 
	
	//bidder's parametre Distribution
	//double[] auctionCountDistribution = new double[]{0,0.6,0.65,0.8,0.85,0.95,0.97,0.98,1};
	double[] reputationDistribution = new double[]{0,0.05,0.1,0.2,0.3,0.7,0.8,0.9,0.95,1};
	double[] bidAmountDistribution = new double[]{0,0.1,0.2,0.3,0.6,0.9,1};
	double[] excessBidIncrementDistribution = new double[]{0,0.4,0.45,0.5,0.7,0.9,0.95,1};
	double[] bidsPerAuctionDistribution = new double[]{0,0.4,0.65,0.75,0.9,0.93,0.96,1};
	
	double[] firstBidTimeDistribution = new double[]{0,0,0,0,0.05,0.1,0.13,0.18,0.26,0.35,0.45,0.6,0.75,0.9,1};
	double[] averageBidTimeDistribution = new double[]{0,0.13,0.13,0.18,0.25,0.33,0.43,0.52,0.6,0.65,0.7,0.8,0.9,1};
	double[] winPropotionDistribution = new double[]{0,0.5,0.5,0.55,0.6,0.6,0.7,0.75,0.75,0.75,0.75,0.75,1};
	double[] bidAmountPropotionDistribution = new double[]{0,0,0,0.05,0.1,0.15,0.2,0.27,0.35,0.45,0.55,0.65,0.75,1};
	double[] bidPropotionDistribution = new double[]{0,0.1,0.17,0.25,0.31,0.38,0.44,0.5,0.54,0.65,0.78,0.8,0.82,0.83,0.88,1};
	
	//bidder's parameter range;
	//double minAuctionCount =0; double maxAuctionCount =4; 
	double minReputation =0; double maxReputation =10; 
	double minBidAmount =0; double maxBidAmount =1; 
	double minExcessBidIncrement =100; double maxExcessBidIncrement =2; //most of them add 1 or  5 dollors
	double minBidsPerAuction =1; double maxBidsPerAuction =4; 
	
	double minFirstBidTime =1000; double maxFirstBidTime =500; //from the end of an auction 
	double minAverageBidTime =0; double maxAverageBidTime =1;//the elapsed time 
	double minWinPropotion =0; double maxWinPropotion =1; 
	double minBidAmountPropotion =0; double maxBidAmountPropotion =1; 
	double minBidPropotion =0; double maxBidPropotion =1; 
	
	//initialisation
	
	private Auction currentAuction;
	private ArrayList<Auction> finishedAuction;
	
	private ArrayList<Bid> allBids;
	private boolean isTraining = false;
	
	protected void setup(){
		super.setup();
		final Object[] args = getArguments();
		if(args!=null && args.length==3 && args[0]!=null){

			deployAgent((Environment) args[0]); 
			BidderNumber = (int) args[1];
			FraudBidderNumber = (int) args[2];
			
		}else if(args!=null && args.length>0 && args[0]!=null ){ 
			deployAgent((Environment) args[0]); 
 
			if( args.length==2 && args[1].equals("train")) {
				this.setTraining(true);
			} 
			
		} else{
			System.err.println("Malfunction during parameter's loading of agent "+ this.getClass().getName());
			System.exit(-1);
		}
		addBehaviour(new BidderBehaviour(this));
		
		
		setFinishedAuction(new ArrayList<Auction>()); 
		setAllBids(new ArrayList<Bid>());
		

		setBidderList (new ArrayList<Bidder>() );
		setFraudBidderList (new ArrayList<Bidder> ());
		
		initBidders();
		
		printBidders();
		
		doWait(2000);	
	}	
	
	public void initBidders(){
		for (int i = 0;i<this.BidderNumber ;i++){
			int bidsPerAuctions = (int) getRandomValueByDistribution(minBidsPerAuction, maxBidsPerAuction, bidsPerAuctionDistribution) ;
			int reputation =(int) getRandomValueByDistribution(minReputation, maxReputation, reputationDistribution) ; 
			int fisrtBidTime = (int) getRandomValueByDistribution(minFirstBidTime, maxFirstBidTime, firstBidTimeDistribution) ;
			double averageBidTime =  getRandomValueByDistribution(minAverageBidTime, maxAverageBidTime, averageBidTimeDistribution) ; 
			double averageBidAmount =   getRandomValueByDistribution(minBidAmount, maxBidAmount, bidAmountDistribution)  ;
			int excessBidIncrement = (int) getRandomValueByDistribution(minExcessBidIncrement, maxExcessBidIncrement, excessBidIncrementDistribution) ;
			
		
		
			Bidder bidder = new Bidder(  bidsPerAuctions,  reputation,   fisrtBidTime,   averageBidTime,   averageBidAmount,   excessBidIncrement      );
			bidderList.add(bidder);
			
		}
		
		for (int i = 0;i<this.FraudBidderNumber ;i++){
			int bidsPerAuctions = (int) getRandomValueByDistribution(minBidsPerAuction, maxBidsPerAuction, bidsPerAuctionDistribution) ;
			int reputation =(int) getRandomValueByDistribution(minReputation, maxReputation, reputationDistribution) ; 
			int fisrtBidTime = (int) getRandomValueByDistribution(minFirstBidTime, maxFirstBidTime, firstBidTimeDistribution) ;
			double averageBidTime =  getRandomValueByDistribution(minAverageBidTime, maxAverageBidTime, averageBidTimeDistribution) ; 
			double averageBidAmount =   getRandomValueByDistribution(minBidAmount, maxBidAmount, bidAmountDistribution)  ;
			int excessBidIncrement = (int) getRandomValueByDistribution(minExcessBidIncrement, maxExcessBidIncrement, excessBidIncrementDistribution) ;
			
			Bidder bidder = new Bidder(  bidsPerAuctions,  reputation,   fisrtBidTime,   averageBidTime,   averageBidAmount,   excessBidIncrement      );
			fraudBidderList.add(bidder);
		}
	}
	

	public double getRandomValueByDistribution(double start, double end, double[] distribution){
		//check if the distribution starts at 0 and ends at 1;
		if(distribution.length>0 && distribution[0]==0 && distribution[distribution.length-1]==1){
			double v = (new Random()).nextDouble();
			int i = 0;
			for(;i<distribution.length-1;i++){
				if( v>=distribution[i] && v<=distribution[i+1]){
					break;
				}
			}
			double v2 = (new Random()).nextDouble() /distribution.length;
			return (double)(i+v2)/distribution.length*(end-start) + start;
		}else {
			System.out.println("distribution not legal, it should start with 0 and end with 1");
		}
		return 0; 
	}

	public int getRandomFirstBidTime() {
		return (int)getRandomValueByDistribution(minFirstBidTime, maxFirstBidTime, firstBidTimeDistribution);
	}
	public double getRandomAverageBidTime() {
		return getRandomValueByDistribution(minAverageBidTime, maxAverageBidTime, averageBidTimeDistribution);
	}
	public double getRandomAverageAmount() {
		return getRandomValueByDistribution(minBidAmount, maxBidAmount, bidAmountDistribution);
	}
	public int getRandomReputation() {
		return (int) getRandomValueByDistribution(minReputation, maxReputation, reputationDistribution);
	}
	
	
	
	
	public ArrayList<Bidder> getFraudBidderList() {
		return fraudBidderList;
	}

	public ArrayList<Bidder> getBidderList() {
		return bidderList;
	}

	public void setBidderList(ArrayList<Bidder> bidderList) {
		this.bidderList = bidderList;
	}

	public void setFraudBidderList(ArrayList<Bidder> fraudBidderList) {
		this.fraudBidderList = fraudBidderList;
	}
	public void printBidders(){
		for (Bidder b:this.bidderList){
			System.out.println(b);
		}
	}

	public Auction getCurrentAuction() {
		return currentAuction;
	}

	public void setCurrentAuction(Auction currentAuction) {
		this.currentAuction = currentAuction;
	}

	public ArrayList<Auction> getFinishedAuction() {
		return finishedAuction;
	}

	public void setFinishedAuction(ArrayList<Auction> finishedAuction) {
		this.finishedAuction = finishedAuction;
	}

	public ArrayList<Bid> getAllBids() {
		return allBids;
	}

	public void setAllBids(ArrayList<Bid> allBids) {
		this.allBids = allBids;
	}

	public boolean isTraining() {
		return isTraining;
	}

	public void setTraining(boolean isTraining) {
		this.isTraining = isTraining;
	}
	
	
}
