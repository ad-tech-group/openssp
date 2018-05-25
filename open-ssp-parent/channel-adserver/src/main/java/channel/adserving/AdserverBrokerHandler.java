package channel.adserving;

import com.atg.openssp.common.configuration.ContextCache;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.core.connector.JsonGetConnector;
import com.google.gson.Gson;
import org.apache.http.client.utils.URIBuilder;

import java.nio.charset.StandardCharsets;

public abstract class AdserverBrokerHandler {
    private final URIBuilder uriBuilder;
    private final JsonGetConnector jsonGetConnector;
    private final Gson gson;

    public AdserverBrokerHandler() {
        String scheme = ContextCache.instance.get(ContextProperties.ADSERVER_PROVIDER_SCHEME);
        String host = ContextCache.instance.get(ContextProperties.ADSERVER_PROVIDER_HOST);
        int port = Integer.parseInt(ContextCache.instance.get(ContextProperties.ADSERVER_PROVIDER_PORT));
        String path = ContextCache.instance.get(ContextProperties.ADSERVER_PROVIDER_VIDEO_PATH);
        uriBuilder = new URIBuilder().setCharset(StandardCharsets.UTF_8).setScheme(scheme).setHost(host).setPort(port).setPath(path);

        jsonGetConnector = new JsonGetConnector();
        gson = new Gson();
    }

    protected JsonGetConnector getConnector() {
        return jsonGetConnector;
    }

    protected Gson getGson() {
        return gson;
    }

    public URIBuilder getUriBuilder() {
        return uriBuilder;
    }

    public abstract Class<? extends AdservingCampaignProvider> getProvider();
}
