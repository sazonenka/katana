package by.sazonenka.katana.web.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ConstraintConfigUpload {

  public static final class Event extends GwtEvent<Handler> {

    private static Type<Handler> type;

    public static Type<Handler> getType() {
      if (type == null) {
        type = new Type<Handler>();
      }
      return type;
    }

    @Override
    public Type<Handler> getAssociatedType() {
      return type;
    }

    @Override
    protected void dispatch(Handler handler) {
      handler.onUpload(this);
    }

  }

  public interface Handler extends EventHandler {
    void onUpload(Event event);
  }

  private ConstraintConfigUpload() {
    /* Ensure non-instanciability. */
  }

}
