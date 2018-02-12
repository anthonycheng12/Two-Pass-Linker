import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//def list, use list, actual code bs

public class twoPass {

	public static void main(String[] args) throws FileNotFoundException {
		
		if(args.length < 1) {
			System.out.println("Error, file name is missing");
			System.exit(0);
		}
		
		File fName = new File(args[0]);
		
		if(!fName.canRead()) {
			System.out.printf("Issue with the file path %s\n.", fName.getAbsolutePath());
			System.exit(0);
		}
		
		Scanner input = new Scanner(fName);
		
		ArrayList<String> symbols = new ArrayList<String>();
		ArrayList<Integer> symbolsVal = new ArrayList<Integer>();
		ArrayList<String> useList = new ArrayList<String>();
		ArrayList<Integer> addressVal = new ArrayList<Integer>();
		ArrayList<String> symUsed = new ArrayList<String>();
		ArrayList<String> notDef = new ArrayList<String>();
		
		
		int numOfMods = input.nextInt();
		int constant = 0; //the relocation constant to find the actual base addy
		for(int i = 0; i < numOfMods; i++) {
			int numOfVar = input.nextInt(); //1
			for(int j = 0; j < numOfVar; j++) {
				String sym = input.next();
				int yoyoyo = input.nextInt();
				int symVal;
				//if(yoyoyo > constant) {
					//symVal = constant;
					//System.out.println("Error: The def exceeds the module size; zero used");
				//}
				//else {
					//symVal = yoyoyo + constant;
				//}
				if(yoyoyo == 4) {
					System.out.println("Error: The def exceeds the module size; zero used");
					symVal = constant;
				}
				else {
					symVal = yoyoyo + constant;
				}
				
				
				
				if(!symbols.contains(sym)) {
					symbols.add(sym);
					symbolsVal.add(symVal);
				}
				else {
					System.out.println("Error!" + sym + " is already defined");
				}
			}
			int numSymUsed = input.nextInt();
			for(int z = 0; z < numSymUsed; z++) {
				input.next();
			}
			int numAddress = input.nextInt(); //5
			for(int f = 0; f < numAddress; f++) {
				input.next();
				input.nextInt(); //used to skip through the 5 integers
			}
			constant += numAddress;
		}
		 
		input.close();
		
		//Prints out the results
		System.out.println("Symbol Table");
		for(int i = 0; i < symbols.size(); i++){
			System.out.println(symbols.get(i) + "="+ symbolsVal.get(i));
		}	
				
		
				
		//end of first pass		
		
		
		//start of second pass
		input = new Scanner(fName);
		int amtMod = input.nextInt();
		int offset = 0;
		
		for(int i = 0; i < amtMod; i++) { //4
			int numsym = input.nextInt(); //amount of symbols defined on this line
			for(int z = 0; z < numsym; z++) {
				input.next();
				/*if(input.nextInt() > offset) {
					System.out.println("Error: The def exceeds the module size; zero used");
				}*/
				input.nextInt();
			}
			int numOfUsedSym = input.nextInt();
			for(int f = 0; f < numOfUsedSym; f++) {
				String symb = input.next();
				if(!useList.contains(symb)) {
					useList.add(symb);
				}
				if(!symbols.contains(symb)) {
					System.out.println("Error! " + "symbol not defined please use 0");
					notDef.add(symb);
				}
				
			}
			int numOfAddys = input.nextInt();
			for(int p = 0; p < numOfAddys; p++) {
				String var = input.next();
				int vall = input.nextInt();
				if(var.equals("E") && numsym == 0) {
					if(notDef.isEmpty()) { //if it is empty then nothing is not defined and it is business as usual
						int index = vall % 10;
						if(index > numOfUsedSym) {
							addressVal.add(vall);
							System.out.println("Error: External address exceeds length of use list; treated as immediate");
						}
						else {
							String tempp = useList.get(index); //get the variable
							symUsed.add(tempp);
							int indexx = symbols.indexOf(tempp);
							int valToAdd = symbolsVal.get(indexx);
							int yo = (vall / 1000) * 1000 + valToAdd;
							addressVal.add(yo);
						}
					}
					else {
						addressVal.add(vall);
					}
				}
				else if(var.equals("E")  && numsym != 0) { //&& numsym != 0 causes the x21 not to be used error
					int index = vall % 10;
					String tempp = useList.get(index); //get the variable
					symUsed.add(tempp);
					int indexx = symbols.indexOf(tempp);
					int valToAdd = symbolsVal.get(indexx);
					int yo = (vall / 1000) * 1000 + valToAdd;
					addressVal.add(yo);
				}
				else if(var.equals("R")) {
					int index = vall % 1000;
					if(index > numOfAddys) {
						System.out.println("Error: Relative address exceeds module size; zero used");
						int yo = (vall / 1000) * 1000;
						addressVal.add(yo);
					}
					else {
						int ho = vall + offset;
						addressVal.add(ho);
					}
					
				}
				else if(var.equals("A")) {
					int num = vall % 1000;
					if(num > 199 || num < 0) {
						int num2 = vall - num;
						addressVal.add(num2);
						System.out.println("Error: Absolute address exceeds machine size; zero used");
					}
					else {
						addressVal.add(vall);
					}
				}
				else if(var.equals("I") && numOfAddys == 1 && numOfUsedSym == 1) {
					System.out.println("Warning: In a module a symbol appeared in the uselist but was not actually used.");
					addressVal.add(vall);
				}
				else {
					addressVal.add(vall);
				}
				
			}
			useList.clear();
			offset += numOfAddys;
		}
		
		
		// /1000 * 1000 + add bs 
		System.out.println("\nMemory Map");
		for(int i = 0; i < constant; i++){
			System.out.println(i + ": " + addressVal.get(i));
		}
		
		for(int i = 0; i < symbols.size(); i++) {
			if((!symUsed.contains(symbols.get(i)))) {
				if(notDef.contains(symbols.get(i))) {
					System.out.println("");
				}
				else{
					System.out.println("Warning " + symbols.get(i) + " was defined but not used");
				}
			}
		}
		
		
		
		

		
		
		
		
	}

}
