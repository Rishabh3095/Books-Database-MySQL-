package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Users {
	// The user's data
	int uID;
	String Name;
	String Email;
	long CreditCard;
	String Address;
	String Password;
	
	//Scanner used to read the user's input
	Scanner in;
	
	//All the prepared statements used to query the database
	PreparedStatement viewOrdersPreparedStatement;
	PreparedStatement createOrderPreparedStatement;
	PreparedStatement decrementStockPreparedStatement;
	PreparedStatement searchByCategory;
	PreparedStatement searchAllItems;
        PreparedStatement searchSpecificItem;
	
	//The items the user has selected to purchase
	List<Item> cart;
	
	/**
	*Constructor to set up the user
	*
	*
	*@param c The connection to the database
	*@param s The statement used to modify and access the database
	*@param uID The user's identification number
	*@param Name The name of the user
	*@param Email The email address of the user
	*@param CreditCard The credit card number of the user
	*@param Address The address of the user
	*@param Password The password the user uses to access the application
	**/
	public Users(Connection c, Statement s, int uID, String Name, String Email, long CreditCard, String Address, String Password)
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
			
			//All prepared statements
			viewOrdersPreparedStatement = (PreparedStatement) c.prepareStatement("CALL getItemsOrdered(?);"); //Used to view the orders
			createOrderPreparedStatement = (PreparedStatement) c.prepareStatement("Insert into orders(items,uID) values (?,?);"); //Used to create the order
			decrementStockPreparedStatement = (PreparedStatement) c.prepareStatement("CALL decrementStock(?);"); //Used to remove an item from the stock
			searchByCategory = (PreparedStatement) c.prepareStatement("Select * from items where Category = ? and stock <> 0 Order by Price asc;"); //Used to select items based on the category
			searchAllItems = (PreparedStatement) c.prepareStatement("Select * from items where stock <> 0 Order by Price asc;"); //Used to select all available items
                        searchSpecificItem = (PreparedStatement) c.prepareStatement("CALL getItem(?);"); //Used to find a specific item based on the item ID
		}
		catch(Exception e)
		{
			System.out.println("There was an error creating the User");
		}

	}
	
	/**
	*Prints out the main user interface
	*/
	public void displayUserInterface()
	{
		System.out.println();
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
	/**
	*Prints out the search interface
	*/
	public void displaySearchInterface()
	{
		System.out.println();
		System.out.println("Please select an option from the menu:");
		System.out.println("=======================");
		System.out.println("|1. Search by Category	|");
		System.out.println("|2. Search All Items		|");
		System.out.println("|3. Go Back				|");
		System.out.println("=======================");
	}
	
	/**
	*Prints out the categories selection interface
	*/
	public void displayCategories()
	{
		System.out.println();
		System.out.println("Please select which category to view items from:");
		System.out.println("=======================");
		System.out.println("|1. Beauty and Health			|");
		System.out.println("|2. Clothing and Accessories	|");
		System.out.println("|3. Electtronics				|");
		System.out.println("|4. Sports					|");
		System.out.println("|5. Go Back to Search Options |");
		System.out.println("=======================");
	}
	
	/**
	 * Displays items for users to select from
	 * Helper method for searchForItems
	 *
	 * @param items - list of items to be displayed
	 */
	public void displayItems(ArrayList<Item> items) {
		int itemIndex = 1; //Item index used to select the item to add to cart
		System.out.println("Products:\n");
		
		//Loops through the items the user asked to display (either by category or every item)
		for(Item item : items) {
			System.out.print(itemIndex + ": ");
			System.out.println("Name: " + item.Name + ", Price: $" + item.Price + "\n\tDescription: " + item.Description + "\n");
			itemIndex++;
		}
		
		System.out.println(itemIndex + ": Go back to search menu\n"); //Final index is used to go back to previous menu
		System.out.println("\nPlease select an item to add to your cart:\n");
	}
	
	/**
	*Starts up the user after the user logs in or creates a new account.
	*/
	public void startUser()
	{
		try
		{
			boolean runningUser = true; //Makes sure the menu stays up until the user logs out
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
					case 1: viewOrders(); //View the orders
							break;
					case 2: searchForItems(); //Search for items
							break;
					case 3: viewCart();	//View current cart
							break;
					case 4: checkout(); //Checkout item selection
							break;
					case 5:	removeItemFromCart(); //Remove an item from the cart
							break;
					case 6:	System.out.println("Logging Out..."); //Will log the user out and return to login menu
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
	/**
	*Allows the user to view the orders previously made.
	*/
	public void viewOrders()
	{
		try
		{
			//Uses the prepared statement to bring back the user's orders based on their user ID.
			viewOrdersPreparedStatement.setInt(1, uID); 
			ResultSet rs = viewOrdersPreparedStatement.executeQuery();

			int orderNumber = 0; //Used to present the orders
			boolean firstOrder = false; //Set to false to see if users
                        
			while(rs.next())
			{
			double orderTotal = 0.00;	
                        if (!firstOrder)
				{
					System.out.println("Previous orders " + rs.getString(2) +  " purchased: ");
					firstOrder = true;
				}
				orderNumber++;
                                System.out.println("Order Number " + orderNumber + ": ");
                                String[] items = rs.getString(1).split(",");
                                for (int i = 0; i < items.length; i++){
                                    int itemID = Integer.parseInt(items[i]);
                                    searchSpecificItem.setInt(1, itemID);
                                    ResultSet itemRS = searchSpecificItem.executeQuery();
                                    itemRS.next();
                                    System.out.println("\t" + Integer.toString(i+1) + ". " + itemRS.getString(2) + ": $" + itemRS.getDouble(3));
                                    orderTotal += itemRS.getDouble(3);
                                }
                                System.out.println("\tTOTAL: $" + Math.round(orderTotal*100.0)/100.0);
				
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

	/**
	 * Allows user to search items by category or items
	 */
	public void searchForItems()
	{
		//ZAHRA STARTS HERE
		try
		{
			boolean runningSearch = true;
			while (runningSearch)
			{
				displaySearchInterface();
				String choice = in.nextLine().trim();
				int userChoice;

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
					case 1: searchByCategory();
							break;
					case 2: searchAllItems();
							break;
					case 3:	System.out.println("Back to main menu...");
							runningSearch = false;
							break;
					default:System.out.println("Not a valid option");
							break;				
				}					
			}

		}
		catch(Exception e)
		{
			System.out.println("Error in searching for items: " + e);
		}
	}

	/**
	 * Allows user to view items from specific category
	 * Helper method for searchForItems
	 */
	public void searchByCategory()
	{
		try {
			boolean runningSearch = true;
			String category = "";
			boolean continueSearch = false;

			while (runningSearch)
			{
				continueSearch = false;
				displayCategories();
				String choice = in.nextLine().trim();
				int userChoice;

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
					case 1: category = "Beauty and Health"; 
							continueSearch = true;
							break;
					case 2: category = "Clothing and Accessories"; 
							continueSearch = true;
							break;
					case 3: category = "Electronics"; 
							continueSearch = true;
							break;
					case 4: category = "Sports"; 
							continueSearch = true;
							break;
					case 5:	System.out.println("Back to search options...");	// need to implement
							runningSearch = false;
							break;
					default:System.out.println("Not a valid option");
							break;							
				}
				
				if(continueSearch) {
					searchByCategory.setString(1, category);
					ResultSet rs = searchByCategory.executeQuery();
					selectItem(rs);
				}

			}

		} catch (Exception e) {
			System.out.println("Error searching by category " + e);
		}
	}
	
	
	/**
	 * Allows user to view all items
	 * Helper method for searchForItems
	 */
	public void searchAllItems()
	{
		try {
			ResultSet rs;
			rs = searchAllItems.executeQuery();
			selectItem(rs);
		} catch (Exception e) {
			System.out.println("Error searching all items " + e);
		}
	}
	
	
	/**
	 * Implements selection of item to add to cart
	 * Helper method for searchForItems
	 *
	 * @param rs Result Set of items that user can select from
	 */
	public void selectItem(ResultSet rs)
	{
		try
		{
			ArrayList<Item> items = new ArrayList<Item>();
			
			while(rs.next())
			{
				Item newItem = new Item(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5), rs.getInt(6));
				items.add(newItem);
			}

			int goBackOption = items.size() + 1;
			boolean runningSearch = true;

			while (runningSearch)
			{
				displayItems(items);
				String choice = in.nextLine().trim();
				int userChoice;

				try 
				{
					userChoice = Integer.parseInt(choice);
				} 
				catch (NumberFormatException e) 
				{
					System.out.println("Invalid option! Please select a valid option!");
					continue;
				}
				
				if(userChoice == goBackOption)
					runningSearch = false;
				else
				{
					Item addToCart = items.get(userChoice - 1);
					int cartCount = 0;
					
					for(Item cartItem : cart) {
						if(cartItem.Name == addToCart.Name)
							cartCount++;
					}
					
					if(addToCart.Stock > cartCount) {
						cart.add(addToCart);
						System.out.println("This item has been added to your cart.\n\n");
					}
					else
						System.out.println("This item is out of stock. Please select a different item. ");
						
				}

			}

		} catch (Exception e) {
			System.out.println("Error selecting item " + e);
		}

	}
	
	
	public void viewCart()
	{
                double cartTotal = 0.0;
		for (int i = 0; i < cart.size(); i++)
		{
			Item currentItem = cart.get(i);
			System.out.println("Name: " + currentItem.Name + ", Price: $" + currentItem.Price);
                        cartTotal += currentItem.Price;
		}
                System.out.println("TOTAL: $" + Math.round(cartTotal*100.0)/100.0);
	}

	public void checkout()
	{
		try
		{
			if (cart.isEmpty())
				System.out.println("Cart is Empty");
			else{
				String items = ""; //A Comma-seperated value of the item id numbers in the cart
				for (int i = 0; i < cart.size(); i++){
					if (cart.get(i).Stock > 0){
						items += Integer.toString(cart.get(i).iID);
						decrementStockPreparedStatement.setInt(1,cart.get(i).iID);
						decrementStockPreparedStatement.execute();
						if (i < cart.size()-1)
							items += ",";
					}
				}
				createOrderPreparedStatement.setString(1, items);
				createOrderPreparedStatement.setInt(2, uID);
				createOrderPreparedStatement.executeUpdate();
				cart.clear();
				System.out.println("Order had been placed!");
			}
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
