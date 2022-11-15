package environment;

import game.Game;
import game.Player;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player=null;
	
	public Cell(Coordinate position, Game g) {
		super();
		this.position = position;
		this.game = g;
	}

	public Coordinate getPosition() {
		return position;
	}

	public boolean isOcupied() {
		return player!=null;
	}


	public Player getPlayer() {
		return player;
	}

	//Criada e só é usada no início do jogo para colocar os players
	public synchronized void spawnPlayer(Player player){
		if(this.isOcupied()) {
			try {
				wait();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		this.player = player;
		player.setCell(this);
	}
	
	public synchronized void setPlayer(Player player) {
		//TODO este metodo e os metodos relacionados necessitam de um rework para distinguir spawn e confronto. Nao sei se esses metodos serao definidos nesta classe ou noutra
		//Criar método confronto entre Players e colocar neste if

		if(this.isOcupied())
			try {
				System.out.println("Jogador " + player.getIdentification() + " não foi colocado, esperando...");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		this.player = player;
		player.setCell(this);
		//notifyAll();
		System.out.println("O jogador " + player.getIdentification() + " foi adicionado.");
	}
	
	public Coordinate getCoordinate() {
		return position;
	}
	
	public synchronized void unsetPlayer() {
		if(!isOcupied())
			return;
		
		player = null;
		game.notifyChange();
		notifyAll();
		
		
	}

	
	public void setCoordinate(Coordinate c) {
		position = c;
	}
	
}
