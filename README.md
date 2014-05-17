phonegap-http-request
=====================

###Installation
```bash
$> cordova plugin add https://github.com/vu06052014/phonegap-http-request.git
```

### Create an instance
```js
var httpReq = new plugin.HttpRequest();
```

### HTTP / GET request
```js
var httpReq = new plugin.HttpRequest();
httpReq.post({
  type : "post",
  url : "http://example.com",
  header : {
	referer : "http://mydomain.com"
  },
  params : {
	name : "Test",
	city : "Test"
  }
},function(err, data) {
	/* Success then err is null
	*/ Fail then data is null
}); 
```
