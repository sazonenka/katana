package by.sazonenka.katana.xml.service;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

import javax.inject.Inject;

import org.simpleframework.xml.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.sazonenka.katana.xml.domain.ConstraintConfigXml;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;

/**
 * Implements saving and loading {@link ConstraintConfigXml}s.
 * <p>
 * Performs argument checking and delegates serializing and deserializing to the
 * {@link Serializer}.
 *
 * @author Aliaksandr Sazonenka
 */
@Service
public final class XmlPersisterImpl implements XmlPersister {

  private static final Logger LOG = LoggerFactory.getLogger(XmlPersisterImpl.class);

  private final Serializer serializer;

  @Inject
  public XmlPersisterImpl(Serializer serializer) {
    this.serializer = Preconditions.checkNotNull(serializer);
  }

  @Override
  public byte[] saveToBuffer(ConstraintConfigXml config) throws XmlPersisterException {
    Preconditions.checkArgument(config != null);

    try {
      ByteArrayOutputStream bufferOutputStream = new ByteArrayOutputStream();
      LOG.debug("Prepare to write the constraint config to a byte array.");

      serializer.write(config, bufferOutputStream);
      LOG.debug("Constraint config was written successfully.");

      return bufferOutputStream.toByteArray();
    } catch (Exception e) {
      LOG.error("Failed to write the constraint config to a byte array.", e);
      throw new XmlPersisterException("Failed to write the constraint config to a byte array.", e);
    }
  }

  @Override
  public String saveToString(ConstraintConfigXml config) throws XmlPersisterException {
    Preconditions.checkArgument(config != null);

    try {
      StringWriter buffer = new StringWriter();
      LOG.debug("Prepare to write the constraint config to a string.");

      serializer.write(config, buffer);
      LOG.debug("Constraint config was written successfully.");

      return buffer.toString();
    } catch (Exception e) {
      LOG.error("Failed to write the constraint config to a string.", e);
      throw new XmlPersisterException("Failed to write the constraint config to a string.", e);
    }
  }

  @Override
  public ConstraintConfigXml loadFromBuffer(byte[] buffer) throws XmlPersisterException {
    Preconditions.checkArgument(buffer != null);

    try {
      String bufferAsString = new String(buffer, Charsets.UTF_8);

      if (serializer.validate(ConstraintConfigXml.class, bufferAsString)) {
        ConstraintConfigXml config = serializer.read(ConstraintConfigXml.class, bufferAsString);
        LOG.debug("Constraint config was loaded successfully.");

        return config;
      } else {
        throw new XmlPersisterException("The document doesn't match expected XML schema.");
      }
    } catch (Exception e) {
      LOG.error("Failed to load a constraint config.", e);
      throw new XmlPersisterException("Failed to load a constraint config from the buffer.", e);
    }
  }

}
