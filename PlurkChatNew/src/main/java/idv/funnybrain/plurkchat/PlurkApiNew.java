package idv.funnybrain.plurkchat;

import org.scribe.builder.api.DefaultApi10a;
//import org.scribe.builder.api.PlurkApi;
import org.scribe.model.Token;

public class PlurkApiNew extends DefaultApi10a {
    private static final String REQUEST_TOKEN_URL = "https://www.plurk.com/OAuth/request_token";
    private static final String AUTHORIZATION_URL = "https://www.plurk.com/OAuth/authorize?oauth_token=%s";
    private static final String ACCESS_TOKEN_URL = "https://www.plurk.com/OAuth/access_token";

    @Override
    public String getRequestTokenEndpoint()
    {
        return REQUEST_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken)
    {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }

    @Override
    public String getAccessTokenEndpoint()
    {
        return ACCESS_TOKEN_URL;
    }

    public static class Mobile extends PlurkApiNew
    {
        private static final String AUTHORIZATION_URL = "https://www.plurk.com/m/authorize?oauth_token=%s";

        @Override
        public String getAuthorizationUrl(Token requestToken)
        {
            return String.format(AUTHORIZATION_URL, requestToken.getToken());
        }
    }
}
