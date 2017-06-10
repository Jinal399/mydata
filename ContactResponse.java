package sample.getdatafromserver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ContactResponse {

	@SerializedName("contacts")
	@Expose
	private List<ContactsItem> contacts;

	public void setContacts(List<ContactsItem> contacts){
		this.contacts = contacts;
	}

	public List<ContactsItem> getContacts(){
		return contacts;
	}

	@Override
 	public String toString(){
		return 
			"ContactResponse{" +
			"contacts = '" + contacts + '\'' + 
			"}";
		}
}