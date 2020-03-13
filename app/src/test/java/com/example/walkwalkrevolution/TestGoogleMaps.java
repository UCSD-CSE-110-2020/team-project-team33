package com.example.walkwalkrevolution;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.walkwalkrevolution.ui.main.ProposedWalkFragment;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TestGoogleMaps {
    @Test
    public void testGoogleMapsURLNoSpace() {
        String address = "Walmart";
        System.out.println(ProposedWalkFragment.getGoogleMapsURL(address).toString());
        assertEquals("http://maps.google.co.in/maps?q=Walmart", ProposedWalkFragment.getGoogleMapsURL(address).toString());
    }
    @Test
    public void testGoogleMapsURLWithSpace() {
        String address = "University of California, San Diego";
        System.out.println(ProposedWalkFragment.getGoogleMapsURL(address).toString());
        assertEquals("http://maps.google.co.in/maps?q=University of California, San Diego", ProposedWalkFragment.getGoogleMapsURL(address).toString());
    }
}
