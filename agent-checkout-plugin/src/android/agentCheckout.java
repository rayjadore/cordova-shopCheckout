package cordova_agent_checkout;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import co.tradedepot.shop.sdk.checkout.Checkout;
import co.tradedepot.shop.sdk.checkout.Registration;


/**
 * This class echoes a string called from JavaScript.
 */
public class agent_checkout extends CordovaPlugin {

    
    @Override protected void pluginInitialize() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                setUpAgentCheckout();
            }
        });
    }

    @Override public void onStart() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                //We also initialize agentCheckout here just in case it has died. 
                setUpAgentCheckout();
                Intercom.client().handlePushMessage();
            }
        });
    }

    @Override public void onNewIntent(Intent intent) {
        cordova.getActivity().setIntent(intent);
    }

    private void setUpAgentCheckout() {
        try {
            //Get app credentials from config.xml or the app bundle if they can't be found
            String apiKey = preferences.getString("agentCheckout-android-api-key", "");

            Intercom.initialize(cordova.getActivity().getApplication(), apiKey, appId);
        } catch (Exception e) {
            Log.e("agentCheckout-Cordova", "ERROR: Something went wrong when initializing agentCheckout. Have you set your AGENTCHECKOUT_ANDROID_API_KEY?", e);
        }
    }

    private enum Action {
        registerAgent {
        @Override void performAction(JSONArray args, CallbackContext callbackContext, CordovaInterface cordova) {
        try {
            JSONObject options = args.optJSONObject(0);
            if((options.optString("agentId") != null && options.optString("agentId").length() > 0) && (options.optString("firstName") != null && options.optString("firstName").length > 0) &&
                    (options.optString("lastName") && != null && options.optString("lastName").length() > 0) && (options.optString("phoneNumber") != null && options.optString("phoneNumber").length() > 0 )
                    && (options.optString("countryCode") != null && options.optString("countryCode").length() > 0) &&
                    (options.optDouble("longitude") != null) && (options.optDouble("latitude") != null) && (options.optString("company") != null && options.optString("company").length() > 0) &&
                    (options.optString("address") != null && options.optString("address").length() > 0)) {
              Registration registration = Registration.Builder.create()
                      .agentId(options.optString("agentId"))
                      .firstName(options.optString("firstName"))
                      .lastName(options.optString("lastName"))
                      .phoneNumber(options.optString("phoneNumber"))
                      .countryCode(options.optString("countryCode"))
                      .company(options.optString("company"))
                      .latitude(options.opttDouble("latitude"))
                      .longitude(options.optDouble("longitude"))
                      .address(options.optString("address")).build();
              Checkout.registerAgent(registration);
              callbackContext.success();
            } else {
                callbackContext.error("registerAgent is called with incomplete details");
          }
        } catch (Exception e) {
            callbackContext.error("shoptopupAgent not initialized");
        }
      },
        openCart {
            @Override void performAction(JSONArray args, CallbackContext callbackContext, CordovaInterface cordova) {
                try{
                    Checkout.openCart();
                    callbackContext.success();
                } catch (Exception e) {
                    callbackContext.error("shoptopupAgent not initialized");
                }
            }

        },
            openTransactions{
                @Override void performAction(JSONArray args, CallbackContext callbackContext, CordovaInterface cordova) {
                    try{
                        Checkout.openTransactions();
                        callbackContext.success();
                    } catch (Exception e) {
                        callbackContext.error("shoptopupAgent not initialized");
                    }

                }
            }

    }

    }
    
}
