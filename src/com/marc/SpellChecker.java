package com.marc;
import java.io.*;
import java.util.*;
/**
 * @author evanxuhe
 * Dico 字典
 *Java字典应用，可以实现字典的增删（txt文件）  以及单词的查询；
 */

public class SpellChecker  {
	private final static String PARENTFOLDER="dics";
	//使用Map存储字典名及词典的内容；
	//使用Set存储无序的单词；
	private Map<String,Set<String>> map=new TreeMap<String,Set<String>>();
	//单词查询
	public boolean contains(String word) {
		System.out.println("查词运行"+map.containsKey("default"));
		for(String dictionary:map.keySet()){
			System.out.println(dictionary);
			if(map.get(dictionary).contains(word)) {
				return true;
			}
		}
		return false;
	}
	//单词联想
	public String[] guess(String word) {
		//Stack栈用于存储联想单词
		Stack<String> guessword = new Stack<String>();
		for(String dictionary:map.keySet()){
			for(String words:map.get(dictionary)){
				if(judge(word,words)){
					guessword.add(words);
				}
			}
		}
		String[] guesses = new String[guessword.size()];
		for(int i = 0; i < guesses.length; i++){
			guesses[i] = guessword.pop();
		}
		return guesses;
	}
	
//	载入字典
	public void load(String name)  {
		File input=new File(PARENTFOLDER,name);
		if(input.exists()){
			if(!map.keySet().contains(name)){
				System.out.println(name+"字典已载入");
				Set<String> words =new HashSet<String>();
				System.out.println(words.size());
				read(name,words);
				System.out.println(words.size());
			    map.put(name,words);
				}else{ 
					System.out.println("文件名已存在");
				}
		}else{System.out.println("文件不存在");}
	}
	
	public List<String> dictionaryList() {
		  List<String> dlist =new ArrayList<String>();
		  for(String dictionary:map.keySet() )
			  dlist.add(dictionary);
		  return dlist;
	}
	
	public void unload(String dictName) {
	    map.keySet().remove(dictName);		
	}
	
	public void addWord(String newWord, String dictName) throws IOException {
		map.get(dictName).add(newWord);
		write(dictName,map.get(dictName));
		
	}
	
	private void print(String call) {
		System.out.println("        Calling " + call + " from SpellChecker");
	}
//	读取字典内容
	private static void read(String inputFile, Set<String> set)   {
		try {
			File filePath = new File(PARENTFOLDER,inputFile);
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String word = null;
			while ( ( word = br.readLine() ) != null ) {
				word = word.trim();
				if ( word != null && word.length() > 0 )
					set.add(word.trim());
			}
			br.close();
		}
		catch ( IOException fnfe ) {}
	}
	
	private static void write(String outputFile, Set<String> set) throws IOException  {
		try {
			File filePath = new File(PARENTFOLDER,outputFile);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
			for ( String word : set )
				pw.println(word);
			pw.close();
		}
		catch ( IOException fnfe ) {}
	}
	
	private boolean judge(String word, String guesswords){
		int count = 0;
		Queue<Integer> index = new LinkedList<Integer>();
		if(word.length() == guesswords.length()){
			for(int j = 0; j < word.length(); j++){
				if (word.toLowerCase().charAt(j) !=guesswords.toLowerCase().charAt(j)){
					count++;
					index.add(j);
				}
			}
			int number = index.poll();
			if(count < 2){
				return true;
			}else if(count == 2 && word.toLowerCase().charAt(number) == guesswords.toLowerCase().charAt(number+1) && word.toLowerCase().charAt(number+1)==guesswords.toLowerCase().charAt(number) ){
				return true;
			}else {
				return false;
			}
		}else if(word.length() == guesswords.length()+1){
			for(int j = 0; j < word.length(); j++){
				if(j< guesswords.length()){
					if(count<1 && word.toLowerCase().charAt(j) != guesswords.toLowerCase().charAt(j) ){
						count++;
					}
				}
				if(j>0){
					if(count >= 1 && word.toLowerCase().charAt(j) != guesswords.toLowerCase().charAt(j-1)){
						count++;
					}
				}
			}
			if(count > 1){
				return false;
			}else {
				return true;
			}
		}else if(word.length() == guesswords.length()-1){
			for(int j = 0; j < word.length()+1; j++){
				if(j< word.length()){
					if( count<1 && word.toLowerCase().charAt(j) != guesswords.toLowerCase().charAt(j) ){
						count++;
					}
				}
				if(j > 0){
					if( count>=1 && word.toLowerCase().charAt(j-1) != guesswords.toLowerCase().charAt(j)){
						count++;
					}
				}
			}
			if(count > 1){
				return false;
			}else {
				return true;
			}
		}else{
			return false;
		}
	
	}
	
}