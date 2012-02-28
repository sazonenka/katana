package by.sazonenka.katana.web.client.events;

import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ValidationRuleSave {

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
      this.model = model;
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
      handler.onSave(this);
    }

  }

  public interface Handler extends EventHandler {
    void onSave(Event event);
  }

  private ValidationRuleSave() {
    /* Ensure non-instanciability. */
  }

}
