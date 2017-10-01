package mas.observer;

import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ShillingBidsObserver implements Observer {
	String model_file_source;
	public ShillingBidsObserver(String model_file_source) {
		//python checkData function from bash
		this.model_file_source=model_file_source;
	}
	@Override
	public void update(String state) {
		//run checkDataBybash
		//if there is a problem 
		//wait();
		
		if (check(state)) {
			Scanner keyboard = new Scanner(System.in);
		    System.out.println("\n----\nFound Shilling bids!!!\n----");
		    keyboard.next();	
		}
	}
	private boolean check(String data) {
		String s = null;
		Process p = null;
		try {
//			System.out.println("data="+data);
			p = Runtime.getRuntime().exec("python ./tensor_watcher.py "+data);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            if ((s = stdInput.readLine()) != null) {
                // read the output from the command
            	if(Integer.parseInt(s) == 1) {
                    return true;
            	}
            }
		} catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
		}  
		return false;
	}
}
