package com.conway;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Conway2D {
	
	private final int width;
	private final int size;
	byte[] data;
	StringBuilder output;
	public static boolean alldead;

	public Conway2D(){

		this.width = 200;
		this.size = width * width;
		data = new byte[size];
		output = new StringBuilder("{");
		alldead = false;		
		
	}
	
	public void iterate(){

		byte[] prev = new byte[size];
		System.arraycopy(data, 0, prev, 0, size);

		byte[] next = new byte[size];

		output.delete(0, output.length());
		
		for ( int i = 0; i < width; i++ ){
			
			for ( int j = 0; j < width; j++ ){
				
				int type = isAlive(i, j, prev);						
				
				if ( type > 0 ){
					next[i * width + j] = 1;
					output.append("["+i+","+j+"],");
				}else{
					next[i * width + j] = 0;
				}
			}
		}
		if (output.length() != 0) {
			output.append("}");
			System.arraycopy(next, 0, data, 0, size);
		}
		else {
			alldead = true;
		}
		
	}


	/**
	 * Checks if the cell is alive
	 *  x - The row position
	 *  y - The column position
	 *  d - The grid data.
	 */

	protected int isAlive(int x, int y, byte[] d){

		int count = 0;

		int pos1 = x * width + y;

		for ( int i = x-1; i <= x + 1; i++ ){
			for ( int j = y - 1; j <= y + 1; j++ ){
				int pos = i * width + j;
				if ( pos >= 0 && pos < size - 1 && pos != pos1){
					if ( d[pos] == 1 ){
						count++;
					}
				}
			}
		}

		//dead
		if ( d[pos1] == 0 ){
			if ( count == 3 ){//becomes alive.
				return 1;
			}
			return 0;//still dead
		}else{//live
			if ( count < 2 || count > 3 ){//Dies
				return 0;
			}
			return 1;//lives
		}
	}
		/**
		 * get seeds from input.  
		 */
		public void getSeed(){

			for ( int i = 0; i < 9500; i++ ){
				int x = (int)(Math.random() * width);
				int y = (int)(Math.random() * width);
				data[y*width+x] = 1;				
			}			
		}
		
		public void getInput() {
			String input="";
			String[] inputarray;
			String[] coor;
			
			int col=0;
			int row=0;
			
			try { 
				String pathname = ".\\input.txt"; 
				File filename = new File(pathname);
				InputStreamReader reader = new InputStreamReader(
						new FileInputStream(filename)); 
				BufferedReader br = new BufferedReader(reader);
				input = br.readLine();
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
			inputarray = input.split("\\[(\\.+?)\\]");							
			coor= inputarray[0].replaceAll("\\[","").replaceAll("\\]","").split(",");
			
			int length = coor.length;		
			
			if  (length > 2* size) {
				System.out.println("invalid input, The length of input is"+length);
			}
			else if(length == 0 ){				
				System.out.println("Input is null");
			}
			else {
				for(int i=0; i<length-1; i+=2) {
					try {
						row = Integer.parseInt(coor[i]);
						col = Integer.parseInt(coor[i+1]);
					}catch(NumberFormatException e) {
						e.printStackTrace();
					}
					
					if (row >= 200 || row < 0 || col >= 200 || col < 0) {
						System.out.println("Invalide input, coordinates out of range! "
								+ "[row,col] is ["+ row+","+col+"]...exit!");
						return;
					}
					else {
						data[row*width+col] = 1;
			}
				}
			}
							

		}

		/* Retrieves the grid data. */
		public void printData(int it, BufferedWriter out) {
			StringBuilder seqNum = new StringBuilder(Integer.toString(it));
			seqNum.append(": ");
			if (output.length() == 0) {
				System.out.println(it+":[]");
				
			}else {
				output.insert(0, seqNum);

				try {
					out.append(output.toString());
					out.append("\r\n\r\n");
					out.flush();
					
				}catch (Exception e){
					e.printStackTrace();
				}
			}			
		}
		
		
		public static void main(String[] args) {			
			
			File writename = new File(".\\result.txt");
			int interation = 100;
			
			Conway2D conway=new Conway2D();
			//Get input from Math.random(), just for testing	
			//conway.getSeed();
			
			//Get intput from file "input.txt"
			conway.getInput();
			
			try {				
				writename.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(writename));
				
				try {
					int i =1;
					do {
						conway.iterate();
						conway.printData(i, out);	
						i++;
					}
					while (alldead!=true && i<=interation);
						
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					out.close();
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
}
