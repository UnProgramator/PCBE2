package client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import news.News;

public class Cititor extends Thread{
	private static final String EXCHANGE_NAME = "topic_logs";
	private static final String EXCHANGE_NAME_S = "topic_s";
	private static String ans="";
	private String topics[];

	public Cititor(String[] topics){
		this.topics=topics;
	}


	public void init() throws Exception {
		  try {
			ConnectionFactory factory = new ConnectionFactory();
		      	factory.setHost("localhost");
		        Connection connection = factory.newConnection();
		        Channel channel = connection.createChannel();
		        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		        String queueName = channel.queueDeclare().getQueue();

		        String cititor=joinStrings(topics, ".", 0);
		        
		        if (topics.length < 1) {
		            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
		            System.exit(1);
		        }

		        for (String bindingKey : topics) {
		            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
		        }

		        //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		        
		        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
		            String message = new String(delivery.getBody(), "UTF-8");
		            
		            News n = News.fromString(message);
		            ans = n.getHead();
		            String routingKey = "raspuns-" + n.src;
		            
		            channel.exchangeDeclare(EXCHANGE_NAME_S, "direct");
		            channel.basicPublish(EXCHANGE_NAME_S, routingKey, null, ans.getBytes("UTF-8"));
		            System.out.println(" [x] Cititorul "+ cititor+" Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
		        };
		        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
		  }
		  catch(Exception e) {
			  e.printStackTrace();
		  }
		  }
	  
	  public void run() {
		  try {
			init();
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	  }  
		  
	  private String joinStrings(String[] strings, String delimiter, int startIndex) {
			    int length = strings.length;
			    if (length == 0) return "";
			    if (length < startIndex) return "";
			    StringBuilder words = new StringBuilder(strings[startIndex]);
			    for (int i = startIndex + 1; i < length; i++) {
			        words.append(delimiter).append(strings[i]);
			    }
			    return words.toString();
			}  
		  
	    
		  
	  
}
