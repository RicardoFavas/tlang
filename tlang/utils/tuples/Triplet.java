package tlang.utils.tuples;

public class Triplet<T0,T1,T2> {
	private T0 first;
	private T1 second;
	private T2 third;
	
	public Triplet(T0 first, T1 second, T2 third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}
	public T0 getFirst() { return this.first; }
	public T1 getSecond() { return this.second;	}
	public T2 getThird() { return this.third; }
	public T0 get0() { return this.first; }
	public T1 get1() { return this.second;	}
	public T2 get2() { return this.third; }

	public void setFirst(T0 v) { this.first = v; }
	public void setSecond(T1 v) { this.second = v; }
	public void setThird(T2 v) { this.third = v; }
	public void set0(T0 v) { this.first = v; }
	public void set1(T1 v) { this.second = v; }
	public void set2(T2 v) { this.third = v; }
	
	@Override
	public String toString() {
		String s = "[";
		s += getFirst();
		s += ",";
		s += this.first;
		s += ",";
		s += this.second;
		s += ",";
		s += this.third;
		s += "]";
		return s;
	}
}
