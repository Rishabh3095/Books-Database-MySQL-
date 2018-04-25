package model;

public class Item {

	int iID;
	String Name;
	double Price;
	String Description;
	String Category;
	int Stock;
	
	public Item(int iID, String Name, double Price, String Description, String Category, int Stock) 
	{
		
		this.iID = iID;
		this.Name = Name;
		this.Price = Price;
		this.Description = Description;
		this.Category = Category;
		this.Stock = Stock;
		
	}
	
	
}
