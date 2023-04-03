package org.Snake.UI.NotInGame.Pages;
import org.Snake.UI.NotInGame.NotInGameUiManager;
import org.Snake.UI.Frame;
import org.Snake.UI.NotInGame.LevelButton;
import processing.core.PApplet;
import org.Snake.Database.KVPair;

import org.Snake.UI.Text;

import org.Snake.Database.MongoDb;

import java.util.ArrayList;
import java.util.Locale;

public class HighScoreBoard extends Frame {

  private final NotInGameUiManager uiManager;

  private ArrayList<KVPair> scores;

  private Text[] scoreTexts;

  public HighScoreBoard(PApplet parent, float x, float y, float width, float height, NotInGameUiManager uiManager) {
    super(parent, x, y, width, height, 0, "");
    this.uiManager = uiManager;
    scores = new ArrayList<>();
  }

  public void getScores() {
    System.out.println("Getting scores called");
    MongoDb db = new MongoDb();
    scores = db.get(uiManager.getSelectedLevel());
    for (KVPair score : scores) {
      System.out.println(score.getKey() + " - " + score.getValue());
    }

    scoreTexts = new Text[scores.size()];
    for (int i = 0; i < scores.size(); i++) {
      scoreTexts[i] = new Text(parent, x + 100, y + 100 + (i * 50), scores.get(i).getKey() + " - " + scores.get(i).getValue());
    }
  }

  @Override
  public void draw() {
    // Draw HighScores title
    Text highScoresTitle = new Text(parent, x + width/2, y + height/10, "HighScores");
    highScoresTitle.setTextSize(32);
    highScoresTitle.setTextColor(parent.color(255));
    highScoresTitle.draw();

    // Draw level name
    Text levelName = new Text(parent, x + width/2, y + height/5, uiManager.getSelectedLevel());
    levelName.setTextSize(24);
    levelName.setTextColor(parent.color(255));
    levelName.draw();

    // Draw scores
    float scoreY = y + height/2;
    for (KVPair score : scores) {
      String scoreText = score.getKey() + " - " + score.getValue();
      Text scoreItem = new Text(parent, x + width/2, scoreY, scoreText);
      scoreItem.setTextSize(20);
      scoreItem.setTextColor(parent.color(255));
      scoreItem.draw();
      scoreY += 30;
    }
  }


  @Override
  public void mouseClicked(float mx, float my) {
    System.out.print("mouse clicked in highscoreboard");
  }
}
