package com.web.foo.zkclient.router.script;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.apache.log4j.lf5.util.StreamUtils;
import org.apache.zookeeper.CreateMode;

public class FirstScriptRouterRule {

	public static void main(String[] args) throws ZkInterruptedException,
			IllegalArgumentException, ZkException,
			RuntimeException, IOException {
		ZkClient zkClient = new ZkClient("10.5.111.101:2181", 60 * 1000);
		System.out
				.println(zkClient
						.getChildren("/dubbo/com.web.foo.service.FirstDubboService/routers"));

//		zkClient.delete("/dubbo/com.web.foo.service.FirstDubboService/routers/script%3A%2F%2F0.0.0.0%2Fcom.web.foo.service.FirstDubboService%3Fcategory%3Drouters%26dynamic%3Dfalse%26rule%3Dfunction%2Broute%2528invokers%2529%2B%257B%250A%2B%2B%2B%2Bvar%2Bresult%2B%253D%2Bnew%2Bjava.util.ArrayList%2528invokers.size%2528%2529%2529%253B%250A%2B%2B%2B%2Breturn%2Bresult%253B%250A%257D%2B%2528invokers%2529%253B%26runtime%3Dtrue%26version%3D1.0");
		InputStream is = FirstScriptRouterRule.class.getClassLoader().getResourceAsStream("script.js");
		String content = new String(StreamUtils.getBytes(is));
		content = URLEncoder.encode(content, "UTF-8");

		zkClient.delete("/dubbo/com.web.foo.service.FirstDubboService/routers/script%3A%2F%2F0.0.0.0%2Fcom.web.foo.service.FirstDubboService%3Fcategory%3Drouters%26dynamic%3Dfalse%26rule%3Dfunction%2Broute%2528invocation%252Ccontext%252Cinvokers%2529%2B%257B%250D%250A%2B%2B%2B%2Bvar%2Bresult%2B%253D%2Bnew%2Bjava.util.ArrayList%2528invokers.size%2528%2529%2529%253B%250D%250A%2B%2B%2B%2Bif%2528invocation.getArguments%2528%2529%255B0%255D.getTest%2528%2529%253D%253D%2522t%2522%2529%250D%250A%2B%2B%2B%2B%257B%250D%250A%2B%2B%2B%2B%2509return%2Bresult%253B%250D%250A%2B%2B%2B%2B%257D%250D%250A%2B%2B%2B%2Bfor%2B%2528i%2B%253D%2B0%253B%2Bi%2B%253C%2Binvokers.size%2528%2529%253B%2Bi%2B%252B%252B%2529%2B%257B%250D%250A%2B%2B%2B%2B%2B%2B%2B%2Bif%2B%2528%2522http%253A%252F%252F10.20.160.198%252Fwiki%252Fdisplay%252Fdubbo%252F10.20.153.10%2522.equals%2528invokers.get%2528i%2529.getUrl%2528%2529.getHost%2528%2529%2529%2529%2B%257B%250D%250A%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2Bresult.add%2528invokers.get%2528i%2529%2529%253B%250D%250A%2B%2B%2B%2B%2B%2B%2B%2B%257D%250D%250A%2B%2B%2B%2B%257D%250D%250A%2B%2B%2B%2Breturn%2Bresult%253B%250D%250A%257D%2B%2528invocation%252Ccontext%252Cinvokers%2529%253B%26runtime%3Dtrue%26version%3D1.0");
//				+ URLEncoder
//						.encode("script://0.0.0.0/com.web.foo.service.FirstDubboService?category=routers&dynamic=false&rule="
//								+ content + "&runtime=true&version=1.0",
//								"UTF-8"));
//
//		zkClient.create(
//				"/dubbo/com.web.foo.service.FirstDubboService/routers/"
//						+ URLEncoder
//								.encode("script://0.0.0.0/com.web.foo.service.FirstDubboService?category=routers&type=groovy&dynamic=false&rule="
//										+ content + "&runtime=true&version=1.0",
//										"UTF-8"), null, CreateMode.PERSISTENT);

		// zkClient.delete("/dubbo/com.web.foo.service.FirstDubboService/routers/"
		// + URLEncoder
		// .encode("script://0.0.0.0/com.web.foo.service.FirstDubboService?category=routers&dynamic=false&rule=function+route%28invokers%29+%7B%0A++++var+result+%3D+new+java.util.ArrayList%28invokers.size%28%29%29%3B%0A++++return+result%3B%0A%7D+%28invokers%29%3B&runtime=true&version=1.0",
		// "UTF-8"));

		// zkClient.create(
		// "/dubbo/com.web.foo.service.FirstDubboService/routers/"
		// +
		// "route%3A%2F%2F0.0.0.0%2Fcom.web.foo.service.FirstDubboService%3Fcategory%3Drouters%26dynamic%3Dfalse%26enabled%3Dtrue%26force%3Dfalse%26name%3Dt%26priority%3D0%26router%3Dcondition%26rule%3Dconsumer.host%2B%253D%2B10.165.124.*%2B%253D%253E%2Bprovider.host%2B%253D%2B10.165.124.36%26runtime%3Dfalse%26version%3D1.0",
		// null, CreateMode.PERSISTENT);

		// zkClient.delete("/dubbo/com.web.foo.service.FirstDubboService/routers/"
		// +
		// "route%3A%2F%2F0.0.0.0%2Fcom.web.foo.service.FirstDubboService%3Fcategory%3Drouters%26dynamic%3Dfalse%26enabled%3Dtrue%26force%3Dfalse%26name%3Dt%26priority%3D0%26router%3Dcondition%26rule%3Dconsumer.host%2B%253D%2B10.165.124.*%2B%253D%253E%2Bprovider.host%2B%253D%2B10.165.124.36%26runtime%3Dfalse%26version%3D1.0");

		System.out
				.println(zkClient
						.getChildren("/dubbo/com.web.foo.service.FirstDubboService/routers"));
	}

}
