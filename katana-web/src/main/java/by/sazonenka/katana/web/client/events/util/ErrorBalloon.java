package by.sazonenka.katana.web.client.events.util;

import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ErrorBalloon extends ContentPanel {

  private static ErrorBalloon BALLOON = null;

  public static void display(String errorMessage) {
    if (BALLOON == null) {
      BALLOON = new ErrorBalloon();
    }

    if (!BALLOON.isVisible()) {
      BALLOON.config.text = errorMessage;
      BALLOON.showBalloon();
    }
  }

  private final InfoConfig config;

  private ErrorBalloon() {
    baseStyle = "x-info";
    frame = true;
    setShadow(true);
    setLayoutOnChange(true);

    config = new InfoConfig("Error", "");
    config.width = 800;
    config.height = 75;
    config.display = 25000;
  }

  private void showBalloon() {
    RootPanel.get().add(this);
    el().makePositionable(true);

    setTitle();
    setText();

    Point p = position();
    el().setLeftTop(p.x, p.y);
    setWidth(config.width);
    setAutoHeight(true);

    Timer timer = new Timer() {
      @Override
      public void run() {
        hide();
      }
    };
    timer.schedule(config.display);
  }

  @Override
  public void hide() {
    super.hide();
    RootPanel.get().remove(this);
  }

  private void setTitle() {
    if (config.title != null) {
      head.setVisible(true);
      setHeading(config.title);
    } else {
      head.setVisible(false);
    }
  }

  private void setText() {
    if (config.text != null) {
      removeAll();
      addText(config.text);
    }
  }

  private Point position() {
    Size s = XDOM.getViewportSize();

    int left = (s.width - config.width) / 2;
    int top = 10;

    return new Point(left, top);
  }

}
