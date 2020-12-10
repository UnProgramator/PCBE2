package client;

import publisher.TopicPublisherStub;
import com.rabbitmq.client.Channel;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Cititor extends Thread{
	private static final String EXCHANGE_NAME = "topic_logs";
	private static final String EXCHANGE_NAME_S = "topic_s";
	private static final String ans="";
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

		        String cititor=TopicPublisherStub.joinStrings(topics, ".", 0);

		        if (topics.length < 1) {
		            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
		            System.exit(1);
		        }

		        for (String bindingKey : topics) {
		            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
		        }



		        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
		            String message = new String(delivery.getBody(), "UTF-8");
		            String routingKey=delivery.getEnvelope().getRoutingKey();
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
}
