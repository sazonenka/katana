package by.sazonenka.katana.web.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * @author Aliaksandr Sazonenka
 */
public final class Icons {

  interface IconBundle extends ClientBundle {

    @Source("add-config.png")
    ImageResource addConfig();

    @Source("upload-config.png")
    ImageResource uploadConfig();

    @Source("open-config.png")
    ImageResource openConfig();

    @Source("download-config.png")
    ImageResource downloadConfig();

    @Source("edit-config.png")
    ImageResource editConfig();

    @Source("delete-config.png")
    ImageResource deleteConfig();

    @Source("warning-config.png")
    ImageResource warningConfig();

    @Source("add-item.png")
    ImageResource addItem();

    @Source("rename-item.png")
    ImageResource renameItem();

    @Source("unmap-item.png")
    ImageResource unmapItem();

    @Source("delete-item.png")
    ImageResource deleteItem();

    @Source("expand-all-items.png")
    ImageResource expandAllItems();

    @Source("collapse-all-items.png")
    ImageResource collapseAllItems();

    @Source("output-file.png")
    ImageResource outputFile();

    @Source("output-field.png")
    ImageResource outputField();

  }

  private final IconBundle BUNDLE = GWT.create(IconBundle.class);

  public AbstractImagePrototype addConfig() {
    return AbstractImagePrototype.create(BUNDLE.addConfig());
  }

  public AbstractImagePrototype uploadConfig() {
    return AbstractImagePrototype.create(BUNDLE.uploadConfig());
  }

  public AbstractImagePrototype openConfig() {
    return AbstractImagePrototype.create(BUNDLE.openConfig());
  }

  public AbstractImagePrototype downloadConfig() {
    return AbstractImagePrototype.create(BUNDLE.downloadConfig());
  }

  public AbstractImagePrototype editConfig() {
    return AbstractImagePrototype.create(BUNDLE.editConfig());
  }

  public AbstractImagePrototype deleteConfig() {
    return AbstractImagePrototype.create(BUNDLE.deleteConfig());
  }

  public AbstractImagePrototype warningConfig() {
    return AbstractImagePrototype.create(BUNDLE.warningConfig());
  }

  public AbstractImagePrototype addItem() {
    return AbstractImagePrototype.create(BUNDLE.addItem());
  }

  public AbstractImagePrototype renameItem() {
    return AbstractImagePrototype.create(BUNDLE.renameItem());
  }

  public AbstractImagePrototype unmapItem() {
    return AbstractImagePrototype.create(BUNDLE.unmapItem());
  }

  public AbstractImagePrototype deleteItem() {
    return AbstractImagePrototype.create(BUNDLE.deleteItem());
  }

  public AbstractImagePrototype expandAllItems() {
    return AbstractImagePrototype.create(BUNDLE.expandAllItems());
  }

  public AbstractImagePrototype collapseAllItems() {
    return AbstractImagePrototype.create(BUNDLE.collapseAllItems());
  }

  public AbstractImagePrototype outputFile() {
    return AbstractImagePrototype.create(BUNDLE.outputFile());
  }

  public AbstractImagePrototype outputField() {
    return AbstractImagePrototype.create(BUNDLE.outputField());
  }

}
