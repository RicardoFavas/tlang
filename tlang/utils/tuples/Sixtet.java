package tlang.utils.tuples;

public class Sixtet<T0,T1,T2,T3,T4,T5,T6,T7,T8,T9> {
	private T0 first;
	private T1 second;
	private T2 third;
	private T3 fourth;
	private T4 fifth;
	private T5 sixth;

	public Sixtet(T0 first, T1 second, T2 third, T3 fourth, T4 fifth, T5 sixth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
		this.fifth = fifth;
		this.sixth = sixth;
	}
	public T0 getFirst() { return this.first; }
	public T1 getSecond() { return this.second;	}
	public T2 getThird() { return this.third; }
	public T3 getFourth() { return this.fourth; }
	public T4 getFifth() { return this.fifth; }
	public T5 getSixth() { return this.sixth; }
	public T0 get0() { return this.first; }
	public T1 get1() { return this.second;	}
	public T2 get2() { return this.third; }
	public T3 get3() { return this.fourth; }
	public T4 get4() { return this.fifth; }
	public T5 get5() { return this.sixth; }


	public void setFirst(T0 v) { this.first = v; }
	public void setSecond(T1 v) { this.second = v; }
	public void setThird(T2 v) { this.third = v; }
	public void setFourth(T3 v) { this.fourth = v; }
	public void setFifth(T4 v) { this.fifth = v; }
	public void setSixth(T5 v) { this.sixth = v; }
	public void set0(T0 v) { this.first = v; }
	public void set1(T1 v) { this.second = v; }
	public void set2(T2 v) { this.third = v; }
	public void set3(T3 v) { this.fourth = v; }
	public void set4(T4 v) { this.fifth = v; }
	public void set5h(T5 v) { this.sixth = v; }

	
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
		s += ",";
		s += this.fifth;
		s += ",";
		s += this.sixth;
		s += "]";
		return s;
	}
}
