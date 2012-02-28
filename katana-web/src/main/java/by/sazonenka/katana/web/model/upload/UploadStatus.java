package by.sazonenka.katana.web.model.upload;

import com.google.gwt.core.client.SingleJsoImpl;

/**
 * @author Aliaksandr Sazonenka
 */
@SingleJsoImpl(UploadStatusJso.class)
public interface UploadStatus {

  Status getStatus();
  void setStatus(Status status);

  String getDescription();
  void setDescription(String description);

  enum Status { SUCCESS, FAILURE }

}
