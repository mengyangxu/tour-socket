package com.xmy.socket.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/*import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.fileupload.disk.DiskFileItem;*/
/*import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;*/


public class HttpClientUtil {
	private static HttpClient client = null;

	public static int GET = 0;
	public static int POST = 1;
	
	// 构造单例
	private HttpClientUtil() {

		MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		// 默认连接超时时间
		params.setConnectionTimeout(30000);
		// 默认读取超时时间
		params.setSoTimeout(30000);
		// 默认单个host最大连接数
		params.setDefaultMaxConnectionsPerHost(100);// very important!!
		// 最大总连接数
		params.setMaxTotalConnections(256);// very important!!
		
		params.setStaleCheckingEnabled(true);
		httpConnectionManager.setParams(params);

		client = new HttpClient(httpConnectionManager);

		client.getParams().setConnectionManagerTimeout(30000);
		
		// client.getParams().setIntParameter("http.socket.timeout", 10000);
		// client.getParams().setIntParameter("http.connection.timeout", 5000);
	}

	private static class ClientUtilInstance {
		private static final HttpClientUtil ClientUtil = new HttpClientUtil();
	}

	public static HttpClientUtil getInstance() {
		return ClientUtilInstance.ClientUtil;
	}

	/**
	 * 发送http GET请求，并返回http响应字符串
	 * 
	 * @param urlstr
	 *            完整的请求url字符串
	 * @return
	 */
	public String doGetRequest(String urlstr) {
		final String METHODNAME = "doGetRequest";
		String response = "";

		GetMethod httpmethod = new GetMethod(urlstr);
		try {
			int statusCode = client.executeMethod(httpmethod);
			InputStream inputStream = null;
			if (statusCode == HttpStatus.SC_OK) {
				inputStream = httpmethod.getResponseBodyAsStream();
			}
			if (inputStream != null) {
				response = getResponseString(inputStream, "UTF-8");
			}
		} catch (HttpException e) {
			// logger.error("获取响应错误，原因：" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// logger.error("获取响应错误，原因1：" + e.getMessage());
			e.printStackTrace();
		} finally {
			httpmethod.releaseConnection();
		}
		return response;
	}

	/**
	 * 发送http POST请求，并返回http响应字符串
	 * 
	 * @param urlstr
	 *            请求url字符串
	 * @param params
	 *            POST参数，以键值对的形式放入，可为null
	 * @return
	 */
	/*
    public String doPostRequest(String urlstr, Map<String, String> params) {
		final String METHODNAME = "doPostRequest";
		String response = "";

		PostMethod httpmethod = new PostMethod(urlstr);  
*//*		if (null != params && !params.isEmpty()) {
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				String value = params.get(key);
				if (value != null) {
					System.out.println(key+"|||||||||"+value);
					httpmethod.addParameter(key, value);
				}
			}
		}*//*

		JSONObject jsonParam=JSONObject.fromObject(params);
		httpmethod.setRequestBody(jsonParam.toString());
		
		httpmethod.addRequestHeader("Content-Type", "application/json");//
		String context = Base64Util.getBase64(GsonTools.createGsonString(params));
	    httpmethod.addRequestHeader("context",context);
		String time = String.valueOf(Calendar.getInstance().getTime().getTime());
	    httpmethod.addRequestHeader("time",time);
		httpmethod.addRequestHeader("scret",this.addSecret(context, time));

		httpmethod.getParams().setContentCharset("utf-8");
		
		try {
			int statusCode = client.executeMethod(httpmethod);
			InputStream inputStream = null;
			if (statusCode == HttpStatus.SC_OK) {
				inputStream = httpmethod.getResponseBodyAsStream();
			}
			if (inputStream != null) {
				response = getResponseString(inputStream, "UTF-8");
			}
		} catch (HttpException e) {
			// logger.error("获取响应错误，原因：" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// logger.error("获取响应错误，原因1：" + e.getMessage());
			e.printStackTrace();
		} finally {
			httpmethod.releaseConnection();

		}
		return response;
	}
	*/
	private String addSecret(String context,String time){
		
		String str = context;
		
		str += ResourceBundle.getBundle("longRangeConfig").getString("author.key")+time;
		return MD5.md5(str);
		
	}
	
	private String FormatBizQueryParaMap(Map<String, String> paraMap,
            boolean urlencode) throws Exception {
        String buff = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(
                    paraMap.entrySet());
            Collections.sort(infoIds,
                    new Comparator<Map.Entry<String, String>>() {
                        public int compare(Map.Entry<String, String> o1,
                                Map.Entry<String, String> o2) {
                            return (o1.getKey()).toString().compareTo(
                                    o2.getKey());
                        }
                    });
            for (int i = 0; i < infoIds.size(); i++) {
                Map.Entry<String, String> item = infoIds.get(i);
                //System.out.println(item.getKey());
                if (!"".equals(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlencode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    buff += key + "=" + val + "&";
                }
            }
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return buff;
    }
	
	/**
	 * Bate:携带 文件数据 和 字符串数据 发送请求
	 * @param urlstr
	 * @param params
	 * @param files
	 * @return
	 */
	/*public String doPostRequestAndFile(String urlstr, Map<String, String> params,Map<String, MultipartFile> files) {
		
		org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlstr);
        
        post.setHeader("Content-Type", "multipart/form-data");
        
        MultipartEntity filesEntity = new MultipartEntity();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
        
        if (null != params && !params.isEmpty()) {
        	
        	
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				String value = params.get(key);
				if (value != null) {
					nvps.add(new BasicNameValuePair(key, value));  
				}
			}
		}
        
        if (null != files && !files.isEmpty()) {
        	
//        	String fileCachePath = this.getClass().getClassLoader().getResource("/").getPath();
//        	fileCachePath = fileCachePath.substring(0, fileCachePath.indexOf("WEB-INF"))+"uploadFiles/cache/";
//        	LoggerFactory.getLogger(this.getClass()).info("图片中转文件夹："+fileCachePath);
//        	String folderPath = fileCachePath+params.get("idCardNumber")+"/";
//        	File folder = new File(folderPath);
//			if(!folder.exists()){
//				folder.mkdirs();
//			}
        	
			Set<String> keySet = files.keySet();
			for (String key : keySet) {
				MultipartFile value = files.get(key);
				if (value != null) {
					CommonsMultipartFile cf= (CommonsMultipartFile)value; 
			        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
					FileBody fileBody = new FileBody(fi.getStoreLocation());
					filesEntity.addPart(key, fileBody); 
				}
			}
		}
        
        try {
			post.setEntity(new UrlEncodedFormEntity(nvps));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        post.setEntity(filesEntity);
        
        HttpResponse response = null;
		try {
			response = httpclient.execute(post);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){    
//            HttpEntity entitys = response.getEntity();   
        	LoggerFactory.getLogger(this.getClass()).info("身份证图片上传成功");
        	httpclient.getConnectionManager().shutdown(); 
        	return response.getHeaders("Content-Type")[0].getValue().toString();
        }  
        else{
        	throw new RuntimeException("身份证图片上传失败。");
        }
        
         
	}
	*/
	/**
	 * 忽略证书发送请求
	 * @param scheme  http/https
	 * @param method  HttpClient.SET/HttpClient.POST
	 * @param port	  443
	 * @param url
	 * @param params
	 * @return
	 */
	public String request(String scheme,int method,int port,String url, Map<String, String> params)
	{
		HttpClient http = new HttpClient();  
	    Protocol myhttps = new Protocol(scheme, new SSLProtocolSocketFactory(), port);
	    Protocol.registerProtocol(scheme, myhttps);  
	    
	    String resultContent = "";
	    
	    try {
	    	
	    	if(method==HttpClientUtil.GET)
		    {
		    	if (null != params && !params.isEmpty()) {
		    		String requestParams = "?";
		    		int i = 0;
		    		Set<String> keySet = params.keySet();
					for (String key : keySet) {
						String value = params.get(key);
						if (value != null) {
							if(i==0)
							{
								requestParams = requestParams+key+"="+value;
								i++;
							}
							else{
								requestParams = requestParams+"&"+key+"="+value;
							}
							
						}
					}
					url += requestParams;
				}
		    	GetMethod requestMethod = new GetMethod(url);
		    	//requestMethod.setRequestHeader("charset", "utf-8");
		    	requestMethod.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");    
		    	http.executeMethod(requestMethod);
		    	resultContent = requestMethod.getResponseBodyAsString();
		    	requestMethod.releaseConnection();
		    }	
		    else if(method==HttpClientUtil.POST)
		    {
//		    	if (null != params && !params.isEmpty()) {
//		    		org.apache.commons.httpclient.NameValuePair[] requestParams = new org.apache.commons.httpclient.NameValuePair[params.size()];
//					int i = 0;
//		    		Set<String> keySet = params.keySet();
//					for (String key : keySet) {
//						String value = params.get(key);
//						if (value != null) {
//							requestParams[i] = new org.apache.commons.httpclient.NameValuePair(key, value);
//							i++;
//						}
//					}
//					requestMethod.setRequestBody(requestParams);
//				}
		    	if (null != params && !params.isEmpty()) {
		    		String requestParams = "?";
		    		int i = 0;
		    		Set<String> keySet = params.keySet();
					for (String key : keySet) {
						String value = params.get(key);
						if (value != null) {
							if(i==0)
							{
								requestParams = requestParams+key+"="+value;
								i++;
							}
							else{
								requestParams = requestParams+"&"+key+"="+value;
							}
							
						}
					}
					url += requestParams;
				}
		    	PostMethod requestMethod = new PostMethod(url);
		    	//requestMethod.setRequestHeader("charset", "utf-8");
		    	requestMethod.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8"); 
		    	http.executeMethod(requestMethod);
		    	resultContent = requestMethod.getResponseBodyAsString();
		    	requestMethod.releaseConnection();
		    }
	    	
	    } catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultContent;
	}

	/**
	 * 
	 * @param inputStream
	 * @param Charset
	 * @return
	 */
	public String getResponseString(InputStream inputStream, String Charset) {
		final String METHODNAME = "getResponseString";
		String response = "";
		try {
			if (inputStream != null) {
				StringBuffer buffer = new StringBuffer();
				InputStreamReader isr = new InputStreamReader(inputStream,
						Charset);
				Reader in = new BufferedReader(isr);
				int ch;
				while ((ch = in.read()) > -1) {
					buffer.append((char) ch);
				}
				response = buffer.toString();
				buffer = null;
			}
		} catch (Exception e) {
			// logger.error("获取响应错误，原因：" + e.getMessage());
			//response = response + e.getMessage();
			e.printStackTrace();
		}
		return response;
	}

	// public static void main(String[] args) {
	// String url =
	// "http://esms.etonenet.com/sms/mt?spid=3060&sppassword=hbkj3060&das=8618611178949&command=MULTI_MT_REQUEST&sm=a1beccd4b1a6a1bf20cda8b5c0bdd3c8ebcdeab3c9a3a1&dc=15";
	// System.out.println(doGetRequest(url));
	// }
	
	public static void main(String arg[]){
		/*System.out.println("外网------------");
		
		Map<String, String> pd = new HashMap<String, String>();
		
		pd.put("userId", "7cc2bab762b54872aac659b34be02c5b");
		pd.put("accountManagerId", "7cc2bab762b54872aac659b34be02c5b");
		pd.put("productId", "2a7a805cd81c4bceb5718a7d76f30024");
		//pd.put("addressId", addressId);
		pd.put("addressInfo", "11234");
		pd.put("pickWay", "0");
		pd.put("count", "1");
		pd.put("remark", "asdfasd");
		pd.put("invoiceInfo", "");
		pd.put("invoiceType", "p");
		pd.put("titleType", "P");
		String doPostRequest = HttpClientUtil.getInstance().doPostRequest("http://test.ucbego.com/csh/appProductOrder/api/submitOrder", pd);
		System.out.println("---------"+doPostRequest);*/
	}
	public String doPostJsonRequestByContentType(String urlstr, Object obj,String contentType){
		final String METHODNAME = "doPostRequest";
		String response = "";

		PostMethod httpmethod = new PostMethod(urlstr);

		try {
			RequestEntity se = new StringRequestEntity(GsonTools.createGsonString(obj), "application/json", "UTF-8");
			httpmethod.setRequestEntity(se);
			int statusCode = client.executeMethod(httpmethod);
			response = httpmethod.getResponseBodyAsString();

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpmethod.releaseConnection();

		}
		return response;
	}


}
