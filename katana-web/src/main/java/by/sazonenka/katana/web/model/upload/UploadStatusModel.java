package by.sazonenka.katana.web.model.upload;

/**
 * @author Aliaksandr Sazonenka
 */
public final class UploadStatusModel implements UploadStatus {

  public static UploadStatusModel forSuccess() {
    return new UploadStatusModel(Status.SUCCESS, null);
  }

  public static UploadStatusModel forFailure(String description) {
    return new UploadStatusModel(Status.FAILURE, description);
  }

  private Status status;
  private String description;

  private UploadStatusModel(Status status, String description) {
    this.status = status;
    this.description = description;
  }

  @Override
  public Status getStatus() { return status; }
  @Override
  public void setStatus(Status status) { this.status = status; }

  @Override
  public String getDescription() { return description; }
  @Override
  public void setDescription(String description) { this.description = description; }

}
