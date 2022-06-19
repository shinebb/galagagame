package kh0211_2;
import java.awt.Image;

public class AlienSprite extends Sprite{
	private GalagaGame game;
	private int count;
	
	public AlienSprite(GalagaGame game, Image image, int x, int y) {
		super(image, x, y);
		this.game = game;
		dx = -3;
		dy = 0;
		count =0;
	}
	
	public AlienSprite(GalagaGame game, Image image, int x, int y, int hp) {
		super(image, x, y);
		this.game = game;
		this.hp = hp;
		dx = -5;
		dy = 0;
	}
	
	@Override
	public void move() {
		count++;
		if(count>=100) {
			y += 10;
			count=0;
			if (y >650) {
				game.gameEnd();
			}
		}
		if(((dx<0)&& (x<10))|| ((dx>0) && (x>800))) dx = -dx;
		super.move();
	}
}
