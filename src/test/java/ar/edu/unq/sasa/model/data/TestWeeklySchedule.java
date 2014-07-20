package ar.edu.unq.sasa.model.data;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

public class TestWeeklySchedule {
	
	public WeeklySchedule weeklySchedule;
	public GregorianCalendar mondayCalendar;
	public GregorianCalendar thursdayCalendar;
	public HourInterval hourInterval1;
	public HourInterval hourInterval2;
	public HourInterval hourInterval3;
	public HourInterval hourInterval4;
	public HourInterval hourInterval5;
	public HourInterval hourInterval6;
	public HourInterval hourInterval7;
	public Timestamp timestamp11;
	public Timestamp timestamp12;
	public Timestamp timestamp21;
	public Timestamp timestamp22;
	public Timestamp timestamp31;
	public Timestamp timestamp32;
	public Map<HourInterval,Assignment> mha1;
	public Timestamp timestamp41;
	public Timestamp timestamp42;
	public Timestamp timestamp51;
	public Timestamp timestamp52;
	public Timestamp timestamp61;
	public Timestamp timestamp62;
	public Timestamp timestamp71;
	public Timestamp timestamp72;
	public Map<HourInterval,Assignment> mha2;
	public Assignment a1;
	public Assignment a2;
	public Assignment a3;
	public Assignment a4;
	public Assignment a5;
	public Assignment a6;
	public Assignment a7;

	@Before
	public void setUp(){
		this.mha1 = new HashMap<HourInterval,Assignment>(); 
		this.mha2 = new HashMap<HourInterval,Assignment>();
		this.a1 = createMock(Assignment.class);
		this.a2 = createMock(Assignment.class);
		this.a3 = createMock(Assignment.class);
		this.a4 = createMock(Assignment.class);
		this.a5 = createMock(Assignment.class);
		this.a6 = createMock(Assignment.class);
		this.a7 = createMock(Assignment.class);
		this.timestamp11 = createMock(Timestamp.class);
		this.timestamp12 = createMock(Timestamp.class);
		this.hourInterval1 = createMock(HourInterval.class); 
		this.timestamp21 = createMock(Timestamp.class);
		this.timestamp22 = createMock(Timestamp.class);
		this.hourInterval2 = createMock(HourInterval.class); //.withConstructor(timestamp21,timestamp22);
		this.timestamp31 = createMock(Timestamp.class);
		this.timestamp32 = createMock(Timestamp.class);
		this.hourInterval3 = createMock(HourInterval.class); //.withConstructor(timestamp31,timestamp32);
		this.timestamp71 = createMock(Timestamp.class);
		this.timestamp72 = createMock(Timestamp.class);
		this.hourInterval7 = createMock(HourInterval.class);//.withConstructor(timestamp71,timestamp72);
		this.timestamp41 = createMock(Timestamp.class);
		this.timestamp42 = createMock(Timestamp.class);
		this.hourInterval4 = createMock(HourInterval.class);//.withConstructor(timestamp41,timestamp42);
		this.timestamp51 = createMock(Timestamp.class);
		this.timestamp52 = createMock(Timestamp.class);
		this.hourInterval5 = createMock(HourInterval.class);//.withConstructor(timestamp51,timestamp52);
		this.timestamp61 = createMock(Timestamp.class);
		this.timestamp62 = createMock(Timestamp.class);
		this.hourInterval6 = createMock(HourInterval.class);//.withConstructor(timestamp61,timestamp62);
		
		this.weeklySchedule = new WeeklySchedule();
		this.mondayCalendar = new GregorianCalendar(2010, Calendar.AUGUST, 16);
		this.thursdayCalendar = new GregorianCalendar(2010, Calendar.AUGUST, 19);
		//------------------------------------------------------------------------//
		expect(this.hourInterval1.intersectsWith(this.hourInterval2)).andReturn(false);
		expect(this.hourInterval1.intersectsWith(this.hourInterval3)).andReturn(false);
		//------------------------------------------------------------------------//
		expect(this.hourInterval2.intersectsWith(this.hourInterval1)).andReturn(false);
		expect(this.hourInterval2.intersectsWith(this.hourInterval3)).andReturn(false);
		//------------------------------------------------------------------------//
		expect(this.hourInterval3.intersectsWith(this.hourInterval1)).andReturn(false);
		expect(this.hourInterval3.intersectsWith(this.hourInterval2)).andReturn(false);
		//------------------------------------------------------------------------//
		//------------------------------------------------------------------------//
		expect(this.hourInterval7.intersectsWith(this.hourInterval4)).andReturn(false);
		expect(this.hourInterval7.intersectsWith(this.hourInterval5)).andReturn(false);
		expect(this.hourInterval7.intersectsWith(this.hourInterval6)).andReturn(false);
		//------------------------------------------------------------------------//
		expect(this.hourInterval4.intersectsWith(this.hourInterval7)).andReturn(false);
		expect(this.hourInterval4.intersectsWith(this.hourInterval5)).andReturn(false);
		expect(this.hourInterval4.intersectsWith(this.hourInterval6)).andReturn(false);
		//------------------------------------------------------------------------//
		expect(this.hourInterval5.intersectsWith(this.hourInterval7)).andReturn(false);
		expect(this.hourInterval5.intersectsWith(this.hourInterval4)).andReturn(false);
		expect(this.hourInterval5.intersectsWith(this.hourInterval6)).andReturn(false);
		//------------------------------------------------------------------------//
		expect(this.hourInterval6.intersectsWith(this.hourInterval7)).andReturn(false);
		expect(this.hourInterval6.intersectsWith(this.hourInterval4)).andReturn(false);
		expect(this.hourInterval6.intersectsWith(this.hourInterval5)).andReturn(false);
		
		this.mha1.put(hourInterval1, a1);
		this.mha1.put(hourInterval2, a2);
		this.mha1.put(hourInterval3, a3);
		this.mha2.put(hourInterval7, a7);
		this.mha2.put(hourInterval4, a4);
		this.mha2.put(hourInterval5, a5);
		this.mha2.put(hourInterval6, a6);
		

		replay(this.hourInterval1);
		replay(this.hourInterval2);
		replay(this.hourInterval3);
		replay(this.hourInterval4);
		replay(this.hourInterval5);
		replay(this.hourInterval6);
		replay(this.hourInterval7);
		replay(this.timestamp11);
		replay(this.timestamp12);
		replay(this.timestamp21);
		replay(this.timestamp22);
		replay(this.timestamp31);
		replay(this.timestamp32);
		replay(this.timestamp41);
		replay(this.timestamp42);
		replay(this.timestamp51);
		replay(this.timestamp52);
		replay(this.timestamp61);
		replay(this.timestamp62);
		replay(this.timestamp71);
		replay(this.timestamp72);
		replay(this.a1);
		replay(this.a2);
		replay(this.a3);
		replay(this.a4);
		replay(this.a5);
		replay(this.a6);
		replay(this.a7);
		
		this.weeklySchedule.getSchedule().put(this.mondayCalendar, this.mha1);
		this.weeklySchedule.getSchedule().put(this.thursdayCalendar, this.mha2);
		
	}

	@Test
	public void test_shouldConstructCorrectly(){
		assertTrue("weeklySchedule is not of the desired class",this.weeklySchedule.getSchedule() instanceof Map<?, ?>);
	}

	@Test
	public void test_doesntContainsRepeatedDays(){
		int mapLenght = this.weeklySchedule.getSchedule().size();
		int calendarsLenght;
		int fcalendarsLenght;
		
		Set<Calendar> sc = this.weeklySchedule.getSchedule().keySet();
		calendarsLenght = sc.size();
		
		ArrayList<String> alc = new ArrayList<String>(); 
		
		for(Calendar it : sc){
			alc.add(it.toString());
		}
		
		fcalendarsLenght = alc.size();
		
		assertTrue("Repeated Weeks -traceback 1-",mapLenght==calendarsLenght);
		assertTrue("Repeated Weeks -traceback 2-",fcalendarsLenght==calendarsLenght);
	}

	@Test
	public void test_daysAreFromTheSameWeek(){
		Set<Calendar> sc = this.weeklySchedule.getSchedule().keySet();
		
		for(Calendar it1 : sc){
			for(Calendar it2 : sc){
				assertTrue("Days are from different weeks",
						it1.get(Calendar.WEEK_OF_YEAR) == it2.get(Calendar.WEEK_OF_YEAR));
			}
		}
	}

	public boolean haveSuperpositions(Map<HourInterval,Assignment> m){
		boolean ret = false;
		Set<HourInterval> shi = m.keySet();
		
		if(m.size()!=shi.size()) //Copies deleted on casting = repeated elements
			{ret = true;}        //                            (superpositions)
		
		for(HourInterval current : shi){
			for(HourInterval it : shi){
				if(!(current.equals(it))){
					if(current.intersectsWith(it))
						{ret = true;}
					}
				}
			}
		
		return ret;
	}

	@Test
	public void test_HourIntervalsDoNotSuperimpose(){

		Set<Map.Entry<Calendar, Map<HourInterval, Assignment>>> s = this.weeklySchedule
				.getSchedule().entrySet();

		for (Map.Entry<Calendar, Map<HourInterval, Assignment>> it : s) {

			assertFalse("The item have superimpositions",
					this.haveSuperpositions(it.getValue()));
		}
	}
}
