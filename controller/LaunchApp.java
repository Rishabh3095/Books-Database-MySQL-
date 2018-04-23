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

  static Scanner in = new Scanner(System.in);
  static Connection connection = null;
  private static PreparedStatement preparedStatement = null;
  private static Statement statement = null;

  public LaunchApp()
  {
	  
  }
  
  public static void viewOrders()
  {
	  
  }
  
  public static void searchItem()
  {
	  
  }
  
  public static void viewCart()
  {
	  
  }
  
  public static void checkout()
  {
	  
  }
  
  public static void login()
  {
	  Scanner in = new Scanner(System.in);
	  
	  System.out.print("Please enter the user name: ");
	  String username = in.nextLine();
	  
	  System.out.print("/nPlease enter the password: ");
	  String pass = in.nextLine();
	  
//	  preparedStatement.exe
	 
  }

  public static void createUser()
  {
	  
  }
  
  public static void userLogOut()
  {
	  
  }

  public static void adminLogout()
  {
	  
  }
  
  public static void adminAddItem()
  {
	  
  }

  public static void adminDeleteItem()
  {
	  
  }
  
  public static void adminUpdateItem()
  {
	  
  }

  

  public static void addItem() throws SQLException {
	  
	  statement.execute("insert into Orders(oID, items, uID) values (1000, \"check\", 5);");
	  
  }
  
  public static void loginOrSignUp(){
	  System.out.println("Please select an option from the menu:");
	  System.out.println("=======================");
      System.out.println("|1. Login|");
      System.out.println("|2. Sign Up|");
      System.out.println("=======================");
  }

//  public static void loginOrSignUp(){
//	  System.out.println("Please select an option from the menu:");
//	  System.out.println("=======================");
//      System.out.println("|1. Login|");
//      System.out.println("|2. Sign Up|");
//      System.out.println("=======================");
//  }
  
  public static boolean isAdmin()
  {
	  
	  return false;
  }
  
  public static void displayAdminInterface(){
	  System.out.println("Please select an option from the menu:");
	  System.out.println("=======================");
      System.out.println("|1. Add Item|");
      System.out.println("|2. Delete Item|");
      System.out.println("|3. Update Item|");
      System.out.println("|4. Logout|");
      System.out.println("=======================");
  }
    
  
  
  public static void displayUserInterface(){
	  System.out.println("Please select an option from the menu:");
	  System.out.println("=======================");
      System.out.println("|1. View Orders|");
      System.out.println("|2. Search for Item|");
      System.out.println("|3. View Cart|");
      System.out.println("|4. Checkout|");
      System.out.println("|5. Remove Item from List|");
      System.out.println("|6. Logout|");
      System.out.println("=======================");
  }
  

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  

  public static void main(String[] args) throws SQLException, InterruptedException, ParseException {
    ConnectDB connect = new ConnectDB();
    // open a connection
    connection = connect.connection;
    statement = connection.createStatement();
    
    //selecting database to perform queries on
    statement.executeQuery("USE ONLINERETAILER");      

    System.out.println("Please select a number for the corresponding option or enter q to quit:");

    
    while (true) {

    loginOrSignUp();
    
    
      int selection = 0; //initialize selection to prevent null pointer exception
      String option = in.nextLine().trim();

      if (option.equals("q") || option.equals("q")) {
        System.out.println("Thank you!");
        break;
      }
      
      try {
        selection = Integer.parseInt(option);
      } catch (NumberFormatException e) {
        System.out.println("Invalid option! Please select a valid option!");
        loginOrSignUp();
        continue;
      }

      
      
      // dispatch request based on selection
      if (selection != 0) {
        if (selection == 1) {
        		if(isAdmin())
        		{
        			boolean adminLogged = true;
        			   int adminChoice = 0;
       	            while(adminLogged){
            			displayAdminInterface();
       	            	String choice = in.nextLine().trim();
       	            	//parsing use input //handling invalid input format exception
       	            	try {
       	                    adminChoice = Integer.parseInt(choice);
       	                  } catch (NumberFormatException e) {
       	                    System.out.println("Invalid option! Please select a valid option!");
       	                    continue;
       	                  }
       	            	
       	            	//process the options = dispatch functions based on selected option
       	            	
       	            	if(adminChoice == 1){
       	            		
       	            	}else if(adminChoice == 2){
       	            		
       	            	}else if(adminChoice == 3){
       	            		       	            			
       	            	}else if(adminChoice == 4){
       	            		adminLogged = false;
       	            	}else{
       	            		System.out.println("Not a valid option");
       	            	}
        		}
        	}
        		else
        		{
            		System.out.println("Invalid username or password. Please try again!");        			
        		}
        }else if (selection == 2) {
    		if(!isAdmin())
    		{
    			boolean userLogged = true;
    			   int userChoice = 0;
   	            while(userLogged){
        			displayAdminInterface();
   	            	String choice = in.nextLine().trim();
   	            	//parsing use input //handling invalid input format exception
   	            	try {
   	                    userChoice = Integer.parseInt(choice);
   	                  } catch (NumberFormatException e) {
   	                    System.out.println("Invalid option! Please select a valid option!");
   	                    continue;
   	                  }
   	            	
   	            	//process the options = dispatch functions based on selected option
   	            	
   	            	if(userChoice == 1){
   	            		
   	            	}else if(userChoice == 2){
   	            		
   	            	}else if(userChoice == 3){
   	            		
   	            	}else if(userChoice == 4){
   	            		
   	            	}else if(userChoice == 5){
   	            		
   	            	}else if(userChoice == 6){
   	            		userLogged = false;
   	            	}else{
   	            		System.out.println("Not a valid option");
   	            	}
    		}
    	}
    		else
    		{
        		System.out.println("Invalid username or password. Please try again!");        			
    		}
        }  else {
          System.out.println("Invalid option! Please choose a valid option from the menu.");
        }
      
        System.out.println(
            "Please select a number for the corresponding option or enter q to quit:");
        //logs out ad comes to this
        loginOrSignUp();
    }
    in.close();
    connect.closeConnection();
  }
}
}