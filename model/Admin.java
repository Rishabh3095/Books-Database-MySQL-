package model;
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
	PreparedStatement deleteUser; //delete a user
	PreparedStatement displayUsers; //display all the users
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
			deleteItemPreparedStatement = (PreparedStatement) connection.prepareStatement("Delete from Items where iID in (select uID from users where iID = ?);");
			updateItemPreparedStatement = (PreparedStatement) connection.prepareStatement("Update PriceLowToHigh set Name = ?, Price = ?, Description = ?, Category = ?, Stock = ? where iID = ?;");
			numberOfUsersPreparedStatement = (PreparedStatement) connection.prepareStatement("select Count(*) from users where admin = 0;");
			displayItemsInOrderStatement = (PreparedStatement) connection.prepareStatement("select * from PriceLowToHigh");
			displayUsers = (PreparedStatement) connection.prepareStatement("Select uID, Name, Email from Users where admin <> 1;");
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
			   		case 4: numberOfUsers(); //go to the get number of users function
			   				break;
			   		case 5: deleteUser(); //go to the delete user function
			   				break;
			   		case 6: System.out.println("Logging Out..."); //user logs out
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
	  System.out.println("|4. Total # of Users  ");
	  System.out.println("|5. Remove a User		");
	  System.out.println("|6. Logout			");
	  System.out.println("=======================");
	}
	
	
	/*
	 * Display the categories for the shopping
	 */
	public String getCategory()
	{
		//list of all the options
		String[] categories = {"Clothing and Accessories", "Beauty and Health", "Sports", "Electronics", "Clothing and Accessories"};
		System.out.println("Select an option from the categories:");
		while (true)
		{
			try
			{
				//display the options
				for (int i = 0; i < categories.length; i++)
				{
					System.out.println((i+1) + ". " + categories[i]);
				}
				
				//user can choose one of the options
				Scanner in = new Scanner(System.in);
				int choice = in.nextInt();
				
				//return the option the use chose
				return categories[choice - 1];
			}
			catch(Exception e)
			{
				System.out.println("There was an error choosing the category. Select a valid option");
				continue;
			}
		}
		
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
				category = getCategory();
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
			
			System.out.println("Added Item!");
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
		int id = 0;
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
			
			//get the id number of the item you want to delete
			System.out.println("Please enter the ID of the item to be deleted: ");
			
			String idSrt = in.nextLine();
			
			try
			{
				id = Integer.parseInt(idSrt);
			}
			catch (Exception e)
			{
				System.out.println("Please enter a valid input!");
			}
		

		}
		catch(Exception e)
		{
			System.out.println("There was an error with deleting the item: " + e );
		}
		//go to helper method to delete the item
		adminDeleteItem(id);
		
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
	 * update item in the items table
	 */
	public void updateItem()
	{
		try
		{	
			int id = 0;
			String name = "";
			double price = 0;
			String description = "";
			String category = "";
			int stock = 0;
			
			//get the response from the query
			ResultSet rs = displayItemsInOrderStatement.executeQuery();
			while(rs.next())
			{
				//display all the items with their IDs
				System.out.println("ID #: " + rs.getInt(1) + ", Name: " + rs.getString(2));
			}
			
			//asking for info regarding the item to be deleted
			Scanner ini = new Scanner(System.in);
			
			while (id <= 0)
			{
				System.out.print("Please enter the ID of the item to be updated: ");
				id = ini.nextInt();
				if (id <= 0)
					System.out.println("Enter a valid ID");
			}
			
			while(name.trim().equals(""))
			{
				System.out.print("Please enter the new name of the item: ");
				name = in.nextLine();
				if (name.trim().equals(""))
					System.out.println("Enter a valid name");
			}
			
			
			while (price <= 0)
			{
				System.out.print("Please enter the new price of the item: \n$");
				price = ini.nextDouble();
				if (price <= 0)
					System.out.println("Enter a valid price");
			}
			
			while (description.trim().equals(""))
			{
				System.out.print("Please enter the new description of the item: ");
				description = in.nextLine();
				if (description.trim().equals(""))
					System.out.println("Enter a valid description");
			}
			
			while(category.trim().equals(""))
			{
				category = getCategory();
				if (category.trim().equals(""))
					System.out.println("Enter a valid category");
			}
			
			while (stock <= 0)
			{
				System.out.print("Please enter the new quantity for the item: ");
				stock = ini.nextInt();
				if (stock <= 0)
					System.out.println("Enter a valid quantity number");
			}
			
			//go into the helper method for adding the item
			adminUpdateItem(name, price, description, category, stock, id);
			
			System.out.println("Updated Item!");
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
	public void adminUpdateItem(String name, double price, String description, String category, int stock, int id)
	{
    	
		try
		{
			//insert the information into the query
			updateItemPreparedStatement.setString(1, name);
			updateItemPreparedStatement.setDouble(2, price);
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