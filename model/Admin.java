package model;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;


public class Admin 
{
	Scanner in;
	
	Connection connection;
	PreparedStatement addItemPreparedStatement;
	PreparedStatement deleteItemPreparedStatement;
	PreparedStatement updateItemPreparedStatement;
	PreparedStatement viewAllUserOrders;

	Statement statement;
	
	public Admin(Connection c, Statement s)
	{
		try
		{
			in = new Scanner(System.in);
			connection = c;
			statement = s;
			addItemPreparedStatement = (PreparedStatement) connection.prepareStatement("INSERT into Items(Name, Price, Description, Category, Stock) values (?, ?, ?, ?, ?);");
			deleteItemPreparedStatement = (PreparedStatement) connection.prepareStatement("Delete from Items where iID = ?;");
			updateItemPreparedStatement = (PreparedStatement) connection.prepareStatement("Update Items set Name = ?, Price = ?, Description = ?, Category = ?, Stock = ? where iID = ?");
			viewAllUserOrders = (PreparedStatement) connection.prepareStatement("select items from orders where uID = (select uID from users where uID = ?)");

		}
		catch (Exception e)
		{
			System.out.println("There was an error creating the Admin: " + e);
		}
	}

	public void startAdmin()
	{
		boolean adminLogged = true;
		
		while(adminLogged)
		{
			try
			{
				displayAdminInterface();
				String choice = in.nextLine().trim();
				int adminChoice;

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
			   		case 2: deleteItem();
			   				break;
			   		case 3: updateItem();
			   				break;
			   		case 4: viewAllOrders();
			   				break;
			   		case 5: System.out.println("Logging Out...");
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
	  System.out.println("|1. Add Item			|");
	  System.out.println("|2. Delete Item		|");
	  System.out.println("|3. Update Item		|");
	  System.out.println("|4. View All Orders	|");
	  System.out.println("|5. Logout			|");
	  System.out.println("=======================");
	}
	
	//Below are the functions for the Admin: Add, Update, Delete, Update
	//????????????????????????????????Work on the error catching! What if there is wrong input by the user
	
	//Add item
	public void addItem()
	{
		try
		{
			//asking for info regarding the item to be added
			Scanner ini = new Scanner(System.in);
			System.out.println("Please enter the name of the item: ");
			String name = in.nextLine();

			
			System.out.println("Please enter the price of the item: ");
			int price  = ini.nextInt();
			
			
			System.out.println("Please enter the description of the item: ");
			String description = in.nextLine();
			
			System.out.println("Please enter the categories of the item seperated by a comma: ");
			String category = in.nextLine();
				
			System.out.println("Please enter the quantity of the item: ");
			int stock = in.nextInt();
			
			//helper method
			adminAddItem(name, price, description, category, stock);
		}
		catch(Exception e)
		{
			System.out.println("Error in adding an item: " + e);
		}
	}
	
	//Add item helper method
	public int adminAddItem(String name, int price, String description, String category, int stock) throws SQLException
	{
		int result = 0;
	    try 
	    {
	    	//using the info of the item and executing the sql statement to update the database
	    	addItemPreparedStatement.setString(1, name);
	    	addItemPreparedStatement.setInt(2, price);
	    	addItemPreparedStatement.setString(3, description);
	    	addItemPreparedStatement.setString(4, category);
	    	addItemPreparedStatement.setInt(5, stock);
	    	addItemPreparedStatement.execute();
	    	result = addItemPreparedStatement.executeUpdate();
	    }
	    catch (SQLException e) 
	    {
	      e.printStackTrace();
	    }
	    finally
	    {
	    	addItemPreparedStatement.close();
	    }
	    return result;

	  }
	
	//Delete item
	public void deleteItem() throws SQLException
	{
		Scanner ini = new Scanner(System.in);
		//asking for info regarding the item to be updated
		System.out.println("Please enter the ID of the item to be deleted: ");
		int id = ini.nextInt();
		
		
		adminDeleteItem(id);
	}
	
	//Delete item helper method
	public void adminDeleteItem(int id) throws SQLException
	{
		try
		{
	    	//using the info of the item and executing the sql statement to update the database
			deleteItemPreparedStatement.setInt(1, id);		
			deleteItemPreparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}
	
	public void viewAllOrders() throws SQLException
	{
		Scanner ini = new Scanner(System.in);
		//asking for info regarding the item to be updated
		System.out.println("Please enter the ID of the user to view his orders: ");
		int id = ini.nextInt();
		viewAllUserOrders.setInt(1, id);		
		viewAllUserOrders.executeQuery();
		
	}
		
		
		
	
	//update item
	public void updateItem()
	{
		//asking for info regarding the item to be deleted
		Scanner ini = new Scanner(System.in);
		System.out.println("Please enter the ID of the item to be updated: ");
		int id = ini.nextInt();
		
		System.out.println("Please enter the new Name of the item: ");
		String name = in.nextLine();
		
		System.out.println("Please enter the new Price of the item: ");
		int price = ini.nextInt();
		
		System.out.println("Please enter the new Description of the item: ");
		String description = in.nextLine();
		
		System.out.println("Please enter the new Categories of the item: ");
		String category = in.nextLine();
		
		System.out.println("Please enter the new Stock for the item: ");
		int stock = ini.nextInt();
		
		adminUpdateItem(name, price, description, category, stock, id);
	}
	
	//Update item helper method
	public void adminUpdateItem(String name, int price, String description, String category, int stock, int id)
	{
    	//using the info of the item and executing the sql statement to update the database
		
		try
		{
			updateItemPreparedStatement.setString(1, name);
			updateItemPreparedStatement.setInt(2, price);
			updateItemPreparedStatement.setString(3, description);
			updateItemPreparedStatement.setString(4, category);
			updateItemPreparedStatement.setInt(5, stock);
			updateItemPreparedStatement.setInt(6, id);
			updateItemPreparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}
	
	//Logs the Admin out of the system
	public void logout()
	{
		
	}
}