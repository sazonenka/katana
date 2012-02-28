package by.sazonenka.katana.web.client.events;

import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ValidationRuleMoveDown {

  public static final class Event extends GwtEvent<Handler> {

    private static Type<Handler> type;

    public static Type<Handler> getType() {
      if (type == null) {
        type = new Type<Handler>();
      }
      return type;
    }

    private final ValidationRuleModel model;

    public Event(ValidationRuleModel model) {
      this.model = Preconditions.checkNotNull(model);
    }

    public ValidationRuleModel getModel() {
      return model;
    }

    @Override
    public Type<Handler> getAssociatedType() {
      return type;
    }

    @Override
    protected void dispatch(Handler handler) {
      handler.onMoveDown(this);
    }

  }

  public interface Handler extends EventHandler {
    void onMoveDown(Event event);
  }

  private ValidationRuleMoveDown() {
    /* Ensure non-instanciability. */
  }

}
