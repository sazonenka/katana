package by.sazonenka.katana.web.client.events;

import java.util.List;

import by.sazonenka.katana.web.model.ConstraintConfigModel;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ConstraintConfigLoad {

  public static final class Event extends GwtEvent<Handler> {

    private static Type<Handler> type;

    public static Type<Handler> getType() {
      if (type == null) {
        type = new Type<Handler>();
      }
      return type;
    }

    private final AsyncCallback<List<ConstraintConfigModel>> callback;

    public Event(AsyncCallback<List<ConstraintConfigModel>> callback) {
      this.callback = callback;
    }

    public AsyncCallback<List<ConstraintConfigModel>> getCallback() {
      return callback;
    }

    @Override
    public Type<Handler> getAssociatedType() {
      return type;
    }

    @Override
    protected void dispatch(Handler handler) {
      handler.onLoad(this);
    }

  }

  public interface Handler extends EventHandler {
    void onLoad(Event event);
  }

  private ConstraintConfigLoad() {
    /* Ensure non-instanciability. */
  }

}
