package api.core;

import config.FrameworkConfig;

public class BaseApiService {

    protected final RestClient restClient;

    public BaseApiService() {
        restClient = new RestClient(FrameworkConfig.APP_URL);
    }

    public BaseApiService(String url) {
        restClient = new RestClient(url);
    }
}
