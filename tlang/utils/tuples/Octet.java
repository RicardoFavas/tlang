package tlang.utils.tuples;

public class Octet<T0,T1,T2,T3,T4,T5,T6,T7> {
	private T0 first;
	private T1 second;
	private T2 third;
	private T3 fourth;
	private T4 fifth;
	private T5 sixth;
	private T6 seventh;
	private T7 eighth;

	public Octet(T0 first, T1 second, T2 third, T3 fourth, T4 fifth, T5 sixth, T6 seventh, T7 eighth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
		this.fifth = fifth;
		this.sixth = sixth;
		this.seventh = seventh;
		this.eighth = eighth;
	}
	public T0 getFirst() { return this.first; }
	public T1 getSecond() { return this.second;	}
	public T2 getThird() { return this.third; }
	public T3 getFourth() { return this.fourth; }
	public T4 getFifth() { return this.fifth; }
	public T5 getSixth() { return this.sixth; }
	public T6 getSeventh() { return this.seventh; }
	public T7 getEighth() { return this.eighth; }
	public T0 get0() { return this.first; }
	public T1 get1() { return this.second;	}
	public T2 get2() { return this.third; }
	public T3 get3() { return this.fourth; }
	public T4 get4() { return this.fifth; }
	public T5 get5() { return this.sixth; }
	public T6 get6() { return this.seventh; }
	public T7 get7() { return this.eighth; }

	public void setFirst(T0 v) { this.first = v; }
	public void setSecond(T1 v) { this.second = v; }
	public void setThird(T2 v) { this.third = v; }
	public void setFourth(T3 v) { this.fourth = v; }
	public void setFifth(T4 v) { this.fifth = v; }
	public void setSixth(T5 v) { this.sixth = v; }
	public void setSeventh(T6 v) { this.seventh = v; }
	public void setEighth(T7 v) { this.eighth = v; }
	public void set0(T0 v) { this.first = v; }
	public void set1(T1 v) { this.second = v; }
	public void set2(T2 v) { this.third = v; }
	public void set3(T3 v) { this.fourth = v; }
	public void set4(T4 v) { this.fifth = v; }
	public void set5h(T5 v) { this.sixth = v; }
	public void set6(T6 v) { this.seventh = v; }
	public void set7h(T7 v) { this.eighth = v; }

	
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
		s += ",";
		s += this.seventh;
		s += ",";
		s += this.eighth;
		s += "]";
		return s;
	}
}
