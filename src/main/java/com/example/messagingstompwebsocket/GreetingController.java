package com.example.messagingstompwebsocket;

import org.assertj.core.internal.Paths;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
@Controller
public class GreetingController {

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(UserMessage message) throws Exception {

		String string1 = message.getNum1();
		String string2 = message.getNum2();

		int index = string1.indexOf(string2);
     
        
        if (index != -1) {
            writeResultToFile("The pattern is found at index: " + index);
            return new Greeting("The pattern is found at index: " + index);
        } else {
            writeResultToFile("The pattern is not found. Returned value: " + index);
            return new Greeting("The pattern is not found. Returned value: " + index);

        }

		
	}

	@MessageMapping("/results")
	@SendTo("/topic/greetings")
    public Greeting getResults() throws IOException {
        String fileName = "results.txt";
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            fileContent.append("No results found.");
        }

        return new Greeting(fileContent.toString());
    }
	private void writeResultToFile(String result) {
        String fileName = "results.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(result);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
