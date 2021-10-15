
/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	enum status {thinking, hungry, eating, talkative, talking};
	private static status[] state;

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers

		state = new status[piNumberOfPhilosophers];
		for (int i = 0; i < piNumberOfPhilosophers; i++){
			state[i] = status.thinking;
		}
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID) {
			state[piTID - 1] = status.hungry;
			if ((state[(piTID + state.length - 2) % state.length] != status.eating) && (state[piTID - 1] == status.hungry) &&
					(state[piTID % state.length] != status.eating)) {
				state[piTID - 1] = status.eating;
			} else {
				try {
					wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		// ...
		state[(piTID-1)% state.length] = status.thinking;
		notifyAll();
	}

	/**
	 * Only one philosopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk(final int piTID)
	{
		// ..
			state[piTID-1] = status.talkative;
			for (int i = 0; i < state.length; i++){
				if (state[i] == status.talking){
					try {
						wait();
					} catch (Exception e){
						e.printStackTrace();
					}
					break;
				}
			}
			state[piTID-1] = status.talking;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk(final int piTID)
	{
		// ...
		state[piTID-1] = status.thinking;
		notifyAll();
	}
}

// EOF
