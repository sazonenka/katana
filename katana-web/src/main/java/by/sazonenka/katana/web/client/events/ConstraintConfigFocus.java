package by.sazonenka.katana.web.client.events;

import by.sazonenka.katana.web.model.ConstraintConfigModel;

import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ConstraintConfigFocus {

  public static final class Event extends GwtEvent<Handler> {

    private static Type<Handler> type;

    public static Type<Handler> getType() {
      if (type == null) {
        type = new Type<Handler>();
      }
      return type;
    }

    private final ConstraintConfigModel model;

    public Event(ConstraintConfigModel model) {
      this.model = Preconditions.checkNotNull(model);
    }

    public ConstraintConfigModel getModel() {
      return model;
    }

    @Override
    public Type<Handler> getAssociatedType() {
      return type;
    }

    @Override
    protected void dispatch(Handler handler) {
      handler.onFocus(this);
    }

  }

  public interface Handler extends EventHandler {
    void onFocus(Event event);
  }

  private ConstraintConfigFocus() {
    /* Ensure non-instanciability. */
  }

}
