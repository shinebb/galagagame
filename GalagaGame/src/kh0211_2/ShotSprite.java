package kh0211_2;
import java.awt.Image;

public class ShotSprite extends Sprite{
	private GalagaGame game;
	
	public ShotSprite(GalagaGame game, Image image, int x, int y) {
		super(image, x,y);
		this.game=game;
		dy= -5;
	}
	
	@Override
	public void move() {
		super.move();
		if(y< -100) {
			game.removeSprite(this);
		}
	}
	
	@Override
	public void handleCollision(Sprite other) {
		// other 객체가 AlienSprite 타입으로 형변환 가능한지 확인(총알이 적과 닿았는지 확인)
		if(other instanceof AlienSprite) {
			game.removeSprite(this);
			game.removeSprite(other);
			game.scoreUp();
		}
	}
}
