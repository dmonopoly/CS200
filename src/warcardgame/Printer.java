package warcardgame;

/**
 * A class just to make printing and quick debugging easier.
 * This allows turning off print and println statements without having to tediously search for them.
 * @author David Zhang
 */
public final class Printer {
	private boolean on;

	public Printer() {
		// adjust as appropriate when coding
		turnOn();
//		turnOff();
	}
	
	public void print(Object obj) {
		if (on)
			System.out.print(obj);
	}

	public void println() {
		if (on)
			System.out.println();
	}

	public void println(Object obj) {
		if (on)
			System.out.println(obj);
	}

	public void turnOff() {
		on = false;
	}

	public void turnOn() {
		on = true;
	}
}
