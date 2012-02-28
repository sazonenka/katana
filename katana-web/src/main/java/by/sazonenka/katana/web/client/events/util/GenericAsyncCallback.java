package by.sazonenka.katana.web.client.events.util;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Aliaksandr Sazonenka
 */
public class GenericAsyncCallback<T> implements AsyncCallback<T> {

  @Override
  public void onSuccess(T result) {
  }

  @Override
  public void onFailure(Throwable caught) {
    ErrorBalloon.display(caught.getMessage());
  }

}
