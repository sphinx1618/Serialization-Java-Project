package User;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import Manager.Manager;
import Cashier.cashier;
import Manager.check;

public class user implements Serializable{

	public int id;
	public String name;
	public int AcNo;
	
	private static cashier ThisCashier;
	private static Manager manager;
	public static int nextid;
	private static int nextAcNo;
	
	
	
	private static Scanner in;
	
	public void setName(String Name)
	{
		this.name = Name;
	}
	
	public user(String name)
	{
		
		try {
			Manager m = Manager.getInstance();
			nextid = m.getNid()+1;
			nextAcNo = nextid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.id=nextid++;
		this.name=name;
		this.AcNo=nextAcNo++;
	}
	
	public static user RequestRegistration() throws IOException
	{
		System.out.println("Enter your Name:");
		Scanner inp = new Scanner(System.in);
		String name = inp.nextLine();
		user NewUser = new user(name);
		manager.RequestRegistration(NewUser);
		System.out.println("User creation request generated! Wait for the manager's approval!");
		manager.SaveState(manager);
		return NewUser;
	}
	
	
	public static void UpdateProfile(user ThisUser)
	{
		System.out.println("You can update your name here!");
		System.out.println("Enter new name!");
		in = new Scanner(System.in);
		String Name = in.nextLine();
		ThisUser.setName(Name);
		System.out.println("Name updated! \n");
		
	}
	
	public static void RequestWithdraw(user ThisUser)
	{
		System.out.println("Enter the amount you want to withdraw!");
		in = new Scanner(System.in);
		int amount = in.nextInt();
		System.out.println(ThisCashier.requestWithdraw(ThisUser.AcNo, amount));
		
	}
	
	public static void RequestDeposit(user ThisUser)
	{
		System.out.println("Enter the amount you want to Deposit!");
		in = new Scanner(System.in);
		int amount = in.nextInt();
		System.out.println(ThisCashier.requestDeposit(ThisUser.AcNo, amount));
		
	}
	
	public void CheckBalance(user ThisUser)
	{
		
		System.out.println(ThisCashier.CheckBalance(ThisUser.AcNo));
	}
	
	public static void CheckDeposit(user ThisUser)
	{
		in = new Scanner(System.in);
		
		System.out.println("Check from :");
		int from=in.nextInt();
		
		System.out.println("Enter the amount on the Check");
		int amount = in.nextInt();
		
		ThisCashier.CheckDeposit(new check(from,ThisUser.AcNo,amount));
		
		
	}
	
	public static void requestTransfer(user ThisUser)
	{
		in = new Scanner(System.in);
		
		System.out.println("Transfer to Acc no:");
		int to=in.nextInt();
		
		System.out.println("Enter the amount : ");
		int amount = in.nextInt();
		
		System.out.println(ThisCashier.transfer(ThisUser.AcNo,to,amount));
		
	}
	
	public static void requestClose(user ThisUser)
	{
		ThisCashier.closeAcc(ThisUser.AcNo);
		manager.closeUser(ThisUser);
		ThisUser=null;
		System.gc();
		
	}
	
	public String toString()
	{
		return("User: "+name+" \nid: "+id+" \nAccount: "+AcNo);
	}
	
	@Override
	protected void finalize()
	{
		System.out.println("The user is deleted");
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		
		manager = Manager.getInstance();
		ThisCashier = manager.TCashier[0];
		
		in = new Scanner(System.in);
		user ThisUser=null;
		
		System.out.println("Are you a new User? (Y/N): ");
		char ch=in.next().charAt(0);
		if(ch=='N')
		{
			System.out.println("Login with your UserId :");
			int uid=in.nextInt();
			
			ThisUser=manager.getUser(uid);
			if(ThisUser!=null) {
				System.out.println("Logged In");
				System.out.println("Welcome "+ThisUser.name+" !");
			}else
			{
				System.out.println("Login Failed");
			}
		}
			
		
		
		System.out.println("You have entered the User Menu:\n");
		Boolean Cont=true;
		while(Cont)
		{
			System.out.println("What would you like to do :");
	
			System.out.println("1). Request Registration");
			System.out.println("2). Update Profile");
			System.out.println("3). Withdraw amount");
			System.out.println("4). Deposit amount");
			System.out.println("5). Check Balance");
			System.out.println("6). Check Deposit/Clearance");
			System.out.println("7). Transfer Amount");
			System.out.println("8). Close Account\n");
			
			
			System.out.println("Enter you choice: ");
			
			int choice = in.nextInt();
			switch(choice)
			{
			case 1:
				
				if(ThisUser!=null)
				{
					System.out.println("User Already registered! ");
				}else {
					try {
					ThisUser = RequestRegistration();
					}catch(Exception e)
					{
						System.out.println(e);
						System.out.println("Manager not Found! Initiate Bank system first. ");
					}
				}
				break;
				
			case 2:
				if(ThisUser!=null)
				{
					ThisUser.UpdateProfile(ThisUser);
					manager.updateUser(ThisUser);
				}else {
					System.out.println("User don't exist! ");
				}
				break;
				
			case 3:
				if(ThisUser!=null)
				{
					ThisUser.RequestWithdraw(ThisUser);
				}else {
					System.out.println("User don't exist! ");
				}
				break;
				
			case 4:
				if(ThisUser!=null)
				{
					ThisUser.RequestDeposit(ThisUser);
				}else {
					System.out.println("User don't Exist! ");
				}
				break;
			case 5:
				if(ThisUser!=null)
				{
					ThisUser.CheckBalance(ThisUser);
				}else {
					System.out.println("User don't Exist! ");
				}
				break;
			case 6:
				if(ThisUser!=null)
				{
					ThisUser.CheckDeposit(ThisUser);
				}else {
					System.out.println("User don't Exist! ");
				}
				break;
			case 7:
				if(ThisUser!=null)
				{
					ThisUser.requestTransfer(ThisUser);
				}else {
					System.out.println("User don't Exist! ");
				}
				break;
			case 8:
				if(ThisUser!=null)
				{
					ThisUser.requestClose(ThisUser);
				}else {
					System.out.println("User don't Exist! ");
				}
				break;
				
			default:
				System.out.println("You entered the wrong choice! ");
			
			}
			
			
			
			System.out.println("Exit? (Y/N) :");
			ch=in.next().charAt(0);
			if(ch=='Y')
			{
				Cont=false;
			}
			
		}
	}

}
