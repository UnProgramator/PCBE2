package publiser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
	private static final String EXCHANGE_NAME = "topic_logs";
	private volatile HashMap<String, Integer> nrCititori;
	private ArrayList<News> publishedNews = new ArrayList<News>();
	
	public void finalize() {
		try {
			channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Publisher(String PublisherName, DomainService ds) throws IOException, TimeoutException {
		this.PublisherName=PublisherName;
		this.ds = ds;
		this.nrCititori = new HashMap<>();
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    
	    connection = factory.newConnection();
	    
	    channel = connection.createChannel();
	    channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	    
	    /*Aici apeleaza functia pentru subscribe la serviciul de "nr stiri citite" pe canalul deja deschis*/
	    nrStiriCitite(channel);
	}
	
	public void run() {
		for(int i=0; i< 10; i++) {
			
			if(publishedNews.size()>2 && Math.random()<0.5) {
				News n = publishedNews.get((int)(Math.random()*publishedNews.size()));
				this.modifyStire(n);
			}
			else
				this.publish(genNews());
			
			if(publishedNews.size() > 0 && (Math.random()>0.15 || i>7)) {
				News toBeDeletedEliminatedEradicated = publishedNews.get((int)(Math.random()*publishedNews.size()));
				System.out.println("removed \'" + toBeDeletedEliminatedEradicated + "\'");
				this.sterge_ma(toBeDeletedEliminatedEradicated); //aici facem strergeri XD
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void publish(News news) {
		try {
	        String routing_key = getPublishRoutingKey(news.domeniu);
	        channel.basicPublish(EXCHANGE_NAME, routing_key, null, news.toString().getBytes("UTF-8"));
	        System.out.println("published to " + routing_key + " msg: " + news);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sterge_ma(News news) {
		int pos = publishedNews.indexOf(news);
		publishedNews.remove(pos);
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
		
		News retVal = new News(
				domeniu,
				PublisherName,
				new Date(),
				titlu,
				body
				);
				
		publishedNews.add(retVal);
				
		return retVal;
	}
	
	private void modifyStire(News news) {
		news.changeContent(news.body + "modify", new Date());
		String routing_key = getPublishRoutingKey(news.domeniu);
        try {
			channel.basicPublish(EXCHANGE_NAME, routing_key, null, news.toString().getBytes("UTF-8"));
			System.out.println("modified to " + routing_key + " msg: " + news);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void nrStiriCitite(Channel channel)
	{
		synchronized(nrCititori){
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
		            System.out.println(PublisherName + nrCititori);
		        };
		        
		        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
