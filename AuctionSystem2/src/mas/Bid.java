package mas;

public class Bid {
	private static int BidNumber = 0;
	private int BidId;
	private int UserId;
	private int AuctionId;
	private int BidTime;
	private int BidAmount;
	
	public Bid(){
		this.setBidId(Bid.BidNumber);
		Bid.BidNumber+=1;
	}
	

	public Bid(int AuctionId, int UserId , int BidTime, int BidAmount ){
		this();
		this.setAuctionId(AuctionId);
		this.setUserId(UserId);
		this.setBidTime(BidTime);
		this.setBidAmount(BidAmount); 
	}
	
	public int getBidId() {
		return BidId;
	}
	public void setBidId(int bidId) {
		BidId = bidId;
	}

	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	public int getAuctionId() {
		return AuctionId;
	}
	public void setAuctionId(int auctionId) {
		AuctionId = auctionId;
	}
	public int getBidTime() {
		return BidTime;
	}
	public void setBidTime(int bidTime) {
		BidTime = bidTime;
	}
	public int getBidAmount() {
		return BidAmount;
	}
	public void setBidAmount(int bidAmount) {
		BidAmount = bidAmount;
	}
	
	public String toString(){
		return "<"+"BidId:"+BidId+", AuctionId:"+AuctionId+", UserId:"+UserId+", BidTime:"+BidTime+", BidAmount:"+BidAmount+">"  ;  
		
	}

}
