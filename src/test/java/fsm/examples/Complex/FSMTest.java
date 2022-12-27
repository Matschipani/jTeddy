package fsm.examples.Complex;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

public class FSMTest {

	@Test
	public void orthogonalDefaultEntry() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Init.class));
		fsm.handleEvent(Event.start);
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.A.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.C.class));
	}

	@Test
	public void orthogonalDefaultExit() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Init.class));
		fsm.handleEvent(Event.start);
		fsm.handleEvent(Event.advance);
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.C.class));
		fsm.handleEvent(Event.advance);
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B2.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.C.class));
		fsm.handleEvent(Event.advance);
		assertEquals(1, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub2.C.class));
		fsm.handleEvent(Event.advance);
		assertEquals(1, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub2.D.class));
		fsm.handleEvent(Event.advance);
		assertEquals(1, fsm.getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Inactive.class));
		assertEquals(0, fsm.getSubstate(0).getSubstates().size());
	}

	@Test
	public void orthogonalExplicitOuterExitDeepHistory() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Init.class));
		fsm.handleEvent(Event.start);
		fsm.handleEvent(Event.advance);
		fsm.handleEvent(Event.advance);
		fsm.handleEvent(Event.toD);
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B2.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.D.class));

		fsm.handleEvent(Event.exit);
		fsm.handleEvent(Event.advance);	// will be ignored
		assertEquals(1, fsm.getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Inactive.class));
		assertEquals(0, fsm.getSubstate(0).getSubstates().size());

		fsm.handleEvent(Event.deep);
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
	public void orthogonalExplicitOuterExitShallowHistory() {
		FSM fsm = new FSM();
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Init.class));
		fsm.handleEvent(Event.start);
		fsm.handleEvent(Event.advance);
		fsm.handleEvent(Event.advance);
		fsm.handleEvent(Event.toD);
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B2.class));
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.D.class));

		fsm.handleEvent(Event.exit);
		fsm.handleEvent(Event.advance);	// should be ignored
		assertEquals(1, fsm.getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(FSM.Inactive.class));
		assertEquals(0, fsm.getSubstate(0).getSubstates().size());

		fsm.handleEvent(Event.shallow);
		assertEquals(1, fsm.getSubstates().size());
		assertEquals(2, fsm.getSubstate(0).getSubstates().size());
		assertThat(fsm.getSubstate(0), instanceOf(Active.class));
		assertThat(fsm.getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.class));
		assertThat(fsm.getSubstate(0).getSubstate(1), instanceOf(Active.Sub2.class));
		assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.A.class));
		// assertThat(fsm.getSubstate(0).getSubstate(0).getSubstate(0).getSubstate(0), instanceOf(Active.Sub1.B.B1.class));	// !!!
		assertThat(fsm.getSubstate(0).getSubstate(1).getSubstate(0), instanceOf(Active.Sub2.C.class));
	}

}
