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
	  
	  System.out.print("Please enter the user name: ");
	  String username = in.nextLine();
	  
	  System.out.print("/nPlease enter the password: ");
	  String pass = in.nextLine();
	  
	  
	  //DHRUV STARTS HERE
	  
	  //return True is the user is an admin
	  
	  //return False if they are a normal user
	  isAdmin = false;
	  if (isAdmin == true)
	  {
		return null;  
	  }
	  else
	  {
		//return a Users object  
	  }
	  return null; //remove after implementing returning a Users object
  }
  
  public Users signUp(String name, String email, String cc, String add, String pass)
  {
	  try 
	  {
		  signUpPreparedStatement.setString(1, name);
		  signUpPreparedStatement.setString(2, email);
		  signUpPreparedStatement.setString(3, cc);
		  signUpPreparedStatement.setString(4, add);
		  signUpPreparedStatement.setString(5, pass);
		  signUpPreparedStatement.executeUpdate();
		  ResultSet rs = statement.executeQuery("Select * from users;");// where name = \" " + name + " \" and password = \" " + pass + "\" ;" );
		  
		  while (rs.next())
		  {
			  if (rs.getString(2).equals(name) && rs.getString(6).equals(pass) && rs.getString(3).equals(email))
			  {
				  System.out.println("Found User: " + rs.getString(2));
				  int userID = rs.getInt(1);
				  int creditCard = Integer.parseInt(cc);
				  return new Users(connection, statement, userID, name, email, creditCard, add, pass);   
			  }
		  }
	  } 
	  catch (SQLException e) 
	  {
		// TODO Auto-generated catch block
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
    	System.out.println("Please select a number for the corresponding option or enter q to quit:");
    	app.loginOrSignUp();

    	int selection = 0; //initialize selection to prevent null pointer exception
    	String option = app.in.nextLine().trim();

    	if (option.toLowerCase().equals("q")) 
    	{
    		System.out.println("Thank you!");
    		appRunning = false;
    	}
  
       selection = Integer.parseInt(option); 
       
       	//Login Option
        if (selection == 1) 
        {
        	//Login UI
        	Users user = app.login();
        	
        	if (app.isAdmin == true)
        	{
        		Admin admin = new Admin(app.connection, app.statement);
        		admin.startAdmin();
        	}
        	else
        	{
        		//Display all user stuff
        		user.startUser();
        	}
        }
        else if (selection == 2)
        {
        	//sign up for the user
        	System.out.println("Please Enter Your Name: ");
        	String name = app.in.nextLine().trim();
        	
        	System.out.println("Please Enter Your Email: ");
        	String email = app.in.nextLine().trim();
        	
        	System.out.println("Please Enter Your CreditCard: ");
        	String cc = app.in.nextLine().trim();
     
        	System.out.println("Please Enter Your Address: ");
        	String add = app.in.nextLine().trim();
        	
        	System.out.println("Please Enter Your Password: ");
        	String pass = app.in.nextLine().trim();
        	
			Users user = app.signUp(name, email, cc, add, pass);     
			
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