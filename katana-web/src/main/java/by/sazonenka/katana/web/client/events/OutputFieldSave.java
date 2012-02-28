package by.sazonenka.katana.web.client.events;

import by.sazonenka.katana.web.model.OutputFieldModel;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Aliaksandr Sazonenka
 */
public final class OutputFieldSave {

  public static final class Event extends GwtEvent<Handler> {

    private static Type<Handler> type;

    public static Type<Handler> getType() {
      if (type == null) {
        type = new Type<Handler>();
      }
      return type;
    }

    private final OutputFieldModel model;

    public Event(OutputFieldModel model) {
      this.model = model;
    }

    public OutputFieldModel getModel() {
      return model;
    }

    @Override
    public Type<Handler> getAssociatedType() {
      return type;
    }

    @Override
    protected void dispatch(Handler handler) {
      handler.onSave(this);
    }

  }

  public interface Handler extends EventHandler {
    void onSave(Event event);
  }

  private OutputFieldSave() {
    /* Ensure non-instanciability. */
  }

}
