package fsm.base;

public class Superstate<E extends Enum<E>> extends State<E> {
	
	public Superstate(State<E> from, State<E> sub, Class<E> classEvent) {
		super(from, classEvent);
		if(sub != null) {
			parallelSubstates.add(sub);
			sub.parent = this;
		}
	}

	public void start(State<E> s) {
		parallelSubstates.add(s);
		s.parent = this;
		s.entryAction();
	}

}
