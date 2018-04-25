package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Users {

	int uID;
	String Name;
	String Email;
	int CreditCard;
	String Address;
	String Password;
	Scanner in;
	PreparedStatement viewOrdersPreparedStatement;
	List<Item> cart;
	
	public Users(Connection c, Statement s, int uID, String Name, String Email, int CreditCard, String Address, String Password)
	{
		try
		{
			this.uID = uID;
			this.Name = Name;
			this.Email = Email;
			this.CreditCard = CreditCard;
			this.Address = Address;
			this.Password = Password;
			in = new Scanner(System.in);
			cart = new ArrayList<Item>();
			
			//All Prepared Statements
			viewOrdersPreparedStatement = (PreparedStatement) c.prepareStatement("Select * from users where uID = ?;");
		}
		catch(Exception e)
		{
			System.out.println("There was an error creating the User");
		}
		
	}

	 public void displayUserInterface()
	 {
		  System.out.println("Please select an option from the menu:");
		  System.out.println("=======================");
	      System.out.println("|1. View Orders			|");
	      System.out.println("|2. Search for Item		|");
	      System.out.println("|3. View Cart				|");
	      System.out.println("|4. Checkout				|");
	      System.out.println("|5. Remove Item from List	|");
	      System.out.println("|6. Logout				|");
	      System.out.println("=======================");
	  }
	 
	 public void startUser()
	 {
		 try
		 {
			 boolean runningUser = true;
			 while (runningUser)
			 {
				displayUserInterface();
				String choice = in.nextLine().trim();
				int userChoice;
				//parsing user input //handling invalid input format exception
				try 
				{
			        userChoice = Integer.parseInt(choice);
			    } 
				catch (NumberFormatException e) 
				{
			        System.out.println("Invalid option! Please select a valid option!");
			        continue;
			    }
				
				switch (userChoice)
				{
					case 1: viewOrders(); //need to test
							break;
					case 2: searchForItems(); //need to implement
							break;
					case 3: viewCart();	//need to test
							break;
					case 4: checkout(); //need to implement
							break;
					case 5:	removeItemFromCart(); // need to check
							break;
					case 6:	System.out.println("Logging Out...");
	        				runningUser = false;
	        				break;
	        		default:System.out.println("Not a valid option");
	        				break;
							
				}
					
			 }
		 }
		 catch(Exception e)
		 {
			 System.out.println("There was an error while running the user: " + e);
		 }
	 }
	  
	 public void viewOrders()
	 {
		 try
		 {
			 viewOrdersPreparedStatement.setInt(1, uID);
			 ResultSet rs = viewOrdersPreparedStatement.executeQuery();
			 
			 int orderNumber = 0;
			 boolean firstOrder = false;
			 while(rs.next())
			 {
				 if (!firstOrder)
				 {
					 System.out.println("Previous orders you purchased: ");
					 firstOrder = true;
				 }
				 orderNumber++;
				 System.out.println("Order Number " + orderNumber + ": " + rs.getString(2));
			 }
			 if (!firstOrder)
			 {
				 System.out.println("There are no previous orders");
			 }
			 
		 }
		 catch(Exception e)
		 {
			 System.out.println("There was an error with viewing the Orders");
		 }
		 
	 }
	 
	 public void searchForItems()
	 {
		 //ZAHRA STARTS HERE
		 try
		 {
			 
		 }
		 catch(Exception e)
		 {
			 System.out.println("Error in searching for items: " + e);
		 }
	 }
	 
	 public void viewCart()
	 {
		 for (int i = 0; i < cart.size(); i++)
		 {
			 Item currentItem = cart.get(i);
			 System.out.print("Name: " + currentItem.Name + ", Price: $" + currentItem.Price);
		 }
	 }
	 
	 public void checkout()
	 {
		 //MICHAEL STARTS HERE
		 try
		 {
			 
		 }
		 catch (Exception e)
		 {
			 System.out.println("Error in checking out: " + e);
		 }
		 
		 
	 }
	 
	 public void removeItemFromCart()
	 {
		 boolean validItem = false;
		 while (!validItem)
		 {
			 try
			 {
				 System.out.println("Please select a number to remove:");
				 for (int i = 0; i < cart.size(); i++)
				 {
					 Item item = cart.get(i);
					 System.out.println((i+1) + " ) " + item.Name);
				 }
				 int removeNumber = in.nextInt();
				 try
				 {
					 cart.remove((removeNumber-1));
					 validItem = true;
				 }
				 catch(Exception e)
				 {
					 System.out.println("Please choose a valid number");
				 }
			 }
			 catch(Exception e)
			 {
				 System.out.println("There was an error with the selection: " + e);
				 continue;
			 }
		 }
	 }
	 
}