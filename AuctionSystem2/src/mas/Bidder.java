package mas;

import java.util.ArrayList;

public class Bidder {
	private int bidderId;

	private static int  BidderNumbers=0;
	private int bidsPerAuctions;
	private int reputation;
	private int fisrtBidTime;
	private double averageBidTime;
	private double averageBidAmount; 
	private int excessBidIncrement;  
	
	private ArrayList <Bid> bids;
	private ArrayList <Auction> auctionWon;
	private ArrayList <Auction> allAuction;
	
	public Bidder() {
		bidderId = this.BidderNumbers;
		Bidder.BidderNumbers+=1;
		bids = new ArrayList <Bid>();
		setAuctionWon(new ArrayList <Auction>());
		setAllAuction(new ArrayList <Auction>());
		
	}
	public Bidder(int bidsPerAuctions,int reputation, int fisrtBidTime, double averageBidTime, double averageBidAmount, int excessBidIncrement   ){
		this() ;
		this.bidsPerAuctions =bidsPerAuctions ;
		this.reputation =reputation ;
		this.fisrtBidTime = fisrtBidTime;
		this.averageBidTime =averageBidTime ;
		this.averageBidAmount =averageBidAmount ;
		this.excessBidIncrement = excessBidIncrement; 
	}
	
	public int getBidderId() {
		return bidderId;
	}
	public void setBidderId(int bidderId) {
		this.bidderId = bidderId;
	}
	public static int getBidderNumbers() {
		return BidderNumbers;
	}
	public static void setBidderNumbers(int bidderNumbers) {
		BidderNumbers = bidderNumbers;
	}
	public int getBidsPerAuctions() {
		return bidsPerAuctions;
	}
	public void setBidsPerAuctions(int bidsPerAuctions) {
		this.bidsPerAuctions = bidsPerAuctions;
	}
	public int getReputation() {
		return reputation;
	}
	public void setReputation(int reputation) {
		this.reputation = reputation;
	}
	public int getFisrtBidTime() {
		return fisrtBidTime;
	}
	public void setFisrtBidTime(int fisrtBidTime) {
		this.fisrtBidTime = fisrtBidTime;
	}
	public double getAverageBidTime() {
		return averageBidTime;
	}
	public void setAverageBidTime(double averageBidTime) {
		this.averageBidTime = averageBidTime;
	}
	public double getAverageBidAmount() {
		return averageBidAmount;
	}
	public void setAverageBidAmount(double averageBidAmount) {
		this.averageBidAmount = averageBidAmount;
	}
	public int getExcessBidIncrement() {
		return excessBidIncrement;
	}
	public void setExcessBidIncrement(int excessBidIncrement) {
		this.excessBidIncrement = excessBidIncrement;
	} 

	@Override 
	public String toString(){
		return "<Bidder:" + bidderId +", bidsPerAuctions:"  + bidsPerAuctions+", reputation;"+ reputation+", fisrtBidTime:"+  fisrtBidTime+", averageBidTime:" + String.format("%.2f",averageBidTime)+", averageBidAmount:"+   String.format("%.2f",averageBidAmount)+", excessBidIncrement:"+   excessBidIncrement +">";
	
	}
	@Override
	public boolean equals(Object obj){ 
	       if (obj == null) {
	           return false;
	       }
	       if (getClass() != obj.getClass()) {
	           return false;
	       }
	       final Bidder other = (Bidder) obj;
	       if (other.getBidderId() == this.getBidderId() ){ 
	           return true;
	       } 
	       
	       return false;
		
	}
	public ArrayList <Bid> getBids() {
		return bids;
	}
	public void setBids(ArrayList <Bid> bids) {
		this.bids = bids;
	}
	

	public String getAverageData(ArrayList<Auction> auction ) {
	  	double reputation=0;
//	 	int fisrtBidTime=0;
		double averageBidTime=0;
	 	double averageBidAmount=0; 
// 		int excessBidIncrement=0; 
	 	double bidsPerAuction=0;
		

		if (this.getBids().size()==0)
			return ""+averageBidTime+" "+averageBidAmount+" "+bidsPerAuction;
		Bid bid;Auction auc;
		for (int i =0;i<this.getBids().size();i++) {
			bid = this.getBids().get(i);
			auc = auction.get(bid.getAuctionId());
			averageBidTime+= (double) bid.getBidTime()/auc.getEndTime();
			averageBidAmount+= (double) bid.getBidAmount()/auc.getEndPrice(); 
		}
		for (int i =0;i<this.getAuctionWon().size();i++) {
			reputation+=this.getAuctionWon().get(i).getStars();
		}
		averageBidTime/=this.getBids().size();
		averageBidAmount/=this.getBids().size();
		if(this.allAuction.size()!=0)
			bidsPerAuction = (double)this.getBids().size()/this.allAuction.size();
		if(this.getAuctionWon().size()!=0)
			reputation/=this.getAuctionWon().size();
		else 
			reputation+=this.reputation;
		
		return ""+String.format("%.3f", averageBidTime)+" "+String.format("%.3f", averageBidAmount)+" "+String.format("%.3f", bidsPerAuction)+" "+String.format("%.3f", reputation);
	}
	public ArrayList <Auction> getAuctionWon() {
		return auctionWon;
	}
	public void setAuctionWon(ArrayList <Auction> auctionWon) {
		this.auctionWon = auctionWon;
	}
	public ArrayList <Auction> getAllAuction() {
		return allAuction;
	}
	public void setAllAuction(ArrayList <Auction> allAuction) {
		this.allAuction = allAuction;
	}
	
	
}
