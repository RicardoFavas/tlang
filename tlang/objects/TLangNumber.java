package tlang.objects;

import java.math.BigDecimal;

public class TLangNumber extends TLangObject {

	final public BigDecimal value;	
	public TLangNumber(BigDecimal number) {
		super();
		this.value = number;
	}
	public TLangNumber(Number number) { this(new BigDecimal(number.toString())); }
	public TLangNumber(double number) { this(new BigDecimal(number)); }
	public TLangNumber(long number) { this(new BigDecimal(number)); }
	public TLangNumber(float number) { this(new BigDecimal(number)); }
	public TLangNumber(int number) { this(new BigDecimal(number)); }
	public TLangNumber(short number) { this(new BigDecimal(number)); }

	
	public TLangObject $add(TLangObject otherObject) throws TLangException {
		if (otherObject instanceof TLangNumber) {
			TLangNumber otherNumber = (TLangNumber)otherObject;
			return new TLangNumber(value.add(otherNumber.value,TLangConstants.MC));
		} 
		throw new TLangException();
	}
	public TLangObject $mod(TLangObject otherObject) throws TLangException {
		if (otherObject instanceof TLangNumber) {
			TLangNumber otherNumber = (TLangNumber)otherObject;
			return new TLangNumber(value.remainder(otherNumber.value,TLangConstants.MC));
		}
		throw new TLangException();
	}
	
	
	
	public TLangObject $lesserThan(TLangObject otherObject) {
		if (!(otherObject instanceof TLangNumber))
			return TLangConstants.FALSE;
		TLangNumber otherNumber = (TLangNumber)otherObject;
		if (this.value != null && this.value.compareTo(otherNumber.value) < 0) {
			return TLangConstants.TRUE;
		}
		return TLangConstants.FALSE;
	}
	public TLangObject $greaterThan(TLangObject otherObject) {
		if (!(otherObject instanceof TLangNumber))
			return TLangConstants.FALSE;
		TLangNumber otherNumber = (TLangNumber)otherObject;
		if (this.value != null && this.value.compareTo(otherNumber.value) > 0) {
			return TLangConstants.TRUE;
		}
		return TLangConstants.FALSE;
	}
	public TLangObject $lesserEqualsThan(TLangObject otherObject) {
		if (!(otherObject instanceof TLangNumber))
			return TLangConstants.FALSE;
		TLangNumber otherNumber = (TLangNumber)otherObject;
		if (this.value != null && this.value.compareTo(otherNumber.value) <= 0) {
			return TLangConstants.TRUE;
		}
		return TLangConstants.FALSE;
	}
	public TLangObject $greaterEqualsThan(TLangObject otherObject) {
		if (!(otherObject instanceof TLangNumber))
			return TLangConstants.FALSE;
		TLangNumber otherNumber = (TLangNumber)otherObject;
		if (this.value != null && this.value.compareTo(otherNumber.value) >= 0) {
			return TLangConstants.TRUE;
		}
		return TLangConstants.FALSE;
	}

	
	public TLangObject $sub(TLangObject otherObject) throws TLangException {
		if (otherObject instanceof TLangNumber) {
			TLangNumber otherNumber = (TLangNumber)otherObject;
			return new TLangNumber(value.subtract(otherNumber.value,TLangConstants.MC));
		}
		throw new TLangException();
	}
	public TLangObject $multiply(TLangObject otherObject) throws TLangException {
		if (otherObject instanceof TLangNumber) {
			TLangNumber otherNumber = (TLangNumber)otherObject;
			return new TLangNumber(value.multiply(otherNumber.value,TLangConstants.MC));
		}
		throw new TLangException();
	}
	public TLangObject $divide(TLangObject otherObject) throws TLangException {
		if (otherObject instanceof TLangNumber) {
			TLangNumber otherNumber = (TLangNumber)otherObject;
			return new TLangNumber(value.divide(otherNumber.value,TLangConstants.MC));
		}
		throw new TLangException();
	}
	public TLangObject $pow(TLangObject otherObject) throws TLangException {
		if (otherObject instanceof TLangNumber) {
			TLangNumber otherNumber = (TLangNumber)otherObject;
			return new TLangNumber(value.pow(otherNumber.value.intValueExact(),TLangConstants.MC));
		}
		throw new TLangException();
	}
	public TLangObject $negate() throws Exception {
		return new TLangNumber(this.value.negate(TLangConstants.MC));
	}

	public TLangObject round(TLangObject otherObject) throws Exception {
		if (otherObject instanceof TLangNumber) {
			TLangNumber otherNumber = (TLangNumber)otherObject;
			return new TLangNumber(value.setScale(otherNumber.value.intValue(),TLangConstants.MC.getRoundingMode()));
		}
		throw new Exception();
	}

	@Override
	public TLangObject $equals(TLangObject otherObject) {
		if (!(otherObject instanceof TLangNumber))
			return TLangConstants.FALSE;
		TLangNumber otherNumber = (TLangNumber)otherObject;
		if (this.value != null && this.value.compareTo(otherNumber.value) == 0) {
			return TLangConstants.TRUE;
		}
		return TLangConstants.FALSE;
	}

	public TLangBoolean isInteger() {
		if (value.signum() == 0)
			return TLangConstants.TRUE;
		if (value.scale() <= 0)
			return TLangConstants.TRUE;
		if (value.stripTrailingZeros().scale() <= 0)
			return TLangConstants.TRUE;
		return TLangConstants.FALSE;
	}
	public TLangNumber abs() {
		return new TLangNumber(value.abs(TLangConstants.MC));
	}

	@Override
	public String toString() {
		return ""+value;
	}
	@Override
	public boolean equals(Object obj) {
		return $equals((TLangObject)obj) == TLangConstants.TRUE;
	}
	@Override
	public int hashCode() {
		return value.hashCode();
	}

}
