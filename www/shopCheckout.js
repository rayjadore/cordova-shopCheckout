var exec = require('cordova/exec');

var shopCheckout = {
    registerAgent: function(options, success, error) {
        exec(success, error, 'ShopCheckout', 'registerAgent', [options]);
    },

    openCart: function(options, success, error) {
        exec(success, error, 'ShopCheckout', 'openCart', []);
    },

    openTransactions: function(success, error) {
        exec(success, error, 'ShopCheckout', 'openTransactions', []);
    },

    logout: function(success, error) {
        exec(success, error, 'ShopCheckout', 'logout', []);
    }

}

module.exports = shopCheckout;