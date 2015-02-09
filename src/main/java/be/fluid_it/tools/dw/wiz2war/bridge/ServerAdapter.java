package be.fluid_it.tools.dw.wiz2war.bridge;

import io.dropwizard.setup.Environment;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

// Adapts a Jetty server to a 3.0 servlet context
public class ServerAdapter extends Server {
    private Logger logger = LoggerFactory.getLogger(ServerAdapter.class);

    private final Environment environment;

    public ServerAdapter(Environment environment) {
        this.environment = environment;
    }

    // Server connectors

    private final List<Connector> connectors = new LinkedList<Connector>();

    @Override
    public Connector[] getConnectors() {
        synchronized (connectors) {
            return connectors.toArray(new Connector[connectors.size()]);
        }
    }

    @Override
    public void addConnector(Connector connector) {
        synchronized (connectors) {
            connectors.add(connector);
        }
    }

    @Override
    public void removeConnector(Connector connector) {
        connectors.remove(connector);
    }

    // Server attributes

    private final Map<String, Object> attributesByName = new HashMap<String, Object>();

    @Override
    public void clearAttributes() {
        synchronized (attributesByName) {
            attributesByName.clear();
        }
    }

    @Override
    public Object getAttribute(String name) {
        synchronized (attributesByName) {
            return attributesByName.get(name);
        }
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        synchronized (attributesByName) {
            return new Vector(attributesByName.keySet()).elements();
        }
    }

    @Override
    public void removeAttribute(String name) {
        synchronized (attributesByName) {
            attributesByName.remove(name);
        }
    }

    @Override
    public void setAttribute(String name, Object attribute) {
        synchronized (attributesByName) {
            attributesByName.put(name, attribute);
        }
    }

    // Handle methods are not supposed to be called

    @Override
    public void handle(HttpChannel<?> connection) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleAsync(HttpChannel<?> connection) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    // Handlers
    private final List<Handler> handlers = new LinkedList<Handler>();

    @Override
    public Handler getHandler() {
        synchronized (handlers) {
            return handlers.size() > 0 ? handlers.get(0) : null;
        }
    }

    @Override
    public Handler[] getHandlers() {
        synchronized (handlers) {
            return handlers.toArray(new Handler[handlers.size()]);
        }
    }

    @Override
    public void setHandler(Handler handler) {
        logger.info("Set handler ", handler.getClass().getName());
        synchronized (handlers) {
            handlers.add(handler);
        }
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    // Server Lifecycle

    private enum ServerState {
        FAILED, STARTING, STARTED, STOPPING, STOPPED;
    }

    private ServerState serverState = ServerState.STARTED;

    @Override
    public boolean isRunning() {
        return STARTED.equals(serverState);
    }

    @Override
    public boolean isStarted() {
        return STARTED.equals(serverState);
    }

    @Override
    public boolean isStarting() {
        return STARTING.equals(serverState);
    }

    @Override
    public boolean isStopping() {
        return STOPPING.equals(serverState);
    }

    @Override
    public boolean isStopped() {
        return STOPPED.equals(serverState);
    }

    @Override
    public boolean isFailed() {
        return FAILED.equals(serverState);
    }

    private final List<LifeCycle.Listener> lifeCycleListeners = new LinkedList<LifeCycle.Listener>();

    @Override
    public void addLifeCycleListener(LifeCycle.Listener listener) {
        synchronized (lifeCycleListeners) {
            lifeCycleListeners.add(listener);
        }
    }

    @Override
    public void removeLifeCycleListener(LifeCycle.Listener listener) {
        synchronized (lifeCycleListeners) {
            lifeCycleListeners.remove(listener);
        }
    }

    @Override
    public String getState() {
        return serverState != null ? serverState.name() : null;
    }

    @Override
    public long getStopTimeout() {
        return 0;
    }


    // Start

    @Override
    protected void doStart() throws Exception {
        logger.info("Dummy start Jetty server ...");
    }

    @Override
    protected void doStop() throws Exception {
        logger.info("Dummy stop Jetty server ...");
    }

    // TODO
    // UnsupportedOperationException

}