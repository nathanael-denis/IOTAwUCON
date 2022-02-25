package iotawucon;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.github.javafaker.Faker;

/**
 * Class which model temperature data
 * for our use case. Temperature data
 * are localised, have a price set
 * by a data broker, and a timestamp,
 * and lose most of their utility otherwise.
 */
public class Temperature {
	/**
	 * The value of the temperature.
	 */
	private float temperatureVal;
	/**
	 * Latitude for GPS position.
	 */
	private float latitude;
	/**
	 * Longitude for GPS positioning.
	 */
	private float longitude;
	/**
	 * Timestamp associated to the data generation.
	 * Uses the java.util.date and not the java.sql.date.
	 */
	private Date genTime;
	/**
	 * Price to pay to get access to the data.
	 * Will be compared to the transaction amount
	 * to grant or deny access.
	 */
	private float iotaPrice;
	Temperature(final float temp, final float lat, final float lon, final Date time, final float price) {
		this.temperatureVal = temp;
		this.latitude = lat;
		this.longitude = lon;
		this.genTime = time;
		this.iotaPrice = price;
	}
	Temperature(final float temp, final float lat, final float lon) {
		this.temperatureVal = temp;
		this.latitude = lat;
		this.longitude = lon;
		Date date = new Date(System.currentTimeMillis());
		this.genTime = date;
		this.iotaPrice = 1f;
	}
	/*
	 * Getters and setters.
	 */
	public Date getGenTime() {
		return genTime;
	}
	public void setGenTime(Date genTime) {
		this.genTime = genTime;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getTemperatureVal() {
		return temperatureVal;
	}
	public void setTemperatureVal(float temperatureVal) {
		this.temperatureVal = temperatureVal;
	}
	public float getIotaPrice() {
		return iotaPrice;
	}
	public void setIotaPrice(float iotaPrice) {
		this.iotaPrice = iotaPrice;
	}
	/**
	 * Generate one instance of the Temperature object.
	 * Used a basis for database feed when repeated.
	 * @return temp a random Temperature instance
	 */
	public static Temperature generateRandTemp() {
		int val = DataUtils.standardRandomInt(-10, 40);
		Temperature temp = new Temperature(val, DataUtils.generateLat(),
				DataUtils.generateLon());
		return temp;
	}
	/**
	 * generate several records of temperature
	 * data for database feeding.
	 * @param nbInstances
	 * @return the set of temperatures
	 */
	public static Set<Temperature> createTempRecords(final int nbInstances) {
		Set<Temperature> setOfTemp = new HashSet<>();
		for (int i = 0; i < nbInstances; i++) {
			Temperature temp = generateRandTemp();
			setOfTemp.add(temp);
	}
		return setOfTemp;
	}


}
