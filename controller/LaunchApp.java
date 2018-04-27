package controller;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import model.*;
import util.ConnectDB;

public class LaunchApp {

	//create all the instance variables
	Connection connection;
	Statement statement;
	Scanner in = new Scanner(System.in);
	PreparedStatement signUpPreparedStatement;
	PreparedStatement loginPreparedStatement1;
	PreparedStatement loginPreparedStatement2;
	boolean isAdmin;

	/*
	 * Create the constructor of the Launch App class
	 */
	public LaunchApp()
	{ 
		try
		{
			//connect to the database
			ConnectDB connect = new ConnectDB();

			//boolean for if the user is an Admin
			isAdmin = false;

			//save the connection
			connection = connect.connection;
			statement = connection.createStatement();

			//selecting database to perform queries on
			statement.executeQuery("USE ONLINERETAILER"); 
			signUpPreparedStatement = connection.prepareStatement("insert into users(Name, Email, CreditCard, Address, Password, admin) values (?, ?, ?, ?, ?, 0)");
			loginPreparedStatement1 = connection.prepareStatement("SELECT * FROM users WHERE Email = ? and Password = ?");


		}
		catch(Exception e)
		{
			System.out.println("Issue connecting with database: " + e);
		}
	}

	/*
	 * Allows the user to login
	 */
	public Users login()
	{
		//create the scanner and the variables used in the method
		Scanner in = new Scanner(System.in);
		String username = "", pass = "";

		//get the input from the user
		while (username.equals("")) 
		{
			System.out.println("Please enter the email: ");
			username = in.nextLine().trim();
			if (username.equals(""))
				System.out.println("cannot be empty ");
		}

		while (pass.equals("")) {
			System.out.println("Please enter the password: ");
			pass = in.nextLine().trim();
			if (pass.equals(""))
				System.out.println("cannot be empty ");
		}

		try
		{
			//put the input into the query and execute it
			loginPreparedStatement1.setString(1, username);
			loginPreparedStatement1.setString(2, pass);

			//get the response from the database
			ResultSet rs = loginPreparedStatement1.executeQuery();
			while(rs.next())
			{	
				//check if the user is an admin
				if(rs.getInt(7) == 1)
				{
					//set admin boolean to true
					isAdmin = true;
					return null;
				}
				else 
				{
					//set admin boolean to false and create a new user object
					isAdmin = false;
					String a = rs.getString(4);
					Long b = Long.parseLong(a);
					return new Users(connection, statement,rs.getInt(1), rs.getString(2), rs.getString(3), b, rs.getString(5), rs.getString(6)); 
				}
			}


		}
		catch(Exception e)
		{
			System.out.println("Error logging in" + e);
		}

		isAdmin = false;
		
		//return null is the user does not exist in the table
		return null; 
	}

	/*
	 * Allows the user to sign up
	 */
	public Users signUp()
	{
		//create the variables that will be used in the method
		long creditCard = 0;
		String name = "",  email = "", cc = "", add = "", pass = "";
		Scanner in  = new Scanner(System.in);

		//get the input from the user
		while(name.equals("")) 
		{
			System.out.println("Please Enter Your Name: ");
			name = in.nextLine().trim();
			if (name.equals(""))
				System.out.println("cannot be empty ");
		}

		while(email.equals("")) 
		{
			System.out.println("Please Enter Your Email: ");
			email = in.nextLine().trim();
			if (email.equals(""))
				System.out.println("cannot be empty ");
		}

		while(cc.equals("") || creditCard == 0) 
		{
			System.out.println("Please Enter Your CreditCard: ");
			cc = in.nextLine().trim();

			try
			{
				creditCard = Long.parseLong(cc);
			}
			catch (Exception e)
			{
				System.out.println("Please enter a valid input!");
			}

			if (cc.equals(""))
				System.out.println("cannot be empty ");
		}

		while(add.equals("")) 
		{
			System.out.println("Please Enter Your Address: ");
			add = in.nextLine().trim();
			if (add.equals(""))
				System.out.println("cannot be empty ");
		}

		while(pass.equals("")) 
		{
			System.out.println("Please Enter Your Password: ");
			pass = in.nextLine().trim();
			if (pass.equals(""))
				System.out.println("cannot be empty ");
		}

		try 
		{
			//put the input into the query and execute it
			signUpPreparedStatement.setString(1, name);
			signUpPreparedStatement.setString(2, email);
			signUpPreparedStatement.setString(3, cc);
			signUpPreparedStatement.setString(4, add);
			signUpPreparedStatement.setString(5, pass);
			signUpPreparedStatement.executeUpdate();

			//get the response from the database for all the users
			ResultSet rs = statement.executeQuery("Select * from users;");

			//search through all the results
			while (rs.next())
			{
				//check you found the user
				if (rs.getString(2).equals(name) && rs.getString(6).equals(pass) && rs.getString(3).equals(email))
				{
					System.out.println("Welcome " + rs.getString(2));
					int userID = rs.getInt(1);
					
					//return the user object
					return new Users(connection, statement, userID, name, email, creditCard, add, pass);   
				}
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();		
		}
		return null;

	}

	/*
	 * display the log in or sign up display
	 */
	public void loginOrSignUp()
	{
		System.out.println("Please select an option from the menu:");
		System.out.println("=======================");
		System.out.println("1. Login");
		System.out.println("2. Sign Up");
		System.out.println("=======================");
	}

	/*
	 * The Main Method that runs all of the code
	 */
	public static void main(String[] args) throws SQLException, InterruptedException, ParseException 
	{
		//create an object of the LaunchApp class
		LaunchApp app = new LaunchApp();
		
		//boolean for loop
		boolean appRunning = true;
		
		while (appRunning) 
		{
			//initialize the scanner object
			app.in  = new Scanner(System.in);
			System.out.println("Please select a number for the corresponding option or enter q to quit:");
			
			//display the log in or sign up option to the user
			app.loginOrSignUp();

			//initialize selection to prevent null pointer exception
			int selection = 0; 
			String option;
			
			//get the input from the user
			option = app.in.nextLine().trim();

			//checks if the user quit the application
			if (option.toLowerCase().equals("q")) 
			{
				System.out.println("Thank you!");
				appRunning = false;
				continue;
			}
			
			//turn the response into an integer
			try 
			{
				selection = Integer.parseInt(option); 
			} 
			catch (Exception e) 
			{
				System.out.println("Wrong Selection");
				continue;
			}

			//Login Option
			if (selection == 1) 
			{
				//logs the user in
				Users user = app.login();
				
				//checks if the user is an admin
				if (app.isAdmin == true)
				{
					//creates an admin object and starts it
					Admin admin = new Admin(app.connection, app.statement);
					admin.startAdmin();
				} 
				else if (user == null) 
				{
					//the user does not exist
					System.out.println("\nUser with those credentials doesn't exist, please try again\n");
					continue;
				} 
				else 
				{
					//start the user class
					user.startUser();
				}

			}
			//Sign up Option
			else if (selection == 2)
			{
				//signs the user up
				Users user = app.signUp();     
				if (user != null)
				{
					//starts the user class
					user.startUser();
				}
			}
			else
			{
				System.out.println("Invalid Option.");
			}
		}

	}
}
