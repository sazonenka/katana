package by.sazonenka.katana.web.client.events;

import by.sazonenka.katana.web.model.OutputFileModel;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Aliaksandr Sazonenka
 */
public final class OutputFileSave {

  public static final class Event extends GwtEvent<Handler> {

    private static Type<Handler> type;

    public static Type<Handler> getType() {
      if (type == null) {
        type = new Type<Handler>();
      }
      return type;
    }

    private final OutputFileModel model;

    public Event(OutputFileModel model) {
      this.model = model;
    }

    public OutputFileModel getModel() {
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

  private OutputFileSave() {
    /* Ensure non-instanciability. */
  }

}
