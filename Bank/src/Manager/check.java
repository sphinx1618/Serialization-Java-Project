package Manager;

import java.io.Serializable;

public class check implements Serializable{

	public int from;
	public int to;
	public int amount;
	
	public check(int from,int to, int amount)
	{
		this.from=from;
		this.to=to;
		this.amount=amount;
	}
	
	public String toString()
	{
		return("Check from "+this.from+" to "+this.to+" of amount : "+this.amount);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
