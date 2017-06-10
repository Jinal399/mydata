package sample.getdatafromserver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Phone{

	@SerializedName("mobile")
	@Expose
	private String mobile;

	@SerializedName("office")
	@Expose
	private String office;

	@SerializedName("home")
	@Expose
	private String home;

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setOffice(String office){
		this.office = office;
	}

	public String getOffice(){
		return office;
	}

	public void setHome(String home){
		this.home = home;
	}

	public String getHome(){
		return home;
	}

	@Override
 	public String toString(){
		return 
			"Phone{" + 
			"mobile = '" + mobile + '\'' + 
			",office = '" + office + '\'' + 
			",home = '" + home + '\'' + 
			"}";
		}
}