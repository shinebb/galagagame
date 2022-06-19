package kh0211_2;
import java.io.File;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Font;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


public class GalagaGame extends JPanel implements KeyListener {
	private boolean running; //게임 진행 상태
	private ArrayList sprites;
	private Sprite starship;
	private Sprite alien;
	private Sprite alienbox[] = new Sprite[255];
	private Sprite enemyShot;
	private Sprite boss;
	private boolean bossSummon=false;
	private boolean bossDie=false;
	
	private JLabel scoreLabel;
	private int scoreNum=0;

	
	private JFrame frame;
	private int arr=0; //alienbox
	
	private BufferedImage alienImage;
	private BufferedImage shotImage;
	private BufferedImage shipImage;
	private BufferedImage bossImage;
	private BufferedImage enemyShotImage;
	private BufferedImage background;
	private BufferedImage boomImage;
	
	private boolean fireOn;
	
	private boolean test = false;
	private int display = 1;
	
	public GalagaGame() {
		running=true; //게임진행상태
		fireOn=true;
		sprites = new ArrayList();
		scoreLabel = new JLabel("Score : "+scoreNum); //스코어 라벨 객체 생성
		scoreLabel.setFont(new Font("Serif", Font.BOLD, 50));
		scoreLabel.setLayout(null);
		scoreLabel.setForeground(Color.ORANGE);
		scoreLabel.setVerticalAlignment(SwingConstants.TOP);
		scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);
		

		frame = new JFrame("Galaga Game");
		
		frame.setSize(800,800);
		frame.add(this); //this 본인의 생성자
		frame.setResizable(false); // 창크기 조절불가
		frame.setVisible(true); //화면에 띄움
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //x버튼 프로그램 종료
		frame.add(scoreLabel);
		
		
		try {
			background = ImageIO.read(new File("image/background.png"));
			shotImage = ImageIO.read(new File("image/fire.png"));
			shipImage = ImageIO.read(new File("image/starship.png"));
			alienImage = ImageIO.read(new File("image/alien.png"));
			bossImage = ImageIO.read(new File("image/boss.png"));
			boomImage = ImageIO.read(new File("image/boom.png"));
			enemyShotImage = ImageIO.read(new File("image/enemyshot.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		this.requestFocus(); //포커스 가져오기
		
		this.initSprites();
		
		addKeyListener(this); //키보드 이벤트 발생
		audioPlay("sound/background_music.wav");
	}
	private void initSprites() { //초기 설정 메소드
		sprites.clear();
		scoreNum=0;
		bossSummon = false;
		starship = new StarShipSprite(this, shipImage, 300,600);
		sprites.add(starship);
		
		int i=0;
		
		
		for(int y = 0; y < 5; y ++) { //5행
			for(int x = 0; x < 12; x++) { //12열
				alien = new AlienSprite(this, alienImage, 100 + (x * 50), (50) + y * 30);
				alienbox[i] = alien;
				sprites.add(alienbox[i]);
				i++;
			}
		}
		repaint();
	}
	
	public void audioPlay(String fileName) { //오디오 배경음악
		try {
			AudioInputStream file = AudioSystem.getAudioInputStream(new File(fileName));
			Clip clip = AudioSystem.getClip();
			clip.open(file);
			if(fileName != "sound/fire.wav") clip.loop(Clip.LOOP_CONTINUOUSLY);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-13.0f); // 볼륨 13 감소
			clip.start();
		} catch (Exception e) {}
	}
	public void gameEnd() {
		test = false; // test false로 변경
		display = 2; // display 2로 변경
	}
	public void removeSprite(Sprite sprite) { //스프라이트 제거
		sprite.hp--; //0으로 초기 설정되어있음
		if (sprite.hp <= 0) {
			if(sprite instanceof ShotSprite || sprite instanceof EnemyShotSprite ) {
				sprites.remove(sprite);
				return;
			}
			sprite.image = boomImage;

			Timer scheduler = new Timer();
	        TimerTask task = new TimerTask() {
	        	@Override  	
	            public void run() {
	        		if(sprite instanceof AlienSprite) {
	        			sprites.remove(sprite);      			
	        		}
	        		if(sprite instanceof StarShipSprite) {
	        			gameEnd();
	    				sprite.image = shipImage;
	    			}
	            }
	        };
	        scheduler.schedule(task, 100);
		}
	}
	
	public void fire() { //아군미사일
		audioPlay("sound/fire.wav");
		ShotSprite shot = new ShotSprite(this, shotImage, starship.getX(), starship.getY() - 30);
		sprites.add(shot);
		fireOn=false;
		delay(250);
	}
	
	public void delay(int time) { //아군미사일 딜레이
		Timer scheduler = new Timer();
        TimerTask task = new TimerTask() {
        	@Override  	
            public void run() {
        		fireOn=true;
            }
        };
        scheduler.schedule(task, time);
    }
	
	@Override
	public void paint(Graphics g) {
		if(test == true) {
		super.paint(g); 
		g.drawImage(background,0,0,null);
		for (int i =0; i<sprites.size(); i++) {
			Sprite sprite = (Sprite) sprites.get(i);
			sprite.draw(g);
		}
		scoreLabel.paint(g);
		
		if(sprites.contains(boss)) { //BOSS HP 우측상단 표기
			g.setColor(Color.WHITE);
			g.drawString("BOSS HP : " + (int)boss.hp, 450, 50);
		}
		
		} else { 
			switch(display) {
			
			case 1:
				g.drawImage(background,0,0,null);
				g.setColor(Color.WHITE);
				g.setFont(new Font("굴림", Font.BOLD, 20));
				g.drawString("Space Invader", 300, 200);
				g.drawString("이동 : 화살표 / 공격 : 스페이스바", 200, 300);
				g.drawString("모든 적을 처치하세요 !", 270, 400);
				g.drawString("게임을 시작하려면 Enter를 눌러주세요", 200, 500);
				g.drawString("종료 : Esc", 300, 600);
				break;
			
			case 2:
				g.drawImage(background,0,0,null);
				g.setColor(Color.WHITE);
				g.setFont(new Font("굴림", Font.BOLD, 20));
				g.drawString("최종 Score " + scoreNum + " 게임을 다시 시작하려면 Enter를 눌러주세요.", 100, 300);
				g.drawString("종료 : Esc", 300, 400);
				break;
			}
		  }
		}
	
	public boolean alive_Alien_check() {
		for(int i=0; i<sprites.size(); i++) {
			if(sprites.get(i).getClass().getSimpleName().equals("AlienSprite")) {
				return true;
			}
		}
		return false;
	}
	
	public void enemyShot() { //적기 미사일 랜덤발사
		Random rand = new Random();
		int i = rand.nextInt(60);
		int j = rand.nextInt(2);
		Sprite enemyShot = new EnemyShotSprite(this, enemyShotImage, alienbox[i].getX(), alienbox[i].getY());
			
		if(sprites.contains(boss) && j==1) { //보스가 생성되면 보스 미사일 쏨
			Sprite bossShot = new EnemyShotSprite(this, enemyShotImage, boss.getX(), boss.getY()+100);
			Sprite bossShot1 = new EnemyShotSprite(this, enemyShotImage, boss.getX()+50, boss.getY()+100);
			Sprite bossShot2 = new EnemyShotSprite(this, enemyShotImage, boss.getX()+100, boss.getY()+100);
			sprites.add(bossShot);
			sprites.add(bossShot1);
			sprites.add(bossShot2);
		}
//		while(!sprites.contains(alienbox[i])) { //살아있는 적기 검출
//			i = rand.nextInt(60);
//		}
		if(sprites.contains(alienbox[i])) { //살아있는 적기만 미사일 쏨
			//test == true 안하면 true시 미사일 쌓인거 한꺼번에 날라옴
			sprites.add(enemyShot);	
		}
	}
	
	public void enemyShotDealy() {	//적기 미사일 딜레이(속도)
		Timer scheduler = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				enemyShot();
				
			}
		};
		scheduler.scheduleAtFixedRate(task, 500, 500);
	}
	
	public void gameLoop() {
		enemyShotDealy();
		while(running) {
			for(int p=0; p<sprites.size();p++) {
				for(int s = p+1; s<sprites.size(); s++) {
					Sprite me = (Sprite) sprites.get(p);
					// s번째 인덱스부터의 스프라이트를 가져옴
					Sprite other = (Sprite) sprites.get(s);
					// s+1번째 인덱스부터의 스프라이트 가져옴
					if (me.checkCollision(other)) { // && (me.image != boomImage || other.image != boomImage)
						me.handleCollision(other);
						other.handleCollision(me);
						scoreLabel.setText("Score : " + scoreNum);
						//bossHpLabel.setSize(hpNum, 20);
						
					}

					if(!alive_Alien_check() && !bossSummon && test) { //보스생성
						boss = new AlienSprite(this, bossImage, 100,0, 50);
						sprites.add(boss);
						enemySummon_Delay();
						bossSummon=true;
					}
					if(!alive_Alien_check() && bossSummon) gameEnd();
				}
			}
			if(test == true) {
			for(int i=0;i<sprites.size();i++) {
			//ArrayList인 sprites의 모든 요소를 Sprite타입(슈퍼클래스)로 변환함
				Sprite sprite = (Sprite) sprites.get(i);
				sprite.move();
			} 
			}
			repaint();
			try {
				Thread.sleep(10);
			}catch(Exception e) {}
		}
	}
	
	public void enemySummon() { //추가 적기
		for (int x = 0; x < 10; x++) {
			alien = new AlienSprite(this, alienImage, 100 + (x * 80), 50); // 에일리언 생성
			//sprites.add(alien);
			alienbox[arr*10+x] = alien;
			sprites.add(alienbox[arr*10+x]);
		}
		arr ++;
	}
	
	public void enemySummon_Delay() { //추가적기 딜레이
		Timer scheduler = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if(sprites.contains(boss)) enemySummon();
				else scheduler.cancel();
			}
		};
		scheduler.scheduleAtFixedRate(task, 10000, 10000);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(test) {
			if(e.getKeyCode() == KeyEvent.VK_LEFT) starship.setDx(-3);
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) starship.setDx(3);
			if(e.getKeyCode() == KeyEvent.VK_UP) starship.setDy(-3);
			if(e.getKeyCode() == KeyEvent.VK_DOWN) starship.setDy(3);
			if(e.getKeyCode() == KeyEvent.VK_SPACE) if (fireOn) fire();
		}
		else {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				test=true; 
				initSprites();
			}
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) starship.setDx(0);
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) starship.setDx(0);
		if(e.getKeyCode() == KeyEvent.VK_UP) starship.setDy(0);
		if(e.getKeyCode() == KeyEvent.VK_DOWN) starship.setDy(0);
		if(e.getKeyCode() == KeyEvent.VK_SPACE);
	}
	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	public void scoreUp() {
		scoreNum++;
	}

	public static void main(String[] args) {
		GalagaGame g = new GalagaGame();
		g.gameLoop();
	}
}