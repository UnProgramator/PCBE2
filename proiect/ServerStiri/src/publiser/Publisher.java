package publiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import news.News;
import services.DomainService;

public class Publisher extends Thread{
	private String PublisherName;
	DomainService ds;
	int nr=0;
	private Connection connection;
	private Channel channel;
	private static final String EXCHANGE_NAME_S = "topic_s";
	private volatile HashMap<String, Integer> nrCititori;
	
	
	public Publisher(String PublisherName, DomainService ds) throws IOException, TimeoutException {
		this.PublisherName=PublisherName;
		this.ds = ds;
		this.nrCititori = new HashMap<>();
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    
	    connection = factory.newConnection();
	    
	    channel = connection.createChannel();
	    channel.exchangeDeclare(PublisherName, "topic");
	    
	    /*Aici apeleaza functia pentru subscribe la serviciul de "nr stiri citite" pe canalul deja deschis*/
	    nrStiriCitite(channel);
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
	
	private void nrStiriCitite(Channel channel)
	{
		try {
			channel.exchangeDeclare(EXCHANGE_NAME_S, "direct");
	        String queueName = channel.queueDeclare().getQueue();
	        channel.queueBind(queueName, EXCHANGE_NAME_S, "raspuns-"+PublisherName);
	        
	        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	            String domeniu_receive = new String(delivery.getBody(), "UTF-8");
	            if(nrCititori.containsKey(domeniu_receive))
	            {
	            	int val = nrCititori.get(domeniu_receive);
	            	nrCititori.put(domeniu_receive, val+1);
	            }
	            else
	            	nrCititori.put(domeniu_receive, 1);
	        };
	        
	        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
