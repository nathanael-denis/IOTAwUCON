package org.tsp.IOTAwUCON;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.github.javafaker.Faker;

import iotawucon.DataUtils;

public class FakerTest {
	/**
	 * expected size of the fakers
	 * sample.
	 */
	private int expectedSize = 50;
	/**
	 * checks if faker creation works
	 * properly, by checking the size
	 * of sample.
	 */
	@Test
	public void fakerCreationSize() {
		Set<Faker> sampleSet = DataUtils.createFakers(expectedSize);
		int actualSampleSize = sampleSet.size();
		assertTrue(expectedSize == actualSampleSize);
	}
}
