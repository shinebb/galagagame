package kh0211_2;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Sprite {
	protected int x;	// 현재위치의 x
	protected int y;	// 현재위치의 y
	protected int dx;	// 단위시간에 움직이는 x방향 거리
	protected int dy;	// 단위시간에 움직이는 y방향 거리
	protected int hp=0;
	protected Image image; // 스프라이트가 가지고 있는 이미지
	
	public Sprite(Image image, int x, int y) {
		this.image = image;
		this.x = x;
		this.y = y;
	}
	public int getWidth() {
		return image.getWidth(null);
	}
	public int getHeight() {
		return image.getHeight(null);
	}
	public void draw(Graphics g) {
		g.drawImage(image,x,y,null);
	}
	public void move() {
		x+= dx;
		y+= dy;
	}
	public void setDx(int dx) {
		this.dx = dx;
	}
	public void setDy(int dy) {
		this.dy=dy;
	}
	public int getDx() {
		return dx;
	}
	public int getDy() {
		return dy;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean checkCollision(Sprite other) {
		Rectangle myRect = new Rectangle();
		Rectangle otherRect = new Rectangle();
		myRect.setBounds(x,y,getWidth(),getHeight()); // 나의위치에 사각형생성
		otherRect.setBounds(other.getX(), other.getY(), other.getWidth(), other.getHeight()); // 적의위치에 사각형 생성
	
		return myRect.intersects(otherRect); //충돌여부 리턴
	}
	
	public void handleCollision(Sprite other) { // 충돌 이벤트처리 메소드
		
	}
}