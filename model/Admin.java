package model;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;


public class Admin 
{
	Scanner in;
	Connection connection;
	PreparedStatement addItemPreparedStatement;
	Statement statement;
	
	public Admin(Connection c, Statement s)
	{
		try
		{
			in = new Scanner(System.in);
			connection = c;
			statement = s;
			addItemPreparedStatement = (PreparedStatement) connection.prepareStatement("INSERT into Items(Name, Price, Description, Category, Stock) values (?, ?, ?, ?, ?);");
		}
		catch (Exception e)
		{
			System.out.println("There was an error creating the Admin: " + e);
		}
	}

	public void startAdmin()
	{
		boolean adminLogged = true;
		int adminChoice;
		
		while(adminLogged)
		{
			try
			{
				displayAdminInterface();
				String choice = in.nextLine().trim();
				//parsing user input 
				try 
				{
			        adminChoice = Integer.parseInt(choice);
			    } 
				catch (NumberFormatException e) 
				{
			        System.out.println("Invalid option! Please select a valid option!");
			        continue;
			    }
			            	
			   	//process the options = dispatch functions based on selected option
			   	switch (adminChoice)
			   	{
			   		case 1: addItem();
			   				break;
			   		case 2: //need to implement RISHABH START HERE
			   				break;
			   		case 3: //need to implement RISHABH START HERE
			   				break;
			   		case 4: System.out.println("Logging Out...");
			   				adminLogged = false;
			   				break;
			   		default: System.out.println("Not a valid option");
			   				break;
			   	}
			   
			}
			catch(Exception e)
			{
				System.out.println("There was an error with the Admin: " + e);
			}
		}
	}
	
	public void displayAdminInterface()
	{
	  System.out.println("Please select an option from the menu:");
	  System.out.println("=======================");
	  System.out.println("|1. Add Item		|");
	  System.out.println("|2. Delete Item	|");
	  System.out.println("|3. Update Item	|");
	  System.out.println("|4. Logout		|");
	  System.out.println("=======================");
	}
	
	public void addItem()
	{
		try
		{
			System.out.println("Please enter the name of the item: ");
			String name = in.nextLine();
				
			System.out.println("Please enter the price of the item: ");
			int price  = in.nextInt();
			
			System.out.println("Please enter the description of the item: ");
			String description = in.nextLine();
			
			System.out.println("Please enter the categories of the item seperated by a comma: ");
			String category = in.nextLine();
				
			System.out.println("Please enter the quantity of the item: ");
			int stock = in.nextInt();
				       	            		
			adminAddItem(name, price, description, category, stock);
		}
		catch(Exception e)
		{
			System.out.println("Error in adding an item: " + e);
		}
	}
	
	public void adminAddItem(String name, int price, String description, String category, int stock) throws SQLException
	{
	    try 
	    {
	    	addItemPreparedStatement.setString(1, name);
	    	addItemPreparedStatement.setInt(2, price);
	    	addItemPreparedStatement.setString(3, description);
	    	addItemPreparedStatement.setString(4, category);
	    	addItemPreparedStatement.setInt(5, stock);
	    	addItemPreparedStatement.executeUpdate();
	    }
	    catch (SQLException e) 
	    {
	      e.printStackTrace();
	    } 

	  }
	  
}
	
