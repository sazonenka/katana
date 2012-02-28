package by.sazonenka.katana.web.model.upload;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Aliaksandr Sazonenka
 */
public final class UploadStatusJso extends JavaScriptObject implements UploadStatus {

  protected UploadStatusJso() {
  }

  @Override
  public native Status getStatus() /*-{ return this.status; }-*/;
  @Override
  public native void setStatus(Status status) /*-{ this.status = status; }-*/;

  @Override
  public native String getDescription() /*-{ return this.description; }-*/;
  @Override
  public native void setDescription(String description) /*-{ this.description = description; }-*/;

  public static final native UploadStatusJso build(String json) /*-{
    return eval('(' + json + ')');
  }-*/;

}
