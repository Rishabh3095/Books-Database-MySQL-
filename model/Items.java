package model;

public class Items {

	int iID;
	String Name;
	int Price;
	String Description;
	String Category;
	int Stock;
	
	public Items(	int iID,
	String Name,
	int Price,
	String Description,
	String Category,
	int Stock) {
		
		this.iID = iID;
		this.Name = Name;
		this.Price = Price;
		this.Description = Description;
		this.Category = Category;
		this.Stock = Stock;
		
	}

	public int getiID() {
		return iID;
	}

	public void setiID(int iID) {
		this.iID = iID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getPrice() {
		return Price;
	}

	public void setPrice(int price) {
		Price = price;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public int getStock() {
		return Stock;
	}

	public void setStock(int stock) {
		Stock = stock;
	}
	
	
}
