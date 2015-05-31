function route(invocation,context,invokers) {
    var result = new java.util.ArrayList(invokers.size());
    if(invocation.getArguments()[0].getTest()=="t")
    {
    	return result;
    }
    for (i = 0; i < invokers.size(); i ++) {
        if ("http://10.20.160.198/wiki/display/dubbo/10.20.153.10".equals(invokers.get(i).getUrl().getHost())) {
            result.add(invokers.get(i));
        }
    }
    return result;
} (invocation,context,invokers);