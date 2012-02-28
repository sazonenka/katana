package by.sazonenka.katana.persistence.interceptor;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs cross-cutting logging of DAO methods' calls.
 *
 * @author Aliaksandr Sazonenka
 */
public final class DaoMethodsLogger {

  private static final Logger LOG = LoggerFactory.getLogger("by.sazonenka.katana.persistence");

  /** Logs signature and arguments before a joinpoint executes. */
  public void logParameters(JoinPoint joinPoint) {
    LOG.debug("Entered method {}, arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
  }

  /** Logs signature and return value after a joinpoint has finished executing. */
  public void logReturnValue(JoinPoint joinPoint, Object returnValue) {
    LOG.debug("Method {} returns value: [{}]", joinPoint.getSignature(), returnValue);
  }

  /** Logs signature and exception only if a joinpoint threw an exception. */
  public void logException(JoinPoint joinPoint, Exception exception) {
    LOG.debug("Method {} throws {}", joinPoint.getSignature(), exception);
  }

}
