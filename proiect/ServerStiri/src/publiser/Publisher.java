package publiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import news.News;
import services.DomainService;

public class Publisher extends Thread{
	private String PublisherName;
	DomainService ds;
	int nr=0;
	private Connection connection;
	private Channel channel;
	
	
	public Publisher(String PublisherName, DomainService ds) throws IOException, TimeoutException {
		this.PublisherName=PublisherName;
		this.ds = ds;
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    
	    connection = factory.newConnection();
	    
	    channel = connection.createChannel();
	    channel.exchangeDeclare(PublisherName, "topic");
	    
	    /*Aici apeleaza functia pentru subscribe la serviciul de "nr stiri citite" pe canalul deja deschis*/
	}
	
	public void run() {
		for(int i=0; i< 10; i++) {
			this.publish(genNews());
		}
	}
	
	private void publish(News news) {
		try {
	        String routing_key = getPublishRoutingKey(news.domeniu);
	        channel.basicPublish(PublisherName, routing_key, null, news.toString().getBytes("UTF-8"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getPublishRoutingKey(String sourceDeclaredDomain) {
		String domain_, subdomain_;
		
		domain_ = ds.getParent(sourceDeclaredDomain);
		
		if(domain_ == null) {
			subdomain_ ="*";
			domain_ = sourceDeclaredDomain;
		}
		else {
			subdomain_ = sourceDeclaredDomain;
		}
		
		return PublisherName + "." + domain_ + "." + subdomain_;
	}
	
	private News genNews() {
		String domeniu, titlu, body;
		
		ArrayList<String> domains = ds.getSubdomains();
		
		domeniu = domains.get((int)(Math.random()*domains.size()));
		titlu = "Stire - " + domeniu + nr + " from " + PublisherName;
		body = "Body - " + domeniu + nr + " from " + PublisherName;
				nr++;
		
		return new News(
				domeniu,
				PublisherName,
				new Date(),
				titlu,
				body
				);
	}
	
}
