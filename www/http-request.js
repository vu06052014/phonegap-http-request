(function(window) {
  
  function HttpRequest() {
    var self = this;
    var methods = ["post", "get"];
    methods.forEach(function(method) {
      self[method] = function(args, callback) {
        self.request_(args, callback);
      };
    });
  }
  
  HttpRequest.prototype.request_ = function(args, callback) 
  {
    return cordova.exec(function(response) {
      callback(null, response);
    }, function(error) {
      callback(error);
    }, 'HttpRequest', 'execute', [args]);
  };
  
  // Plug in to Cordova
  cordova.addConstructor(function() {
    if (!window.Cordova) {
      window.Cordova = cordova;
    };
    if(!window.plugin) {
      window.plugin = {};
    }
    window.plugin.HttpRequest = HttpRequest;
  });
})(window);