package Cashier;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import Manager.Manager;
import Manager.check;


public class cashier implements Serializable {

	public int id;
	public String name;
	
	cashier ThisCashier = null;
	
	public HashMap<Integer,Integer> AccBalances;
	
	private int nextid;
	private int nextAcNo;
	static Scanner in;
	
	
	
	public void addAccount(int acc)
	{
		ThisCashier=getInstance("TemporaryCashier");
		try
		{	
			ThisCashier.AccBalances.put(acc,0);
			
		}catch(Exception e)
		{
			System.out.println("Cashier doesnot Exist yet!");
			
		}
		
		updateCashier(ThisCashier);
	}
	
	public String requestWithdraw(int acc,int amount)
	{
		return(Withdraw(acc,amount));
	}
	
	public String requestDeposit(int acc,int amount)
	{
		return(Deposit(acc,amount));
	}
	
	private String Withdraw(int acc,int amount)
	{
		ThisCashier=getInstance("TemporaryCashier");
		int bal=ThisCashier.AccBalances.get(acc);
		if(bal>=amount)
		{
			ThisCashier.AccBalances.replace(acc, bal-amount);
			updateCashier(ThisCashier);
			return("Successfull! New Balance : "+(bal-amount));
		}else
		{
			return("Not Enough Balance");
		}
	}
	
	private String Deposit(int acc,int amount)
	{
		ThisCashier=getInstance("TemporaryCashier");
		int bal=ThisCashier.AccBalances.get(acc);
		ThisCashier.AccBalances.replace(acc, bal+amount);
		updateCashier(ThisCashier);
		return("Successfull Deposit! New Balance : "+(bal+amount));
	}
	
	public String CheckBalance(int acc)
	{
		ThisCashier=getInstance("TemporaryCashier");
		int bal=ThisCashier.AccBalances.get(acc);
		return("Your Balance is : "+bal);
	}
	
	
	public void CheckDeposit(check c)
	{
		CheckApproval(c);
		System.out.println("Check sent for Approval!");
	}
	
	
	private void CheckApproval(check c)
	{
		
		try {
			Manager m = Manager.getInstance();
			m.requestCheckApproval(c);
			m.SaveState(m);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	public void processCheck(check c)
	{
		String result;
		try {
		result=Withdraw(c.from,c.amount);
		}catch(Exception e)
		{
			System.out.println("Wrong Account No! Please Check Again!");
			return;
		}
		if(result.charAt(0)=='S') //successful withdraw
		{
			Deposit(c.to,c.amount);
			
		}else
		{	
			System.out.println("Check Bounced :( Not Enough balance!");
		}
		
		
	}
	
	public String transfer(int from, int to, int amount)
	{
		try {
			Deposit(to,0);
		}catch(Exception e)
		{System.out.println("Wrong Account number! Please Check Again!");
			return "Transfer Failed!";}
		
		String result=Withdraw(from,amount);
		
		if(result.charAt(0)=='S') //successful withdraw
		{	
			Deposit(to,amount);
			return("Successfully Transferred the amount!");
		}else
		{	
			return("Transfer Failed! Not Enough Balance! ");
		}
	}
	
	public void closeAcc(int acc)
	{
		ThisCashier=getInstance("TemporaryCashier");
		ThisCashier.AccBalances.remove(acc);
		updateCashier(ThisCashier);
	}
	
	public cashier()
	{
		AccBalances = new HashMap<Integer,Integer>();
	}
	
	public void updateCashier(cashier ThisCashier) 
	{
		Manager mng;
		try {
			mng = Manager.getInstance();
			mng.TCashier[0] = ThisCashier;
			mng.SaveState(mng);
		}
		catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public cashier(String name)
	{
		this.ThisCashier = null;
		this.id = 1;
		this.name = name;
		this.AccBalances = new HashMap<Integer,Integer>();
	}
	
	public static cashier getInstance(String name)
	{
		try {
			cashier c = Manager.getInstance().TCashier[0];
			if(c!=null) {
			
			return c;
			}else {
				System.out.println("New Cashier Created!");
				return new cashier(name);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return new cashier("Temporary");
			
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("You have entered the Cashier Menu:\n");
		in = new Scanner(System.in);
		cashier c = getInstance("temporary");
		Boolean Cont=true;
		while(Cont)
		{
	
			System.out.println("What would you like to do :");

			System.out.println("1). Check Balance");
			System.out.println("2). Deposit Money");
			System.out.println("3). Withdraw Money");
			System.out.println("4). Request for Check Approval");
			
			
			int acc,amount;
			System.out.println("Enter you choice: ");
			int choice = in.nextInt();
			switch(choice)
			{
			case 1:
				try {
				System.out.println("Enter the account No: ");
				acc = in.nextInt();
				System.out.println(c.CheckBalance(acc));
				}catch(Exception e)
				{
					System.out.println("Account doesn't exist!");
				}
				break;
				
			case 2:
				try {
				System.out.println("Enter the account No: ");
				acc = in.nextInt();
				System.out.println("Enter amount: ");
				amount = in.nextInt();
				System.out.println(c.Deposit(acc,amount));
				}catch(Exception e)
				{
					System.out.println("Account doesn't exist!");
				}
				break;
				
			case 3:
				try {
				System.out.println("Enter the account No: ");
				acc = in.nextInt();
				System.out.println("Enter amount: ");
				amount = in.nextInt();
				System.out.println(c.Withdraw(acc,amount));
				}catch(Exception e)
				{
					System.out.println("Account doesn't exist!");
				}
				break;
				
			case 4:
				try {
				System.out.println("Check from :");
				int from=in.nextInt();
				
				System.out.println("Check to: ");
				int to = in.nextInt();
				
				System.out.println("Enter the amount on the Check");
				amount = in.nextInt();
				
				c.CheckDeposit(new check(from,to,amount));
				}catch(Exception e)
				{
					System.out.println("Account doesn't exist!");
				}
				break;
			
			default:
				System.out.println("You entered the wrong choice! ");
			
			}
			
			System.out.println("Exit? (Y/N) :");
			char ch=in.next().charAt(0);
			if(ch=='Y')
			{
				Cont=false;
			}
			
		}
		
	}

}
