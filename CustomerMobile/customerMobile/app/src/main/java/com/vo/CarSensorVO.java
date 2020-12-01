package com.vo;

import java.util.Date;

public class CarSensorVO {
	private int carid;
	private int heartbeat;
	private String pirfront;
	private String pirrear;
	private int freight;
	private int fuel;
	private int fuelmax;
	private int temper;
	private String starting;
	private String moving;
	private Date movingstarttime;
	private String aircon;
	private String crash;
	private String door;
	private double lat;
	private double lng;
	
	public CarSensorVO() {
		super();
	}

	public CarSensorVO(int carid, String pirfront, String pirrear, int freight, int temper, String crash) {
		super();
		this.carid = carid;
		this.pirfront = pirfront;
		this.pirrear = pirrear;
		this.freight = freight;
		this.temper = temper;
		this.crash = crash;
	}

	public CarSensorVO(int carid, String pirfront, String pirrear, int temper, String starting, String moving, String aircon,
			String door) {
		super();
		this.carid = carid;
		this.pirfront = pirfront;
		this.pirrear = pirrear;
		this.temper = temper;
		this.starting = starting;
		this.moving = moving;
		this.aircon = aircon;
		this.door = door;
	}

	public CarSensorVO(int carid, int heartbeat, int fuel, int fuelmax, String starting, String moving, String door) {
		super();
		this.carid = carid;
		this.heartbeat = heartbeat;
		this.fuel = fuel;
		this.fuelmax = fuelmax;
		this.starting = starting;
		this.moving = moving;
		this.door = door;
	}

	public CarSensorVO(int carid, int heartbeat, String moving) {
		super();
		this.carid = carid;
		this.heartbeat = heartbeat;
		this.moving = moving;
	}

	public CarSensorVO(int carid, String pirfront, String pirrear, String starting) {
		super();
		this.carid = carid;
		this.pirfront = pirfront;
		this.pirrear = pirrear;
		this.starting = starting;
	}

	public CarSensorVO(int carid, int freight, String moving, Date movingstarttime) {
		super();
		this.carid = carid;
		this.freight = freight;
		this.moving = moving;
		this.movingstarttime = movingstarttime;
	}

	public CarSensorVO(int carid, String crash) {
		super();
		this.carid = carid;
		this.crash = crash;
	}

	public CarSensorVO(int carid, int heartbeat, String pirfront, String pirrear, int freight, int fuel, int fuelmax,
			int temper, String starting, String moving, Date movingstarttime, String aircon, String crash, String door,
			double lat, double lng) {
		super();
		this.carid = carid;
		this.heartbeat = heartbeat;
		this.pirfront = pirfront;
		this.pirrear = pirrear;
		this.freight = freight;
		this.fuel = fuel;
		this.fuelmax = fuelmax;
		this.temper = temper;
		this.starting = starting;
		this.moving = moving;
		this.movingstarttime = movingstarttime;
		this.aircon = aircon;
		this.crash = crash;
		this.door = door;
		this.lat = lat;
		this.lng = lng;
	}

	public int getCarid() {
		return carid;
	}

	public void setCarid(int carid) {
		this.carid = carid;
	}

	public int getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(int heartbeat) {
		this.heartbeat = heartbeat;
	}

	public String getPirfront() {
		return pirfront;
	}

	public void setPirfront(String pirfront) {
		this.pirfront = pirfront;
	}

	public String getPirrear() {
		return pirrear;
	}

	public void setPirrear(String pirrear) {
		this.pirrear = pirrear;
	}

	public int getFreight() {
		return freight;
	}

	public void setFreight(int freight) {
		this.freight = freight;
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public int getFuelmax() {
		return fuelmax;
	}

	public void setFuelmax(int fuelmax) {
		this.fuelmax = fuelmax;
	}

	public int getTemper() {
		return temper;
	}

	public void setTemper(int temper) {
		this.temper = temper;
	}

	public String getStarting() {
		return starting;
	}

	public void setStarting(String starting) {
		this.starting = starting;
	}

	public String getMoving() {
		return moving;
	}

	public void setMoving(String moving) {
		this.moving = moving;
	}

	public Date getMovingstarttime() {
		return movingstarttime;
	}

	public void setMovingstarttime(Date movingstarttime) {
		this.movingstarttime = movingstarttime;
	}

	public String getAircon() {
		return aircon;
	}

	public void setAircon(String aircon) {
		this.aircon = aircon;
	}

	public String getCrash() {
		return crash;
	}

	public void setCrash(String crash) {
		this.crash = crash;
	}

	public String getDoor() {
		return door;
	}

	public void setDoor(String door) {
		this.door = door;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "CarSensorVO [carid=" + carid + ", heartbeat=" + heartbeat + ", pirfront=" + pirfront + ", pirrear="
				+ pirrear + ", freight=" + freight + ", fuel=" + fuel + ", fuelmax=" + fuelmax + ", temper=" + temper
				+ ", starting=" + starting + ", moving=" + moving + ", movingstarttime=" + movingstarttime + ", aircon="
				+ aircon + ", crash=" + crash + ", door=" + door + ", lat=" + lat + ", lng=" + lng + "]";
	}
	
	
}
