package client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import news.News;
import services.DomainService;

public class Cititor{
	private static final String EXCHANGE_NAME = "topic_logs";
	private static final String EXCHANGE_NAME_S = "topic_s";
	private static String ans="";
	private final DomainService ds;
	private String topics[];
	private static int contor=1;
	private int cititor;

	public Cititor(String[] topics, DomainService ds) throws Exception{
		cititor=contor;
		contor++;
		this.topics=topics;
		this.ds=ds;
		init();

	}


	private void init() throws Exception {
		  try {
			ConnectionFactory factory = new ConnectionFactory();
		      	factory.setHost("localhost");
		        Connection connection = factory.newConnection();
		        Channel channel = connection.createChannel();
		        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		        String queueName = channel.queueDeclare().getQueue();
		        channel.exchangeDeclare(EXCHANGE_NAME_S, "direct");
        if (topics.length < 1) {
		            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
		            System.exit(1);
		        }
	      		      		        
				
			 for (String topic : topics) {
				 StringBuilder bindingKey= new StringBuilder("");
				 int pozDel=topic.indexOf("%");
				 if(pozDel<0) 
					 bindingKey.append("*."+domeniu_all(topic));
				 else
					 if(pozDel==0)
						 bindingKey.append(topic.substring(1)+".#");	 
					 else
					 {
						 bindingKey.append(topic.substring(pozDel+1));
						 bindingKey.append('.');
						 bindingKey.append(domeniu_all(topic.substring(0,pozDel)));
							 
				 }
				channel.queueBind(queueName,EXCHANGE_NAME, bindingKey.toString() ); 
				//System.out.println("-!- "+bindingKey );
				}
			
		        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
		        	try {
			            String message = new String(delivery.getBody(), "UTF-8");
			            News n = News.fromString(message);
			            
			            if(n.lastChangedData.equals(n.publishData)) {
			            	System.out.println(" [x] Cititorul "+ cititor+" Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
			            	
				            ans = n.getHead();
				            String routingKey = "raspuns-" + n.src;
				            
				            channel.basicPublish(EXCHANGE_NAME_S, routingKey, null, ans.getBytes("UTF-8"));
			            }
			            else {
			            	System.out.println(" [x] Cititorul "+ cititor+" Receivedthe modified message '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
			            }
			            
		        	}
		        	catch(Exception e) {
		        		e.printStackTrace();
		        	}
		        };
		        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
		  }
		  catch(Exception e) {
			  e.printStackTrace();
		  }
		  }
	
	private String domeniu_all(String topic) {
		if(ds.getParent(topic)==null) return topic+".*";
		return "*."+topic;
	}
	
	public void run() {
		  try {
			init();
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	  }  
		  
	  
	  
}
