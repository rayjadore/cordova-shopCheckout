package cordova.plugin.shop.checkout;

import org.apache.cordova.CallbackContext;
import android.content.Intent;
import android.util.Log;

import org.apache.cordova.CordovaInterface;
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
public class ShopCheckout extends CordovaPlugin {

    @Override protected void pluginInitialize() {
        Log.i("start up initialization", "------------------>");
        // cordova.getActivity().runOnUiThread(new Runnable() {
        //     @Override public void run() {
        //         Log.i("start up initialization", "------------------>");
        //         setUpCheckout();
        //     }
        // });
    }

    @Override public void onStart() {
        Log.i("onstart initialization", "------------------>");
        // cordova.getActivity().runOnUiThread(new Runnable() {
        //     @Override public void run() {
        //         //We also initialize agentCheckout here just in case it has died. 
        //         setUpCheckout();
        //     }
        // });
    }


    @Override public void onNewIntent(Intent intent) {
        cordova.getActivity().setIntent(intent);
    }

    private void setUpCheckout() {
        try {
            //Get app credentials from config.xml or the app bundle if they can't be found
            String apiKey = preferences.getString("shop-checkout-android-api-key", "");
            String sandbox = preferences.getString("shop-checkout-android-sandbox", "true");
            boolean isSandbox = Boolean.parseBoolean(sandbox);
            Checkout.initialize(cordova.getActivity().getApplication(), apiKey, isSandbox);
            Log.i("shop checkout", "initialized");
        } catch (Exception e) {
            Log.e("ShopCheckout-Cordova", "ERROR: Something went wrong when initializing shopCheckout. Have you set your SHOP-CHECKOUT_ANDROID_API_KEY?", e);
        }
    }

    private enum Action {
        registerAgent {
            void performAction(JSONArray args, CallbackContext callbackContext, CordovaInterface cordova) {
                try {
                    Log.i("register agent", "clicked");
                    JSONObject options = args.optJSONObject(0);
                    if((options.optString("agentId") != null && options.optString("agentId").length() > 0) && (options.optString("firstName") != null && options.optString("firstName").length() > 0) &&
                            (options.optString("lastName") != null && options.optString("lastName").length() > 0) && (options.optString("phoneNumber") != null && options.optString("phoneNumber").length() > 0 )
                            && (options.optString("countryCode") != null && options.optString("countryCode").length() > 0)  && (options.optString("company") != null && options.optString("company").length() > 0) &&
                            (options.optString("address") != null && options.optString("address").length() > 0)) {
                        Registration registration = Registration.Builder.create()
                            .agentId(options.optString("agentId"))
                            .firstName(options.optString("firstName"))
                            .lastName(options.optString("lastName"))
                            .phoneNumber(options.optString("phoneNumber"))
                            .countryCode(options.optString("countryCode"))
                            .company(options.optString("company"))
                            .latitude(options.optDouble("latitude"))
                            .longitude(options.optDouble("longitude"))
                            .address(options.optString("address")).build();
                        Checkout.registerAgent(registration);
                        callbackContext.success();
                    } else {
                        callbackContext.error("registerAgent is called with incomplete details");
                        Log.e("registerAgent is called with incomplete details", "reg failed");
                    }
                } catch (Exception e) {
                    callbackContext.error("shop-checkout not initialized");
                    Log.e("shop-checkout not initialized", "app initialization");
                }
            }
        },
        openProducts {
            void performAction(JSONArray args, CallbackContext callbackContext, CordovaInterface cordova) {
                try{
                    Checkout.openProducts();
                    callbackContext.success();
                } catch (Exception e) {
                    callbackContext.error("shop-checkout not initialized");
                }
            }
        },
        openTransactions {
            void performAction(JSONArray args, CallbackContext callbackContext, CordovaInterface cordova) {
                try{
                    Checkout.openTransactions();
                    callbackContext.success();
                } catch (Exception e) {
                    callbackContext.error("shop-checkout not initialized");
                }
            }
        },
        logout {
            void performAction(JSONArray args, CallbackContext callbackContext, CordovaInterface cordova) {
                try{
                    Checkout.logout();
                    callbackContext.success();
                } catch (Exception e) {
                    callbackContext.error("shop-checkout not initialized");
                }
            }
        },
        isInitialized {
            void performAction(JSONArray args, CallbackContext callbackContext, CordovaInterface cordova) {
                try{
                    Checkout chkObject = new Checkout();
                    boolean initialized = chkObject.isInitialized();
                    callbackContext.success(Boolean.toString(initialized));
                } catch (Exception e) {
                    callbackContext.error("shop-checkout not initialized");
                }
            }
        }
    }
}
