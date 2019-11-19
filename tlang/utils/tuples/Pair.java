package tlang.utils.tuples;

public class Pair<T0,T1> {
	private T0 first;
	private T1 second;
	
	public Pair(T0 first, T1 second) {
		this.first = first;
		this.second = second;
	}
	public T0 getFirst() { return this.first; }
	public T1 getSecond() { return this.second;	}
	public T0 get0() { return this.first; }
	public T1 get1() { return this.second;	}


	public void setFirst(T0 v) { this.first = v; }
	public void setSecond(T1 v) { this.second = v; }
	public void set0(T0 v) { this.first = v; }
	public void set1(T1 v) { this.second = v; }
	
	@Override
	public String toString() {
		String s = "[";
		s += getFirst();
		s += ",";
		s += this.first;
		s += ",";
		s += this.second;
		s += "]";
		return s;
	}
}
