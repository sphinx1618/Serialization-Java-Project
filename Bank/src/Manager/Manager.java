package Manager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import User.user;
import Cashier.cashier;

public class Manager implements Serializable {

	protected ArrayList<user> NewUsers;
	protected ArrayList<user> users;
	static Scanner in;
	public cashier TCashier[]; 
	public String name;
	protected ArrayList<check> pendingChecks;
	
	public void RequestRegistration(user NewUser)
	{
		NewUsers.add(NewUser);
	}
	
	public int getNid()
	{
		if(NewUsers.size()!=0)
			return NewUsers.get(NewUsers.size()-1).id;
		if(users.size()!=0)
			return users.get(users.size()-1).id;
		return 0;
	}
	
	public void ApproveAccounts()
	{
		in=new Scanner(System.in);
		System.out.println("Following are the Pending requests:");
		for(int i =0; i<NewUsers.size(); i++)
		{
			System.out.println("User Name: "+NewUsers.get(i).name);
			System.out.println("Approve? (Y/N)");
			char ch = in.nextLine().charAt(0);
			if(ch=='Y')
			{
				users.add(NewUsers.get(i));
				System.out.println("User created!");
				TCashier[0].addAccount(NewUsers.get(i).AcNo);
				
			}
		}
		NewUsers = new ArrayList<user>();
	}
	
	
	public user getUser(int id)
	{
		for(int i =0; i<users.size(); i++)
		{
			if(users.get(i).id == id)
			{
				return(users.get(i));
			}
		}
		return(null);
	}
	
	
	
	
	public void requestCheckApproval(check c)
	{
		pendingChecks.add(c);
	}
	
	private void ApproveCheck()
	{
		System.out.println("Following are the Pending Checks:");
		in=new Scanner(System.in);
		for(int i =0; i<pendingChecks.size(); i++)
		{
			System.out.println(pendingChecks.get(i));
			System.out.println("Approve? (Y/N)");
			char ch = in.nextLine().charAt(0);
			if(ch=='Y')
			{
				TCashier[0].processCheck(pendingChecks.get(i));
				System.out.println("Check Approved! ");
			}
		}
	}
	
	public void closeUser(user ThisUser)
	{
		users.remove(ThisUser);
		
	}
	
	public void updateUser(user u)
	{
		Manager m;
		try {
			m = getInstance();
			for(int i =0; i<m.users.size(); i++)
			{
				if(m.users.get(i).id == u.id)
				{
					m.users.set(i, u);
				}
			}
			m.SaveState(m);
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	public void ViewDetails()
	{
		for(int i =0; i<users.size(); i++)
		{
			System.out.println(users.get(i));
			System.out.println("Account Balance: "+TCashier[0].CheckBalance(users.get(i).AcNo));
		}
	}
	
	public Manager()
	{
		this.NewUsers = new ArrayList<user>();
		this.users = new ArrayList<user>();
		this.TCashier = new cashier[1];
		this.pendingChecks = new ArrayList<check>();
		this.name="Mohd Huzaifa";
	}
	
	public static Manager getInstance() throws IOException
	{
		
		try {
			ObjectInputStream in=new ObjectInputStream(new FileInputStream("mng.txt"));
            Manager manager=(Manager)in.readObject();
            in.close();
            return manager;
            
		}catch(Exception e)
		{
			Manager manager = new Manager();
			FileOutputStream fout=new FileOutputStream("mng.txt");
			ObjectOutput out=new ObjectOutputStream(fout);
			out.writeObject(manager);
			out.flush();
            out.close();
			System.out.println("New Manager created!");
            return manager;
		}
	}
	
	public void SaveState(Manager manager) throws IOException
	{
		
		FileOutputStream fout=new FileOutputStream("mng.txt");
		ObjectOutput out=new ObjectOutputStream(fout);
		out.writeObject(manager);
		out.flush();
        out.close();
        
        
        
	}
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("You have entered the Manager Menu:\n");
		in = new Scanner(System.in);
		Manager manager = getInstance();
		System.out.println("Welcome "+manager.name+" !");
		Boolean Cont=true;
		while(Cont)
		{
			System.out.println("What would you like to do :");

			System.out.println("1). Approve Accounts");
			System.out.println("2). Create Cashier Account");
			System.out.println("3). View Bank Details");
			System.out.println("4). Approve Checks\n");
			
			
			System.out.println("Enter you choice: ");
			int choice = in.nextInt();
			
			switch(choice)
			{
			case 1:
				try {
				manager.ApproveAccounts();
				manager.TCashier = getInstance().TCashier;
				manager.SaveState(manager);
				}catch(Exception e)
				{	System.out.println(e);
					System.out.println("No Pending Accounts Request");}
				break;
				
			case 2:
				manager.TCashier[0] = cashier.getInstance("Mohd Huzaifa");
				System.out.println("Cashier account created!");
				manager.SaveState(manager);
				break;
				
			case 3:
				try {
				manager.ViewDetails();
				}catch(Exception e)
				{System.out.println("No Account found yet!");}
				break;
				
			case 4:
				try {
				manager.ApproveCheck();
				}catch(Exception e)
				{System.out.println("No Pending Checks for approval");}
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
