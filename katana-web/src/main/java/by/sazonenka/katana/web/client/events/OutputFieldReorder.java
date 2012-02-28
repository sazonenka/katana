package by.sazonenka.katana.web.client.events;

import by.sazonenka.katana.web.model.OutputFileModel;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Aliaksandr Sazonenka
 */
public final class OutputFieldReorder {

  public static final class Event extends GwtEvent<Handler> {

    private static Type<Handler> type;

    public static Type<Handler> getType() {
      if (type == null) {
        type = new Type<Handler>();
      }
      return type;
    }

    private final OutputFileModel parent;

    public Event(OutputFileModel parent) {
      this.parent = parent;
    }

    public OutputFileModel getParent() {
      return parent;
    }

    @Override
    public Type<Handler> getAssociatedType() {
      return type;
    }

    @Override
    protected void dispatch(Handler handler) {
      handler.onReorder(this);
    }

  }

  public interface Handler extends EventHandler {
    void onReorder(Event event);
  }

  private OutputFieldReorder() {
    /* Ensure non-instanciability. */
  }

}
