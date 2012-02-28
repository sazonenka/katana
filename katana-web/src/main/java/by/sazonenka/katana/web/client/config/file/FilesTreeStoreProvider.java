package by.sazonenka.katana.web.client.config.file;

import java.util.List;

import by.sazonenka.katana.web.client.config.ConfigDictionary;
import by.sazonenka.katana.web.client.events.util.GenericAsyncCallback;
import by.sazonenka.katana.web.client.managers.OutputFieldManagerAsync;
import by.sazonenka.katana.web.client.managers.OutputFileManagerAsync;
import by.sazonenka.katana.web.model.FilesTreeModel;
import by.sazonenka.katana.web.model.FilesTreeModel.Type;
import by.sazonenka.katana.web.model.OutputFieldModel;
import by.sazonenka.katana.web.model.OutputFileModel;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Aliaksandr Sazonenka
 */
public final class FilesTreeStoreProvider implements Provider<TreeStore<FilesTreeModel>> {

  private final ConfigDictionary dictionary;
  private final OutputFileManagerAsync fileManager;
  private final OutputFieldManagerAsync fieldManager;

  @Inject
  public FilesTreeStoreProvider(ConfigDictionary dictionary, OutputFileManagerAsync fileManager,
      OutputFieldManagerAsync fieldManager) {
    this.dictionary = Preconditions.checkNotNull(dictionary);
    this.fileManager = Preconditions.checkNotNull(fileManager);
    this.fieldManager = Preconditions.checkNotNull(fieldManager);
  }

  @Override
  public TreeStore<FilesTreeModel> get() {
    RpcProxy<List<FilesTreeModel>> proxy = createProxy();
    TreeLoader<FilesTreeModel> loader = createLoader(proxy);

    TreeStore<FilesTreeModel> store = new TreeStore<FilesTreeModel>(loader);
    store.setKeyProvider(new ModelKeyProvider<FilesTreeModel>() {
      @Override
      public String getKey(FilesTreeModel model) {
        return "node_" + model.getClass().getName() + "_" + model.getId();
      }
    });
    return store;
  }

  private RpcProxy<List<FilesTreeModel>> createProxy() {
    return new RpcProxy<List<FilesTreeModel>>() {
      @Override
      protected void load(Object loadConfig, final AsyncCallback<List<FilesTreeModel>> callback) {
        if (loadConfig == null) {
          fileManager.findByConfig(dictionary.getConfigId(),
              new GenericAsyncCallback<List<OutputFileModel>>() {
                @Override
                public void onSuccess(List<OutputFileModel> result) {
                  callback.onSuccess(Lists.<FilesTreeModel>newArrayList(result));
                }
              });
        } else {
          OutputFileModel file = (OutputFileModel) loadConfig;
          fieldManager.findByFile(file.getId(),
              new GenericAsyncCallback<List<OutputFieldModel>>() {
                @Override
                public void onSuccess(List<OutputFieldModel> result) {
                  callback.onSuccess(Lists.<FilesTreeModel>newArrayList(result));
                }
              });
        }
      }
    };
  }

  private TreeLoader<FilesTreeModel> createLoader(RpcProxy<List<FilesTreeModel>> proxy) {
    return new BaseTreeLoader<FilesTreeModel>(proxy) {
      @Override
      public boolean hasChildren(FilesTreeModel parent) {
        return parent.getType() == Type.FILE;
      }
    };
  }

}
