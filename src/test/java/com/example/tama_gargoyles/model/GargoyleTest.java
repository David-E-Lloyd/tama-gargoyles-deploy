package com.example.tama_gargoyles.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class GargoyleTest {
    @Test
        public void gargoyleHasUserId() {
        User user = new User("Test@Testemail.com");
        user.setId(1L);
        Gargoyle gargoyle = new Gargoyle(user);

        assertThat(gargoyle.getUser().getId(), is(1L));
        }

    @Test
        public void gargoyleCanStoreDefaultValues() {
        User user = new User("Test@Testemail.com");
        user.setId(1L);
        Gargoyle gargoyle = new Gargoyle(user);

        assertThat(gargoyle.getAge(), is(0));
        assertThat(gargoyle.getType(), is(Gargoyle.Type.CHILD));
        assertThat(gargoyle.getStatus(), is(Gargoyle.Status.ACTIVE));

        assertThat(gargoyle.getHunger(), is(100));
        assertThat(gargoyle.getHappiness(), is(100));
        assertThat(gargoyle.getHealth(), is(100));
        assertThat(gargoyle.getExperience(), is(0));

        assertThat(gargoyle.getStrength(), is(25));
        assertThat(gargoyle.getSpeed(), is(10));
        assertThat(gargoyle.getIntelligence(), is(10));

        assertThat(gargoyle.isPaused(), is(false));
        assertThat(gargoyle.getActiveMinutes(), is(0L));

        assertThat(gargoyle.getLastTickAt() != null, is(true));
    }
    @Test
    public void testLombokGettersAndSetters(){

        User user = new User("Test@Testemail.com");
        user.setId(1L);
        Gargoyle gargoyle = new Gargoyle(user);

        gargoyle.setName("Test");
        gargoyle.setAge(10);
        gargoyle.setType(Gargoyle.Type.BAD);
        gargoyle.setStatus(Gargoyle.Status.RETIRED);

        gargoyle.setHunger(5);
        gargoyle.setHappiness(5);
        gargoyle.setHealth(5);
        gargoyle.setExperience(5);

        gargoyle.setStrength(50);
        gargoyle.setSpeed(50);
        gargoyle.setIntelligence(50);

        gargoyle.setPaused(true);
        gargoyle.setPausedAt(Instant.now());
        gargoyle.setActiveMinutes(10L);


        assertEquals("Test", gargoyle.getName());
        assertEquals(Integer.valueOf(10), gargoyle.getAge());
        assertEquals(Gargoyle.Type.BAD, gargoyle.getType());
        assertEquals(Gargoyle.Status.RETIRED, gargoyle.getStatus());

        assertEquals(Integer.valueOf(5), gargoyle.getHunger());
        assertEquals(Integer.valueOf(5), gargoyle.getHappiness());
        assertEquals(Integer.valueOf(5), gargoyle.getHealth());
        assertEquals(Integer.valueOf(5), gargoyle.getExperience());

        assertEquals(Integer.valueOf(50), gargoyle.getStrength());
        assertEquals(Integer.valueOf(50), gargoyle.getSpeed());
        assertEquals(Integer.valueOf(50), gargoyle.getIntelligence());

        assertNotNull(gargoyle.getPausedAt());
        assertEquals(10L, gargoyle.getActiveMinutes());
    }
}
