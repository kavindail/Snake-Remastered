package org.Snake.UI.NotInGame;

import org.Snake.UI.UIComponent;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageAnimation extends UIComponent {

  private final PImage[] images;
  private int currentImageIndex;
  private final int frameInterval;
  private long lastFrameTime;

  public ImageAnimation(PApplet parent, float x, float y, float width, float height, String[] images, int frameInterval) {
    super(parent, x, y, width, height);
    this.images = new PImage[images.length];
    for (int i = 0; i < images.length; i++) {
      this.images[i] = parent.loadImage(images[i]);
    }
    this.frameInterval = frameInterval;
    this.currentImageIndex = 0;
    this.lastFrameTime = parent.millis();
  }

  @Override
  public void draw() {
    long currentTime = getParent().millis();
    if (currentTime - lastFrameTime >= frameInterval) {
      currentImageIndex = (currentImageIndex + 1) % images.length;
      lastFrameTime = currentTime;
    }
    getParent().image(images[currentImageIndex], x, y, width, height);
  }

  @Override
  public void mouseClicked(float mx, float my) {
    // do nothing
  }
}