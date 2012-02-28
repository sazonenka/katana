package by.sazonenka.katana.web.client.events;

import java.util.List;

import by.sazonenka.katana.web.model.OutputFieldModel;
import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Aliaksandr Sazonenka
 */
public final class OutputFieldMapToRule {

  public static final class Event extends GwtEvent<Handler> {

    private static Type<Handler> type;

    public static Type<Handler> getType() {
      if (type == null) {
        type = new Type<Handler>();
      }
      return type;
    }

    private final List<OutputFieldModel> fields;
    private final ValidationRuleModel rule;

    public Event(List<OutputFieldModel> fields, ValidationRuleModel rule) {
      this.fields = fields;
      this.rule = rule;
    }

    public List<OutputFieldModel> getFields() {
      return fields;
    }

    public ValidationRuleModel getRule() {
      return rule;
    }

    @Override
    public Type<Handler> getAssociatedType() {
      return type;
    }

    @Override
    protected void dispatch(Handler handler) {
      handler.onMap(this);
    }

  }

  public interface Handler extends EventHandler {
    void onMap(Event event);
  }

  private OutputFieldMapToRule() {
    /* Ensure non-instanciability. */
  }

}
