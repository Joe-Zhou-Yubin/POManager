package com.POM.POManager.model;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "Manager")
public class POmodel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column (name = "order")
	private String POorder;
	
	
	@Column(name = "vendor")
	private String POvendor;
	
	@Column(name = "total")
	private String POtotal;
	
//	@Column(name = "date")
//	private Date POdate;
	
	public POmodel() {
		
	}
	
	public POmodel(String POorder, String POvendor, String POtotal) {
		this.POorder = POorder;
		this.POvendor = POvendor;
		this.POtotal = POtotal;
//		this.POdate = POdate;
	}
	
	public long getId() {
		return id;
	}

	public String getPOorder() {
		return POorder;
	}

	public void setPOorder(String POorder) {
		this.POorder = POorder;
	}

	public String getPOvendor() {
		return POvendor;
	}

	public void setPOvendor(String POvendor) {
		this.POvendor = POvendor;
	}

	public String getPOtotal() {
		return POtotal;
	}

	public void setPOtotal(String POtotal) {
		this.POtotal = POtotal;
	}

//	public Date getPOdate() {
//		return POdate;
//	}
//
//	public void setPOdate(Date POdate) {
//		this.POdate = POdate;
//	}
	
	
	@Override
	public String toString() {
		return "Manager [id=\" + id + \", order=\" + POorder + \", vendor=\" + POvendor + \", total=\" + POtotal + \"]";
	}
	
}
