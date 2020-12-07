package com.rabbitmq.consumer;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import com.ram.model.Product;

@RestController
@Component



public class Consumer implements ChannelAwareMessageListener {
	@ResponseBody
	//@RequestMapping(value = "/getString", method = RequestMethod.POST)
	@Override
	public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
		// System.out.println("Received <" + message + ">");
		
		byte[] body = message.getBody();
		  String receive=new String(body); 
		  
		  System.out.println(receive);
		  channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		  
		  String query = "http://localhost:9191/server/store";

          URL url = new URL(query);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setConnectTimeout(5000);
          conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
          conn.setDoOutput(true);
          conn.setDoInput(true);
          conn.setRequestMethod("POST");
			/*
			 * OutputStream os = conn.getOutputStream();
			 * os.write(receive.getBytes("UTF-8")); os.close(); if (conn.getResponseCode()
			 * != HttpURLConnection.HTTP_CREATED) { throw new
			 * RuntimeException("Failed : HTTP error code : " + conn.getResponseCode()); }
			 * BufferedReader br = new BufferedReader(new InputStreamReader(
			 * (conn.getInputStream())));
			 * 
			 * String output; System.out.println("Output from Server .... \n"); while
			 * ((output = br.readLine()) != null) { System.out.println(output); }
			 * 
			 * conn.disconnect();
			 */
          OutputStreamWriter output=new OutputStreamWriter(conn.getOutputStream());
          System.out.println(receive);
          output.write(receive.toString());
          conn.connect();
			
			  String output1=conn.getResponseMessage(); System.out.println(output1);
			 
		/**
		 * Suppose say here we are storing product details in the table and some
		 * exception occured then what we can do is do not acknowledge to Queue, so that
		 * it will be in queue and later we can pick up the same product from the queue
		 * and process it
		 */
		/*
		 * Positively acknowledge a single delivery, the message will be discarded
		 */
		
	}
	
	/*
	 * @RequestMapping(value="/process", method = RequestMethod.POST) public void
	 * process(@RequestBody Map<String, Object> receive) {
	 * System.out.println(receive); }
	 */
}
