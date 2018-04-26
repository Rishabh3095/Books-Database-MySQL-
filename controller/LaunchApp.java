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

	Connection connection;
	Statement statement;
	Scanner in = new Scanner(System.in);
	PreparedStatement signUpPreparedStatement;
	PreparedStatement loginPreparedStatement1;
	PreparedStatement loginPreparedStatement2;
	boolean isAdmin;
	
	public LaunchApp()
	{ 
		try
		{
			ConnectDB connect = new ConnectDB();
			// open a connection
			connection = connect.connection;
			statement = connection.createStatement();
			//selecting database to perform queries on
			statement.executeQuery("USE ONLINERETAILER"); 
			signUpPreparedStatement = connection.prepareStatement("insert into users(Name, Email, CreditCard, Address, Password, admin) values (?, ?, ?, ?, ?, 0)");
			loginPreparedStatement1 = connection.prepareStatement("SELECT * FROM users WHERE Email = ? and Password = ?");
			

			isAdmin = false;
		}
		catch(Exception e)
		{
			System.out.println("Issue connecting with database: " + e);
		}
	}
  
    
  public Users login()
  {
	  Scanner in = new Scanner(System.in);
	  String username = "", pass = "";
	  
	  while (username.equals("")) {
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
	  
	  //DHRUV STARTS HERE
	  try
	  {
		  loginPreparedStatement1.setString(1, username);
		  loginPreparedStatement1.setString(2, pass);
		  

		  ResultSet rs = loginPreparedStatement1.executeQuery();
		  
		  while(rs.next())
		  {	
			  if(rs.getInt(7) == 1)
			  {
				  isAdmin = true;
				  return null;
			  }
			  else 
			  {
				  String a = rs.getString(4);
				  Long b = Long.parseLong(a);
				  System.out.println(b);
				  return new Users(connection, statement,rs.getInt(1), rs.getString(2), rs.getString(3), b, rs.getString(5), rs.getString(6)); 
			  }
		  }

		  
	  }
	  catch(Exception e)
	  {
		  System.out.println("Error logging in" + e);
	  }
	  //return True is the user is an admin
	  
	  //return False if they are a normal user
	 
	  return null; //remove after implementing returning a Users object
  }
  
  public Users signUp()
  {
	  String name = "", email = "", cc = "", add = "", pass = "";
	  Scanner in  = new Scanner(System.in);
	  
	  //sign up for the user
	  while(name.equals("")) {
		  System.out.println("Please Enter Your Name: ");
	  	  name = in.nextLine().trim();
	  	  if (name.equals(""))
	  		  System.out.println("cannot be empty ");
	  }
		  
	  while(email.equals("")) {
		  System.out.println("Please Enter Your Email: ");
		  email = in.nextLine().trim();
		  if (email.equals(""))
		  	  System.out.println("cannot be empty ");
	  }
	  
	  while(cc.equals("")) {
		  System.out.println("Please Enter Your CreditCard: ");
		  cc = in.nextLine().trim();
		  if (cc.equals(""))
		  	  System.out.println("cannot be empty ");
	  }

	  while(add.equals("")) {
		  System.out.println("Please Enter Your Address: ");
		  add = in.nextLine().trim();
		  if (add.equals(""))
		  	  System.out.println("cannot be empty ");
	  }
		
	  while(pass.equals("")) {
		  System.out.println("Please Enter Your Password: ");
		  pass = in.nextLine().trim();
		  if (pass.equals(""))
		      System.out.println("cannot be empty ");
	  }
	  
	  try 
	  {
		  signUpPreparedStatement.setString(1, name);
		  signUpPreparedStatement.setString(2, email);
		  signUpPreparedStatement.setString(3, cc);
		  signUpPreparedStatement.setString(4, add);
		  signUpPreparedStatement.setString(5, pass);
		  signUpPreparedStatement.executeUpdate();
		  ResultSet rs = statement.executeQuery("Select * from users;");// where name = \" " + name + " \" and password = \" " + pass + "\" ;" );
		  System.out.println(rs);
		  
		  while (rs.next())
		  {
			  System.out.println(rs);

			  if (rs.getString(2).equals(name) && rs.getString(6).equals(pass) && rs.getString(3).equals(email))
			  {
				  System.out.println("Found User: " + rs.getString(2));
				  int userID = rs.getInt(1);
				  long creditCard = Long.parseLong(cc);
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
    
  public void loginOrSignUp(){
	  System.out.println("Please select an option from the menu:");
	  System.out.println("=======================");
      System.out.println("|1. Login|");
      System.out.println("|2. Sign Up|");
      System.out.println("=======================");
  }

  
  
  
 

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  

  public static void main(String[] args) throws SQLException, InterruptedException, ParseException 
  {
    
    LaunchApp app = new LaunchApp();
    
    boolean appRunning = true;
   
    
    while (appRunning) 
    {
		app.in  = new Scanner(System.in);
		System.out.println("Please select a number for the corresponding option or enter q to quit:");
    	app.loginOrSignUp();

    	int selection = 0; //initialize selection to prevent null pointer exception
		String option;
    	
    	option = app.in.nextLine().trim();

    	if (option.toLowerCase().equals("q")) 
    	{
    		System.out.println("Thank you!");
    		appRunning = false;
    		continue;
    	}
    	
    	try {
    		selection = Integer.parseInt(option); 
    	} catch (Exception e) {
    		System.out.println("Wrong Selection");
    		continue;
    	}
       
       	//Login Option
        if (selection == 1) 
        {
        	//Login UI
        	Users user = app.login();
        	
        	
        	if (app.isAdmin == true)
        	{
        		Admin admin = new Admin(app.connection, app.statement);
        		admin.startAdmin();
        	} else if (user == null) {
        		System.out.println("\nUser with those credentials doesn't exist, please try again\n");
        		continue;
        	} else {
        		user.startUser();
        	}
        	
        	
        }
        else if (selection == 2)
        {
        	Users user = app.signUp();     
			
			if (user != null)
			{
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
