package com.POM.POManager.model;

import java.sql.Date;

import jakarta.persistence.*;
//import org.hibernate.dialect.SQLServer2016Dialect;

@Entity
@Table(name = "POManager")
public class POmodel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (name = "number")
	private String POnumber;
	
	
	@Column(name = "vendor")
	private String POvendor;
	
	@Column(name = "total")
	private String POtotal;
	
//	@Column(name = "date")
//	private Date POdate;
	
	public POmodel() {
		
	}
	
	public POmodel(String POorder, String POvendor, String POtotal) {
		this.POnumber = POorder;
		this.POvendor = POvendor;
		this.POtotal = POtotal;
//		this.POdate = POdate;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getPOorder() {
		return POnumber;
	}

	public void setPOorder(String POorder) {
		this.POnumber = POnumber;
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
	    return "POHeader [id=" + id + ", number=" + POnumber +  " , total=" + POtotal + ", vendor=" + POvendor +"]";
	  }
}
