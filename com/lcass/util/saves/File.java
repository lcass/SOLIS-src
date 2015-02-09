package com.lcass.util.saves;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class File {
	private FileOutputStream fos;
	private FileInputStream fis;
	private boolean open = false;
	public File(String location){
		try {
			fos = new FileOutputStream(location);
			fis = new FileInputStream(location);
			open = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close(){
		if(open){
			try {
				fos.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			open = false;
		}
		else{
			System.out.println("Error no location given");
		}
	}
	public void open(String location){
		if(open){
			close();
		}
		
		try {
			fis = new FileInputStream(location);
			fos = new FileOutputStream(location);
			open = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String read(){
		if(!open){
			System.out.println("No open file");
			return null;
		}
		byte[] input;
		try {
			input = new byte[fis.available()];
			fis.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String output = input.toString();
		return output;
		
		
	}
	public void write(String data){
		if(!open){
			System.out.println("No open file");
			return;
		}
		try {
			fos.write(data.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void write(String data,int offset){
		if(!open){
			System.out.println("No open file");
			return;
		}
		try {
			
			fos.write(data.getBytes(),offset,data.getBytes().length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
