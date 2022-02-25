package iotawucon;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Class which models humidity levels
 * for our use case. Humidity is a
 * measure of the amount of water
 * vapor in the air, in relation
 * with the maximum of water vapor.
 * The higher the temperature, the
 * more water vapor the air can hold.
 * Humidity is therefore express as
 * a percentage, under 20% and above 70%
 * being troublesome.
 */
public class HumidityLevel {
	/**
	 * percentage corresponding to
	 * humidity levels.
	 */
	private int humidityLevel;
	/**
	 * Latitude for GPS positioning.
	 */
	private float latitude;
	/**
	 * Longitude for GPS positioning.
	 */
	private float longitude;
	/**
	 * Timestamp associated to the data generation.
	 * Use the java.util.date and not the java.sql.date.
	 */
	private Date genTime;
	/**
	 * Price to pay to get access to the data.
	 * Will be compared to the transaction amount
	 * to grant or deny access.
	 */
	private float iotaPrice;
	HumidityLevel(final int humLev, final float lat, final float lon, final Date time, final float price) {
		this.humidityLevel = humLev;
		this.latitude = lat;
		this.longitude = lon;
		this.genTime = time;
		this.iotaPrice = price;
	}
	HumidityLevel(final int humLev, final float lat, final float lon) {
		this.humidityLevel = humLev;
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
	public int getHumidityLevel() {
		return humidityLevel;
	}
	public void setHumidityLevel(int humidityLevel) {
		this.humidityLevel = humidityLevel;
	}
	public float getIotaPrice() {
		return iotaPrice;
	}
	public void setIotaPrice(float iotaPrice) {
		this.iotaPrice = iotaPrice;
	}
	/**
	 * generate a humidity record considering
	 * very low and very high level are unlikely events.
	 * @return humLev an instance of humidity level
	 */
	public static HumidityLevel generateRandHumLev() {
		int val = DataUtils.standardRandomInt(20, 70);
		HumidityLevel humLev = new HumidityLevel(val, DataUtils.generateLat(),
				DataUtils.generateLon());
		return humLev;
	}
	/**
	 * generate several records of humidity level data
	 * for database feeding.
	 * @param nbInstances
	 * @return the set of humidity levels
	 */
	public static Set<HumidityLevel> createHumLevRecords(final int nbInstances) {
		Set<HumidityLevel> setOfHumLev = new HashSet<>();
		for (int i = 0; i < nbInstances; i++) {
			HumidityLevel temp = generateRandHumLev();
			setOfHumLev.add(temp);
	}
		return setOfHumLev;
	}

}
