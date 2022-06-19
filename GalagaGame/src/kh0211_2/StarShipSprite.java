package kh0211_2;
import java.awt.Image;

public class StarShipSprite extends Sprite {
	private GalagaGame game;
	
	public StarShipSprite(GalagaGame game, Image image, int x, int y) {
		super(image, x,y);
		this.game = game;
		dx =0;
		dy =0;
	}
	
	@Override
	public void move() {
		if ((dx<0) && (x<10)) {
			return;
		}
		if((dx>0)&& (x>750)) {
			return;
		}
		if ((dy<0) && (y<10)) {
			return;
		}
		if((dy>0)&& (y>650)) {
			return;
		}
		super.move();
	}
}
