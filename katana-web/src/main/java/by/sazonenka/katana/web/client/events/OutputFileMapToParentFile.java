package by.sazonenka.katana.web.client.events;

import java.util.List;

import by.sazonenka.katana.web.model.OutputFileModel;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Aliaksandr Sazonenka
 */
public final class OutputFileMapToParentFile {

  public static final class Event extends GwtEvent<Handler> {

    private static Type<Handler> type;

    public static Type<Handler> getType() {
      if (type == null) {
        type = new Type<Handler>();
      }
      return type;
    }

    private final List<OutputFileModel> files;
    private final OutputFileModel parent;

    public Event(List<OutputFileModel> files, OutputFileModel parent) {
      this.files = files;
      this.parent = parent;
    }

    public List<OutputFileModel> getFiles() {
      return files;
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
      handler.onMap(this);
    }

  }

  public interface Handler extends EventHandler {
    void onMap(Event event);
  }

  private OutputFileMapToParentFile() {
    /* Ensure non-instanciability. */
  }

}
