package iotawucon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import com.github.javafaker.Faker;

/**
 * The purpose of this class is to generate data to feed the databases, either
 * centralised or decentralised. Based on Java Faker.
 *
 */
public class DataUtils {
	static String URL= "/PATH_TO_FILE/IOTAwUCON/src/resource/fr.csv";
	/**
	 * generate random users. Set type is used to store data,
	 * might be a problem if two random users are the same.
	 * @param userQuantity how many users to be generated
	 * @return the faker created
	 */
	public static Set<Faker> createFakers(final int userQuantity) {
		Set<Faker> setOfFakers = new HashSet<>();
		for (int i = 0; i < userQuantity; i++) {
			Faker faker = new Faker();
			setOfFakers.add(faker);
	}
		return setOfFakers;
	}
	/**
	 * function to print all the fakers,
	 * basically used after generation to debug or
	 * after import from databases.
	 * @param setOfFakers
	 */
	public static void showAllFakers(final Set<Faker> setOfFakers) {
		setOfFakers.forEach(System.out::println);
	}
	/**
	 * generate a random int in the (min,max) range
	 * the standard way (after Java 1.7).
	 * @param min the lower bound of int generated
	 * @param max the upper bound of int generated
	 * @return the int created
	 */
	public static int standardRandomInt(final int min, final int max) {
		int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
		return randomNum;
	}
	/**
	 * generate a random character [A-Z].
	 * @return the generated character
	 */
	public static char generateRandomChar() {
		final int rot = 26;
		Random r = new Random();
		char c = (char) (r.nextInt(rot) + 'a');
		return c;
	}
	/**
	 * generate a string of a given size.
	 * Used to generate more realisitc car models.
	 * @param size
	 * @return the string generated.
	 */
	public static String generateString(final int size) {
		String generatedString = "";
		for (int i = 0; i < size; i++) {
			generatedString += DataUtils.generateRandomChar();
		}
		return generatedString;
	}
	/**
	 * take a csv file and parse it before appending
	 * data to a set. This is used to generate properly random
	 * car names.
	 * @param file the csv file from which the data will be extracted.
	 * @return a set of data derived from the csv.
	 * @throws FileNotFoundException if the scanner can't open the file.
	 */
	public static List<String> csvToList(final File file) throws FileNotFoundException {
		List<String> list = new ArrayList<>();
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter(",");
		while (scanner.hasNext()) {
			list.add(scanner.next());
		}
		scanner.close();
		return list;
	}
	/**
	 * pick a random element of a list. Useful to pick a random
	 * element from a parsed csv file.
	 * @param list of strings.
	 * @return the element randomely chosen.
	 */
	public static String pickRandomElementStringList(final List<String> list) {
		int nbOfElementPicked = standardRandomInt(0, list.size() - 1);
		String element = list.get(nbOfElementPicked);
		return element;
		}
	/*
	 * PART II : Integration use case
	 * The following functions generate data specifically for
	 * the 2nd article use case. This use case consider data
	 * brokers selling data on a decentralised DAG-based
	 * marketplace. Data brokers generate temperature and
	 * humidity levels data with sensors, and grant access
	 * provided the buyers make the transaction, which is verified
	 * by the PEP. In this class, we generate the data according to
	 * this use case
	 */

	/**
	 * print the records in a csv column.
	 * @param url
	 * @param columnKey
	 */
    public static void readColFromCSV(String url, String columnKey) {
        try(
                BufferedReader br = new BufferedReader(new FileReader(url));
                CSVParser parser = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(br);
                ) {
            for (CSVRecord record : parser) {
                System.out.println(record.get(columnKey));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /**
     * count the number of rows.
     * @param url
     * @return
     * @throws IOException
     */
    public static int nbOfRows(String url) throws IOException {
    	int randInt = DataUtils.standardRandomInt(0, 100);
    	int i = 0;
        try (
                BufferedReader br = new BufferedReader(new FileReader(url));
                CSVParser parser = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(br);
                ) {
            for (CSVRecord record : parser) {
            	i++;
            }
        }
        return i;
    }
    /**
     * choose randomly a row
     * in the resource's csv file
     * @param url
     * @param columnKey
     * @return string of the random row chosen
     */
    public static String randomRowFromColumn(String url, String columnKey) {
    	int randInt = DataUtils.standardRandomInt(0, 5000);
    	int i = 0;
        try(
                BufferedReader br = new BufferedReader(new FileReader(url));
                CSVParser parser = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(br);
                ) {
            for (CSVRecord record : parser) {
            	i++;
            	if (i == randInt) {
                return (record.get(columnKey));
            	}
            }
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
		return "";
    }
    /**
     * 
     * @return
     */
    public static float generateLat() {
    	String s = randomRowFromColumn(URL, "lat");
    	return Float.parseFloat(s);
    }
    /**
     * 
     * @return
     */
    public static float generateLon() {
    	String s = randomRowFromColumn(URL, "lng");
    	return Float.parseFloat(s);
    }
    /**
     * gaussian distribution.
     * @param x 
     * @return
     */
    public static double pdf(double x) {
        return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI);
    }

	}

