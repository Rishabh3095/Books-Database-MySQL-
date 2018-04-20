package model;

public class Users {

	int uID;
	String Name;
	String Email;
	int CreditCard;
	String Address;
	String Password;
	
	public Users(	int uID,
	String Name,
	String Email,
	int CreditCard,
	String Address,
	String Password
){
		this.uID = uID;
		this.Name = Name;
		this.Email = Email;
		this.CreditCard = CreditCard;
		this.Address = Address;
		this.Password = Password;
		
	}

	public int getuID() {
		return uID;
	}

	public void setuID(int uID) {
		this.uID = uID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public int getCreditCard() {
		return CreditCard;
	}

	public void setCreditCard(int creditCard) {
		CreditCard = creditCard;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}
	
}