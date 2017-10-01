package mas;

import java.util.ArrayList;

public class Auction {
	private static int AutcionNumber = 0;
	private int AutcionId ;
	private int StartTime ;
	private int EndTime ;
	private int Duration;
	private int StartPrice;
	private int CurrentPrice=0;
	private int CurrentTime=0;
	private int EndPrice;
	private int ItemValuation;
	
	private ArrayList<Bid> bidsList;
	
	private int winner;
	private int stars;
	
	public Auction(){
		this.setAutcionId(Auction.AutcionNumber);
		Auction.AutcionNumber+=1;
		
		setBidsList(new ArrayList<Bid>());
	}
 
	public void createFromString(String content){
		 String[] fields = content.split("_"); 
		 StartTime =  Integer.parseInt(fields[0]);
		 Duration =  Integer.parseInt(fields[1]);  
		 ItemValuation =  Integer.parseInt(fields[2]);
		 StartPrice =  Integer.parseInt(fields[3]);  
		 EndTime  =Duration+StartTime;
	}
	
	public int getAutcionId() {
		return AutcionId;
	}

	public void setAutcionId(int autcionId) {
		AutcionId = autcionId;
	}

	public int getStartTime() {
		return StartTime;
	}

	public void setStartTime(int startTime) {
		StartTime = startTime;
	}

	public int getEndTime() {
		return EndTime;
	}

	public void setEndTime(int endTime) {
		EndTime = endTime;
	}

	public int getDuration() {
		return Duration;
	}

	public void setDuration(int duration) {
		Duration = duration;
	}

	public int getItemValuation() {
		return ItemValuation;
	}

	public void setItemValuation(int itemValuation) {
		ItemValuation = itemValuation;
	}

	public int getStartPrice() {
		return StartPrice;
	}

	public void setStartPrice(int startPrice) {
		StartPrice = startPrice;
	}

	public int getEndPrice() {
		return EndPrice;
	}

	public void setEndPrice(int endPrice) {
		EndPrice = endPrice;
	}

	public ArrayList<Bid> getBidsList() {
		return bidsList;
	}

	public void setBidsList(ArrayList<Bid> bidsList) {
		this.bidsList = bidsList;
	}

	public int getCurrentPrice() {
		return CurrentPrice;
	}

	public void setCurrentPrice(int currentPrice) {
		CurrentPrice = currentPrice;
	}

	public int getCurrentTime() {
		return CurrentTime;
	}

	public void setCurrentTime(int currentTime) {
		CurrentTime = currentTime;
	}
	@Override
	public String toString(){
		String str =  "Auction<\nAutcionId:"+  AutcionId +", StartTime:"+  StartTime+", EndTime:"+  EndTime+", StartPrice:"+  StartPrice +", EndPrice:"+EndPrice+", ItemValuation"+ItemValuation+">";
		str+="\n";
		str += "Bids<\n";
		for(Bid bid : this.getBidsList()){
			str+=bid+"\n";
		}
		str+=">";
		return str;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

}
