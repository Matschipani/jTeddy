package fsm.examples.Hierarchical;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;

public class FSMTest {
	
	@Test
	public void simpleStateChange() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.A.class));
		fsm.handleEvent(new MyEvent(Event.toB, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.B.class));
		assertEquals(1, fsm.getSubstates().size());
	}

	@Test
	public void defaultEntry() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.A.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Normal.class));
		fsm.handleEvent(new MyEvent(Event.up, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		fsm.handleEvent(new MyEvent(Event.down, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Low.class));
		fsm.handleEvent(new MyEvent(Event.clear, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Normal.class));
		fsm.handleEvent(new MyEvent(Event.tick, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Normal.class));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(1, fsm.getSubstate(0).getSubstates().size());
	}

	@Test
	public void explicitEntry() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.A.class));
		fsm.handleEvent(new MyEvent(Event.toB, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.B.class));
		fsm.handleEvent(new MyEvent(Event.up, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(1, fsm.getSubstate(0).getSubstates().size());
	}

	@Test
	public void explicitOuterExit() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.A.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Normal.class));
		fsm.handleEvent(new MyEvent(Event.error, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.E.class));
		assertEquals(1, fsm.getSubstates().size());
	}

	@Test
	public void explicitInnerExit() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.A.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Normal.class));
		fsm.handleEvent(new MyEvent(Event.up, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		fsm.handleEvent(new MyEvent(Event.down, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Low.class));
		fsm.handleEvent(new MyEvent(Event.last, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.C.class));
		assertEquals(1, fsm.getSubstates().size());
	}

	@Test
	public void defaultExit() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.A.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Normal.class));
		fsm.handleEvent(new MyEvent(Event.up, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		fsm.handleEvent(new MyEvent(Event.exceed, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.D.class));
		assertEquals(1, fsm.getSubstates().size());
	}

	@Test
	public void enterShallowHistoryAfterExplicitInnerExit() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.A.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Normal.class));
		fsm.handleEvent(new MyEvent(Event.up, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		fsm.handleEvent(new MyEvent(Event.down, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Low.class));
		fsm.handleEvent(new MyEvent(Event.last, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.C.class));

		fsm.handleEvent(new MyEvent(Event.toF, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.F.class));
		fsm.handleEvent(new MyEvent(Event.shallow, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Low.class));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(1, fsm.getSubstate(0).getSubstates().size());
	}

	@Test
	public void enterShallowHistoryAfterExplicitOuterExit() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.A.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Normal.class));
		fsm.handleEvent(new MyEvent(Event.up, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		fsm.handleEvent(new MyEvent(Event.error, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.E.class));

		fsm.handleEvent(new MyEvent(Event.toF, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.F.class));
		fsm.handleEvent(new MyEvent(Event.shallow, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(1, fsm.getSubstate(0).getSubstates().size());
	}

	@Test
	public void enterDeepHistoryAfterExplicitInnerExit() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.A.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Normal.class));
		fsm.handleEvent(new MyEvent(Event.up, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		fsm.handleEvent(new MyEvent(Event.down, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Low.class));
		fsm.handleEvent(new MyEvent(Event.last, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.C.class));

		fsm.handleEvent(new MyEvent(Event.toF, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.F.class));
		fsm.handleEvent(new MyEvent(Event.deep, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Low.class));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(1, fsm.getSubstate(0).getSubstates().size());
	}

	@Test
	public void enterDeepHistoryAfterExplicitOuterExit() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.A.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Normal.class));
		fsm.handleEvent(new MyEvent(Event.up, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		fsm.handleEvent(new MyEvent(Event.error, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.E.class));

		fsm.handleEvent(new MyEvent(Event.toF, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.F.class));
		fsm.handleEvent(new MyEvent(Event.deep, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(1, fsm.getSubstate(0).getSubstates().size());
	}

	@Test
	public void contextData() {
		FSM fsm = new FSM();
		assertEquals("iA", fsm.data.toString());
	}
	
	@Test
	public void parentData() {
		FSM fsm = new FSM();
		assertEquals("iA", fsm.data2.toString());
	}
	
	@Test
	public void entryExitActionOrderDefaultExit() {
		FSM fsm = new FSM();
		assertEquals("iA", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertEquals("iAoAiSiN", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.up, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		assertEquals("iAoAiSiNoNiH", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.exceed, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.D.class));
		assertEquals("iAoAiSiNoNiHoHoSiD", fsm.data.toString());
	}
	
	@Test
	public void entryExitActionOrderExplicitInnerExit() {
		FSM fsm = new FSM();
		assertEquals("iA", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertEquals("iAoAiSiN", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.up, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		assertEquals("iAoAiSiNoNiH", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.down, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.Low.class));
		assertEquals("iAoAiSiNoNiHoHiL", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.last, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.C.class));
		assertEquals("iAoAiSiNoNiHoHiLpLpSiC", fsm.data.toString());
	}
	
	@Test
	public void entryExitActionOrderExplicitOuterExit() {
		FSM fsm = new FSM();
		assertEquals("iA", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertEquals("iAoAiSiN", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.error, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.E.class));
		assertEquals("iAoAiSiNpNpSiE", fsm.data.toString());
	}
	
	@Test
	public void entryExitActionOrderDeepHistory() {
		FSM fsm = new FSM();
		assertEquals("iA", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertEquals("iAoAiSiN", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.up, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		assertEquals("iAoAiSiNoNiH", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.error, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.E.class));
		assertEquals("iAoAiSiNoNiHpHpSiE", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.toF, 0));
		assertThat(fsm.getSubstate(0), instanceOf(FSM.F.class));
		assertEquals("iAoAiSiNoNiHpHpSiEoEiF", fsm.data.toString());
		fsm.handleEvent(new MyEvent(Event.deep, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		assertEquals("iAoAiSiNoNiHpHpSiEoEiFoFuSuH", fsm.data.toString());
	}

	@Test
	public void goToOwnDeepHistory() {
		FSM fsm = new FSM();
		fsm.handleEvent(Event.start);
		fsm.handleEvent(Event.up);
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		fsm.handleEvent(Event.tick);
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(1, fsm.getSubstate(0).getSubstates().size());
	}
	
	@Test
	public void goToOwnShallowHistory() {
		FSM fsm = new FSM();
		fsm.handleEvent(Event.start);
		fsm.handleEvent(Event.up);
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		fsm.handleEvent(Event.shallow);
		assertThat(fsm.getSubstate(0), instanceOf(Sub.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Sub.High.class));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(1, fsm.getSubstate(0).getSubstates().size());
	}
	
}
