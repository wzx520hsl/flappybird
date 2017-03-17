package com.usc.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	Texture gameover;
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	int state;
	float positionY = 0;
	float velocity = 0;
	int score = 0;
	int scoringTube = 0;
	int gameState = 0;
	float gravity = 1;
	Circle bird = new Circle();
	Texture topTube;
	Texture bottomTube;

	float gap;
	float maxOffset;
	Random randomGenerator;
	float[] tubeOffset = new float[4];
	float[] tubeX = new float[4];
	float tubeVelocity = 3;
	float distanceBetweenTubes;
	ShapeRenderer shapeRenderer;
	Rectangle[] topRectangles;
	Rectangle[] bottomRectangles;
	BitmapFont font;
	@Override
	public void create () {
		//shapeRenderer = new ShapeRenderer();
		gap = 400;
		state = 0;
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		gameover = new Texture("gameover.png");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		topRectangles = new Rectangle[4];
		bottomRectangles = new Rectangle[4];

		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

		start();
	}

	public void start(){
		positionY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		for (int i=0; i < 4; i++){
			if (i == 0){
				tubeOffset[i] = 0;
			}else {
				tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * maxOffset * 2;
			}

			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 +i*distanceBetweenTubes;

			topRectangles[i] = new Rectangle();
			bottomRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



		if (gameState == 1) {
			if (tubeX[scoringTube] < Gdx.graphics.getWidth()){
				score++;

				Gdx.app.log("Score", String.valueOf(score));
				if (scoringTube < 3){
					scoringTube ++;
				}else{
					scoringTube = 0;
				}
			}
			if (Gdx.input.justTouched()){

				Gdx.app.log("Touched","Oops!");
				gameState = 1;
				velocity = -20;

			}
			for (int i = 0; i < 4; i++ ) {

				if (tubeX[i] < -topTube.getWidth()){
					tubeX[i] = 4 * distanceBetweenTubes - topTube.getWidth();
				}else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topRectangles[i] = new Rectangle( tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
				bottomRectangles[i] = new Rectangle( tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());
			}

			if (positionY > 0 ){

				velocity = velocity + gravity;
				positionY -= velocity;
			}else {
				gameState = 2;
			}


		}else if(gameState == 0){

			if (Gdx.input.justTouched()){

				gameState = 1;
			}
		}else if(gameState == 2){

			batch.draw(gameover,Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2,Gdx.graphics.getHeight()/2 - gameover.getHeight() / 2);
			if (Gdx.input.justTouched()){

				gameState = 1;
				start();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
		}

		state = state == 0 ? 1 : 0;



		batch.draw(birds[state], Gdx.graphics.getWidth() / 2 - birds[state].getWidth() / 2, positionY);
		font.draw(batch, String.valueOf(score),100,200);
		bird.set(Gdx.graphics.getWidth() / 2, positionY + birds[state].getHeight() / 2, birds[state].getWidth() / 2);

		for (int i = 0;i < 4; i++){


			if (Intersector.overlaps(bird,topRectangles[i])||Intersector.overlaps(bird,bottomRectangles[i])){
				gameState = 2;
			}
		}
		batch.end();

		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
