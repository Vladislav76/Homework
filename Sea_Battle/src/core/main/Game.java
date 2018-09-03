//package core.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Game {

	public void start() {
		ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
		RunnablePanel gamePanel = context.getBean("consoleVersion", RunnablePanel.class);
		gamePanel.run();
	}

}
