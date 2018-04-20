package model;

public class Orders {

	int oID;
	String items;
	int uID;
	
	public Orders(int oID, String items, int uID)
	{
		this.oID = oID;
		this.items = items;
		this.uID = uID;
	}

	public int getoID() {
		return oID;
	}

	public void setoID(int oID) {
		this.oID = oID;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public int getuID() {
		return uID;
	}

	public void setuID(int uID) {
		this.uID = uID;
	}
}
