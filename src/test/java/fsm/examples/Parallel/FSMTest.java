package fsm.examples.Parallel;

import org.junit.Test;

import fsm.base.EventConsumption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

public class FSMTest {

	@Test
	public void parallelDefaultEntry() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Init.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.A.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.C.class));
	}

	@Test
	public void parallelDefaultExit() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Init.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		fsm.handleEvent(new MyEvent(Event.advance, 0));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.C.class));
		fsm.handleEvent(new MyEvent(Event.advance, 0));
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B2.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.C.class));
		fsm.handleEvent(new MyEvent(Event.advance, 0));
		assertEquals(1, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub2.C.class));
		fsm.handleEvent(new MyEvent(Event.advance, 0));
		assertEquals(1, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub2.D.class));
		fsm.handleEvent(new MyEvent(Event.advance, 0));
		assertEquals(1, fsm.getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Inactive.class));
		assertEquals(0, fsm.getSubstate(0).getSubstates().size());
	}

	@Test
	public void parallelExplicitOuterExitDefaultEntry() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Init.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		fsm.handleEvent(new MyEvent(Event.advance, 0));
		fsm.handleEvent(new MyEvent(Event.advance, 0));
		fsm.handleEvent(new MyEvent(Event.toD, 0));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B2.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.D.class));

		fsm.handleEvent(new MyEvent(Event.exit, 0));
		fsm.handleEvent(new MyEvent(Event.advance, 0));	// will be ignored
		assertEquals(1, fsm.getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Inactive.class));
		assertEquals(0, fsm.getSubstate(0).getSubstates().size());

		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.A.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.C.class));
	}

	@Test
	public void parallelExplicitOuterExitDeepHistory() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Init.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		fsm.handleEvent(new MyEvent(Event.advance, 0));
		fsm.handleEvent(new MyEvent(Event.advance, 0));
		fsm.handleEvent(new MyEvent(Event.toD, 0));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B2.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.D.class));

		fsm.handleEvent(new MyEvent(Event.exit, 0));
		fsm.handleEvent(new MyEvent(Event.advance, 0));	// will be ignored
		assertEquals(1, fsm.getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Inactive.class));
		assertEquals(0, fsm.getSubstate(0).getSubstates().size());

		fsm.handleEvent(new MyEvent(Event.deep, 0));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B2.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.D.class));
	}

	@Test
	public void parallelExplicitOuterExitShallowHistory() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Init.class));
		fsm.handleEvent(new MyEvent(Event.start, 0));
		fsm.handleEvent(new MyEvent(Event.advance, 0));
		fsm.handleEvent(new MyEvent(Event.advance, 0));
		fsm.handleEvent(new MyEvent(Event.toD, 0));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B2.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.D.class));

		fsm.handleEvent(new MyEvent(Event.exit, 0));
		fsm.handleEvent(new MyEvent(Event.advance, 0));	// will be ignored
		assertEquals(1, fsm.getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Inactive.class));
		assertEquals(0, fsm.getSubstate(0).getSubstates().size());

		fsm.handleEvent(new MyEvent(Event.shallow, 0));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.A.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.C.class));
	}

	@Test
	public void parallelAdd() {
		FSM fsm = new FSM();
		fsm.handleEvent(new MyEvent(Event.start, 0));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.A.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.C.class));
		fsm.handleEvent(new MyEvent(Event.inner, 0));
		assertEquals(3, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2), instanceOf(Active.SubN.class));
		assertEquals(1, fsm.getSubstate(0).getSubstate(2).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2).getSubstate(0), instanceOf(Active.SubN.E.class));
		assertEquals("iSiE", ((Active)fsm.getSubstate(0)).data.toString());
		fsm.handleEvent(new MyEvent(Event.inner, 0));
		assertEquals(4, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2), instanceOf(Active.SubN.class));
		// assertEquals(1, fsm.getSubstate(0).getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2).getSubstate(0), instanceOf(Active.SubN.E.class));
		assertThat(fsm.getSubstate(0).getSubstate(3), instanceOf(Active.SubN.class));
		// assertEquals(1, fsm.getSubstate(0).getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(3).getSubstate(0), instanceOf(Active.SubN.E.class));
		assertEquals("iSiEiSiE", ((Active)fsm.getSubstate(0)).data.toString());
		fsm.handleEvent(new MyEvent(Event.toF, 0));
		assertEquals(4, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2), instanceOf(Active.SubN.class));
		assertThat(fsm.getSubstate(0).getSubstate(2).getSubstate(0), instanceOf(Active.SubN.F.class));
		assertThat(fsm.getSubstate(0).getSubstate(3), instanceOf(Active.SubN.class));
		assertThat(fsm.getSubstate(0).getSubstate(3).getSubstate(0), instanceOf(Active.SubN.E.class));
		assertEquals("iSiEiSiEoEiF", ((Active)fsm.getSubstate(0)).data.toString());

		fsm.handleEvent(new MyEvent(Event.exit, 0));
		fsm.handleEvent(new MyEvent(Event.advance, 0));	// will be ignored
		assertEquals(1, fsm.getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Inactive.class));
		assertEquals(0, fsm.getSubstate(0).getSubstates().size());
		// assertEquals("iSiEiSiEoEiFpEpSpFpS", ((Active)fsm.getSubstate(0)).data.toString());

		fsm.handleEvent(new MyEvent(Event.deep, 0));
		assertEquals(4, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2), instanceOf(Active.SubN.class));
		assertThat(fsm.getSubstate(0).getSubstate(2).getSubstate(0), instanceOf(Active.SubN.F.class));
		assertThat(fsm.getSubstate(0).getSubstate(3), instanceOf(Active.SubN.class));
		assertThat(fsm.getSubstate(0).getSubstate(3).getSubstate(0), instanceOf(Active.SubN.E.class));
		assertEquals("iSiEiSiEoEiFpFpEpSpSuSuSuFuE", ((Active)fsm.getSubstate(0)).data.toString());
	}

	@Test
	public void accessContextData() {
		FSM fsm = new FSM();
		assertEquals(0, fsm.getContextData().i);
		assertEquals("iI", fsm.getContextData().s.toString());
		fsm.handleEvent(new MyEvent(Event.start, 42));
		assertEquals(42, fsm.getContextData().i);
		assertEquals("iIoI", fsm.getContextData().s.toString());
	}

	@Test
	public void fsmTermination() {
		FSM fsm = new FSM();
		fsm.handleEvent(new MyEvent(Event.start, 0));
		fsm.handleEvent(new MyEvent(Event.exit, 0));
		fsm.handleEvent(new MyEvent(Event.exit, 0));
		assertEquals(0, fsm.getSubstates().size());
		fsm.handleEvent(Event.start);	// no exception thrown
		fsm.handleEvent(new MyEvent(Event.start, 0));	// no exception thrown
		assertEquals(0, fsm.getSubstates().size());
	}

	@Test
	public void goToOwnDeepHistoryParallel() {
		FSM fsm = new FSM();
		fsm.handleEvent(new MyEvent(Event.start, 0));
		fsm.handleEvent(Event.advance);
		fsm.handleEvent(Event.advance);
		fsm.handleEvent(Event.toD);
		fsm.handleEvent(Event.inner);
		fsm.handleEvent(Event.toF);

		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B2.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.D.class));
		assertEquals(3, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2), instanceOf(Active.SubN.class));
		assertEquals(1, fsm.getSubstate(0).getSubstate(2).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2).getSubstate(0), instanceOf(Active.SubN.F.class));

		assertEquals(EventConsumption.fullyUsed, fsm.handleEvent(Event.deep));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B2.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.D.class));
		assertEquals(3, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2), instanceOf(Active.SubN.class));
		assertEquals(1, fsm.getSubstate(0).getSubstate(2).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2).getSubstate(0), instanceOf(Active.SubN.F.class));
	}

	@Test
	public void goToOwnShallowHistoryParallel() {
		FSM fsm = new FSM();
		fsm.handleEvent(new MyEvent(Event.start, 0));
		fsm.handleEvent(Event.advance);
		fsm.handleEvent(Event.advance);
		fsm.handleEvent(Event.toD);
		fsm.handleEvent(Event.inner);
		fsm.handleEvent(Event.toF);

		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B2.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.D.class));
		assertEquals(3, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2), instanceOf(Active.SubN.class));
		assertEquals(1, fsm.getSubstate(0).getSubstate(2).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2).getSubstate(0), instanceOf(Active.SubN.F.class));

		assertEquals(EventConsumption.fullyUsed, fsm.handleEvent(Event.shallow));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.A.class));
		assertEquals(0, fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.C.class));
		assertEquals(3, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2), instanceOf(Active.SubN.class));
		assertEquals(1, fsm.getSubstate(0).getSubstate(2).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(2).getSubstate(0), instanceOf(Active.SubN.E.class));
	}

}
