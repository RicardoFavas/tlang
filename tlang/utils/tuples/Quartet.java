package tlang.utils.tuples;

public class Quartet<T0,T1,T2,T3> {
	private T0 first;
	private T1 second;
	private T2 third;
	private T3 fourth;

	public Quartet(T0 first, T1 second, T2 third, T3 fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}
	public T0 getFirst() { return this.first; }
	public T1 getSecond() { return this.second;	}
	public T2 getThird() { return this.third; }
	public T3 getFourth() { return this.fourth; }
	public T0 get0() { return this.first; }
	public T1 get1() { return this.second;	}
	public T2 get2() { return this.third; }
	public T3 get3() { return this.fourth; }

	public void setFirst(T0 v) { this.first = v; }
	public void setSecond(T1 v) { this.second = v; }
	public void setThird(T2 v) { this.third = v; }
	public void setFourth(T3 v) { this.fourth = v; }

	public void set0(T0 v) { this.first = v; }
	public void set1(T1 v) { this.second = v; }
	public void set2(T2 v) { this.third = v; }
	public void set3(T3 v) { this.fourth = v; }
	
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
		s += ",";
		s += this.fourth;
		s += "]";
		return s;
	}
}
