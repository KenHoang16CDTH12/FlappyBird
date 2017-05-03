package kenhoang.hueic.it;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	//Image backgroud
	Texture background;
	//Image bird
	Texture[] birds;
	//Image tube
	Texture bottomTube;
	Texture topTube;
	//Image gameover
	Texture gameover;
	//font
	BitmapFont font;
	//Score
	int score = 0;
	int scoringTube = 0;
	//State bird
	int birdState = 0;
	//State game
	int gameState = 0;
	//Y
	float birdY = 0;
	//velocity
	float velocity = 0;
	//ShapeRenderer shapeRenderer;
	Circle birdCircle;
	float gravity = 2;
	//gap
	float gap = 400;
	//
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;

	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		//Tube
		bottomTube = new Texture("bottomtube.png");
		topTube = new Texture("toptube.png");
		gameover = new Texture("gameover.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];
		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(8);
		startGame();
	}
	public void startGame() {

		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		for (int i = 0; i < numberOfTubes; i++) {
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}
	@Override
	public void render () {
		batch.begin();
		//set background full screen
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (gameState == 1) {
			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
				score++;
				Gdx.app.log("Score : ", String.valueOf(score));
				if (scoringTube < numberOfTubes - 1) {
					scoringTube ++;
				} else {
					scoringTube = 0;
				}
			}
			if (Gdx.input.justTouched()) {
				velocity = -30;
			}
			for (int i = 0; i < numberOfTubes; i++) {
				if (tubeX[i] < -topTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}
				batch.draw(topTube, tubeX[i],
						Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i],
						Gdx.graphics.getHeight() / 2 - gap / 2 - topTube.getHeight() + tubeOffset[i]);
				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],
						topTube.getWidth(),
						topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],
						bottomTube.getWidth(),
						bottomTube.getHeight());
			}
			if (birdY > 0 ) {
				velocity += gravity;
				birdY -= velocity;
			} else {
				gameState = 2;
			}
		} else if (gameState == 0){
			//setDraw();
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else if (gameState == 2) {
			//Handle game over
			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2,
					Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);
			if (Gdx.input.justTouched()) {
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
		}
		if (birdState == 0) {
			birdState = 1;
		} else {
			birdState = 0;
		}
		//draw bird center screen
		batch.draw(birds[birdState],
				Gdx.graphics.getWidth() / 2 - birds[birdState].getWidth() / 2,
				birdY);
		//draw font
		font.draw(batch, String.valueOf(score), 100, 150);
		//finish batch
		batch.end();
		//Circle
		birdCircle.set(Gdx.graphics.getWidth() / 2,
				birdY + birds[birdState].getHeight() / 2,
				birds[birdState].getWidth() / 2);
		//ShapeRenderer
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);


		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		for (int i = 0; i < numberOfTubes; i++) {
			/*shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],
					topTube.getWidth(),
					topTube.getHeight());
			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],
					bottomTube.getWidth(),
					bottomTube.getHeight());*/
			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) ||
					Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
				gameState = 2;
			}
		}
		//shapeRenderer.end();
	}

	private void setDraw() {
		batch.begin();
		//set background full screen
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//draw bird center screen
		batch.draw(birds[birdState],
				Gdx.graphics.getWidth() / 2 - birds[birdState].getWidth() / 2,
				birdY);
		//finish batch
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
