package org.tsp.IOTAwUCON;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import iotawucon.IOTAUtils;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-) .
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }
    
    @Test
    public void connectionNodeWorks() {
    	IOTAUtils.nodeInfo();
    }
}
