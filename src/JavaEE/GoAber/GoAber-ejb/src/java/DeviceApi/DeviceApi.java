/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DeviceApi;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 *
 * @author Craig
 */
public class DeviceApi extends DefaultApi20
{
    private static final String AUTHORIZE_URL = "https://www.fitbit.com/oauth2/authorize/oauth?client_id=%s&redirect_uri=%s&scope=activity&response_type=code";

    @Override
    public String getAccessTokenEndpoint() {
       return "https://api.fitbit.com/oauth2/token?grant_type=authorization_code";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        
         Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");
          return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));        
    }
    
}
