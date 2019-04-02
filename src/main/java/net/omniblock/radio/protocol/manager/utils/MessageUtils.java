package net.omniblock.radio.protocol.manager.utils;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.entities.Message;

public class MessageUtils {

	public static DeadMessage deleteMessageAfter(Message message, TimeUnit unit, int quantity) {
		
		Thread thread = new Thread(new Runnable(){
			
			@Override
            public void run(){
				
                try {
                	
					unit.sleep(quantity);
					message.delete().complete();
					return;
					
				} catch (InterruptedException e) { }
				
            }
            
        });
		
		thread.start();
		return new DeadMessage(thread);
		
	}
	
	public static class DeadMessage {
		
		protected Thread thread;
		
		public DeadMessage(Thread thread) {
			this.thread = thread;
		}
		
		public void cancel() {
			try {
				thread.interrupt();
			} catch(Exception e) { }
		}
		
	}
	
}
