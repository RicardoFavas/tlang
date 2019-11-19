package tlang.utils.tuples;

public class Unit<T0> {
	private T0 first;
	
	public Unit(T0 first) {
		this.first = first;
	}
	public T0 getFirst() { return this.first; }

	public void setFirst(T0 v) { this.first = v; }
	public void set0(T0 v) { this.first = v; }
	
	@Override
	public String toString() {
		String s = "[";
		s += getFirst();
		s += "]";
		return s;
	}
}
