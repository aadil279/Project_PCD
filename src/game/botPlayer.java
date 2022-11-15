package game;

import environment.Direction;

import static java.lang.Thread.sleep;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class botPlayer extends Player implements Runnable {
	public botPlayer(int id, byte strength) {
		super(id, strength);
	}

	@Override
	public boolean isHumanPlayer() {
		return false;
	}

	//Função que calcula de forma random para onde o player se vai mover 

	@Override
	public Direction chosenDirection() {
		Direction d = null;
		int prob = (int)Math.round((Math.random() * 3));

		switch (prob) {
			case 0 ->	d = Direction.UP;
			case 1 ->	d = Direction.RIGHT;
			case 2 ->	d = Direction.LEFT;
			case 3 ->	d = Direction.DOWN;
		}
		return d;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			sleep(game.INITIAL_WAITING_TIME);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}


		int counter = 0;
		while (true) {
			counter++;
			System.out.println("Counter = " + counter + " Player ID = " + this.getIdentification());
			if (counter == originalStrength) {
				counter = 0;
				//System.out.println("Counter dentro do if = " + counter);
				synchronized (this) {
					move();
			/*try {
				wait(Game.REFRESH_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
				}
			}
			try {
				sleep(game.REFRESH_INTERVAL);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

		}
	}
}