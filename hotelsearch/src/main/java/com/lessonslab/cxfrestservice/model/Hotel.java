package com.lessonslab.cxfrestservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="hotel")
public class Hotel {
	
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	private String hotelName;
	private String location;

}
