package model;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.*;


public class Admin 
{
	Scanner in; //gets the user's input
	Connection connection; //creates the connection to the database
	PreparedStatement addItemPreparedStatement; //adds an item into the items table
	PreparedStatement deleteItemPreparedStatement; //deletes an item from the items table
	PreparedStatement updateItemPreparedStatement; //updates an item in the items table
	PreparedStatement numberOfUsersPreparedStatement; //gets the number of users 
	PreparedStatement displayItemsInOrderStatement; //displays all the items in order
	PreparedStatement viewAllUserOrders; //displays all the users orders
	PreparedStatement deleteUser;
	PreparedStatement displayUsers;
	Statement statement; //sends the queries to the database
	
	/*
	 * Initializes the Admin Object
	 */
	public Admin(Connection c, Statement s)
	{
		try
		{
			//initialize all the instance variables
			in = new Scanner(System.in);
			connection = c;
			statement = s;
			
			//initialize all the prepared statements with the queries
			addItemPreparedStatement = (PreparedStatement) connection.prepareStatement("INSERT into Items(Name, Price, Description, Category, Stock) values (?, ?, ?, ?, ?);");
			deleteItemPreparedStatement = (PreparedStatement) connection.prepareStatement("Delete from Items where iID = ?;");
			updateItemPreparedStatement = (PreparedStatement) connection.prepareStatement("Update PriceLowToHigh set Name = ?, Price = ?, Description = ?, Category = ?, Stock = ? where iID = ?;");
			viewAllUserOrders = (PreparedStatement) connection.prepareStatement("select items from orders where uID = (select uID from users where uID = ?)");
			numberOfUsersPreparedStatement = (PreparedStatement) connection.prepareStatement("select Count(*) from users where admin = 0;");
			displayItemsInOrderStatement = (PreparedStatement) connection.prepareStatement("select * from PriceLowToHigh");
			displayUsers = (PreparedStatement) connection.prepareStatement("Select uID, Name, Email Users where admin <> 1;");
			deleteUser = (PreparedStatement) connection.prepareStatement("Delete from users where uID = ?;");

		}
		catch (Exception e)
		{
			System.out.println("There was an error creating the Admin: " + e);
		}
	}

	/*
	 * Acts as the main method of the Admin function where everything is run and displayed
	 */
	public void startAdmin()
	{
		//determines whether the admin is logged in
		boolean adminLogged = true;
		
		//continues to display the options while the admin is logged in
		while(adminLogged)
		{
			try
			{
				//displays the options for the admin to choose from
				displayAdminInterface();
				
				//gets the input from the admin
				String choice = in.nextLine().trim();
				
				//the admin's choice
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
			            	
			   	//process the options, dispatch functions based on selected option
			   	switch (adminChoice)
			   	{
			   		case 1: addItem(); //go to the add item function
			   				break;
			   		case 2: deleteItem(); //go to the delete item function
			   				break;
			   		case 3: updateItem(); //go to the update item function
			   				break;
			   		case 4: viewAllOrders(); //go to the view orders function
			   				break;
			   		case 5: numberOfUsers(); //go to the get number of users function
			   				break;
			   		case 6: deleteUser(); //go to the delete user function
			   				break;
			   		case 7: System.out.println("Logging Out..."); //user logs out
			   				adminLogged = false;
			   				break;
			   		default: System.out.println("Not a valid option"); //user gave an invalid option
			   				break;
			   	}
			   
			}
			catch(Exception e)
			{
				System.out.println("There was an error with the Admin: " + e);
			}
		}
	}
	
	/*
	 * display the admin's user interface
	 */
	public void displayAdminInterface()
	{
	  System.out.println("Please select an option from the menu:");
	  System.out.println("=======================");
	  System.out.println("|1. Add Item			");
	  System.out.println("|2. Delete Item		");
	  System.out.println("|3. Update Item		");
	  System.out.println("|4. View All Orders	");
	  System.out.println("|5. Total # of Users  ");
	  System.out.println("|6. Remove a User		");
	  System.out.println("|7. Logout			");
	  System.out.println("=======================");
	}
	
	/*
	 * Gets the number of Users in the Users table
	 */
	public void numberOfUsers() 
	{
		try
		{
			//get the response from the database from the query
			ResultSet rs = numberOfUsersPreparedStatement.executeQuery();
			if (rs.next())
			{
				//display each row
				System.out.println("There are " + rs.getInt(1) + " users who have created an account" );
			}
		}
		catch(Exception e)
		{
			System.out.println("There was an error is getting the number of users");
		}
	}
	
	/*
	 * Add an item into the items table
	 */
	public void addItem()
	{
		try
		{
			String name = "";
			double price = 0;
			String description = "";
			String category = "";
			int stock = 0;
			Scanner in = new Scanner(System.in);
			
			//asking for info regarding the item to be added
			while(name.trim().equals(""))
			{
				System.out.print("Please enter the name of the item: ");
				name = in.nextLine();
				if (name.trim().equals(""))
				{
					System.out.println("Please enter a valid name");
				}
			}

			while(price <= 0)
			{
				System.out.print("Please enter the price of the item: \n$");
				price  = in.nextDouble();
				if (price == 0)
				{
					System.out.println("Please enter a valid price");
				}
			}
			
			in.nextLine();
			
			
			while(description.trim().equals(""))
			{
				System.out.print("Please enter the description of the item: ");
				description = in.nextLine();
				
				if (description.trim().equals(""))
				{
					System.out.println("Please enter a valid description");
				}
			}
			
			while (category.trim().equals(""))
			{
				System.out.print("Please enter the category of the item: ");
				category = in.nextLine();
				if (category.trim().equals(""))
				{
					System.out.println("Please enter a valid category");
				}
			}
				
			while (stock <= 0)
			{
				System.out.print("Please enter the quantity of the item: ");
				stock = in.nextInt();
				if (stock <= 0)
				{
					System.out.println("Please enter a valid number for the stock");
				}
			}
			//helper method for adding the item
			adminAddItem(name, price, description, category, stock);
		}
		catch(Exception e)
		{
			System.out.println("Error in adding an item");
		}
	}
	
	/*
	 * Add item helper method
	 */
	public void adminAddItem(String name, double price, String description, String category, int stock)
	{
	    try 
	    {
	    	//set up the info of the item 
	    	addItemPreparedStatement.setString(1, name);
	    	addItemPreparedStatement.setDouble(2, price);
	    	addItemPreparedStatement.setString(3, description);
	    	addItemPreparedStatement.setString(4, category);
	    	addItemPreparedStatement.setInt(5, stock);
	    	//execute the sql statement to update the database
	    	if (addItemPreparedStatement.execute())
	    		System.out.println(name + " added to the database!");
	    }
	    catch (SQLException e) 
	    {
	      e.printStackTrace();
	    }
	   
	  }
	
	/*
	 * Delete item from the items table
	 */
	public void deleteItem()
	{
		try
		{
			//gets the response for displaying all the items
			ResultSet rs = displayItemsInOrderStatement.executeQuery();
			while(rs.next())
			{
				//Display all the items
				System.out.println("ID #: " + rs.getInt(1) + ", Name: " + rs.getString(2));
			}
			
			//create a new scanner for input
			Scanner ini = new Scanner(System.in);
			
			//get the id number of the item you want to delete
			System.out.println("Please enter the ID of the item to be deleted: ");
			int id = ini.nextInt();
			
			//go to helper method to delete the item
			adminDeleteItem(id);
		}
		catch(Exception e)
		{
			System.out.println("There was an error with deleting the item: " + e );
		}
	}
	
	/*
	 * Delete item helper method
	 */
	public void adminDeleteItem(int id)
	{
		try
		{
	    	//add the item info into the query 
			deleteItemPreparedStatement.setInt(1, id);
			
			//execute the sql statement to update the database
			deleteItemPreparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}

	/*
	 * View all the orders from a user
	 */
	public void viewAllOrders()
	{
		try
		{
			//Create a new scanner object
			Scanner ini = new Scanner(System.in);
			
			//get the information from the user
			System.out.println("Please enter the ID of the user to view his orders: ");
			
			int id = ini.nextInt();
			viewAllUserOrders.setInt(1, id);		
			viewAllUserOrders.executeQuery();
		}
		catch(Exception e)
		{
			System.out.println("There was an error with displaying all items: " + e);
		}
		
	}
		
	/*
	 * update item in the items table
	 */
	public void updateItem()
	{
		try
		{		
			//get the response from the query
			ResultSet rs = displayItemsInOrderStatement.executeQuery();
			while(rs.next())
			{
				//display all the items with their IDs
				System.out.println("ID #: " + rs.getInt(1) + ", Name: " + rs.getString(2));
			}
			
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
			
			//go into the helper method for adding the item
			adminUpdateItem(name, price, description, category, stock, id);
		}
		catch (Exception e)
		{
			System.out.println("There was an error with updating the item");
		}
	}

	/*
	 * Deletes a selected user
	 */
	public void deleteUser()
	{
		try
		{
			//gets all the users returned from the table
			ResultSet rs = displayUsers.executeQuery();
			while(rs.next())
			{
				//display each user along with their ID
				System.out.println("User ID#: " + rs.getInt(1) + ", Name: " + rs.getString(2) + ", Email: " + rs.getString(3));
			}
			
			//get the input from the user
			Scanner ini = new Scanner(System.in);
			
			//asking for info regarding the item to be updated
			System.out.println("Please enter the ID of the user you want to delete: ");
			int id = ini.nextInt();
			
			//insert the information into the query
			deleteUser.setInt(1, id);
			
			//execute the query
			deleteUser.execute();
			System.out.println("User has been deleted\n");
			
		}
		catch(Exception e)
		{
			System.out.println("There was an error with deleting the user: " + e);
		}
	}
	
	/*
	 * Update item helper method
	 */
	public void adminUpdateItem(String name, int price, String description, String category, int stock, int id)
	{
    	
		try
		{
			//insert the information into the query
			updateItemPreparedStatement.setString(1, name);
			updateItemPreparedStatement.setInt(2, price);
			updateItemPreparedStatement.setString(3, description);
			updateItemPreparedStatement.setString(4, category);
			updateItemPreparedStatement.setInt(5, stock);
			updateItemPreparedStatement.setInt(6, id);
			
			//execute the query
			updateItemPreparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}
	
}