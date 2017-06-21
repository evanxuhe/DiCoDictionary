package com.marc;
/**
 * @author evanxuhe
 * 字典AWT图形界面
 */

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.*;


public class GraphicSpellChecker implements ActionListener  {

   	private JFrame appFrame;
   	
   	private JTextField jInput;
   	private JButton jCheck, jClear, jLoad, jQuit;
   	private JTextArea jGuesses;
   	private JLabel jMsg;
   	
   	private JMenuBar menuBar;
   	private JMenu menuFile;
	private JMenuItem exitMenuItem;

	private JMenu menuList;
	private JMenuItem defaultMenuItem;
	private JMenu menuAdd;
	
	private ActionListener menuAddListener;
	
	private final JFileChooser fc = new JFileChooser();
	private SpellChecker sc;
	private final static String PARENTFOLDER="dics";
   	
   	public GraphicSpellChecker() throws FileNotFoundException {
   		menuAddListener = new MenuAddListener();
   		appFrame = new JFrame("DiCo");
   		createAndPlaceComponents();
   		appFrame.setResizable(true);
   		appFrame.pack();
   		appFrame.setVisible(true);
   		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   		sc = new SpellChecker();
   		loadDefault();
   	}
   	
   	public void createAndPlaceComponents() {
   		menuBar = new JMenuBar();
		menuFile = new JMenu("File");
		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(this);
		menuFile.add(exitMenuItem);
		menuBar.add(menuFile);
		menuList = new JMenu("Dictionaries");
		defaultMenuItem = new JMenuItem("Set as default");
		defaultMenuItem.addActionListener(this);
		menuList.add(defaultMenuItem);
		menuList.addSeparator();
		menuBar.add(menuList);
		menuAdd = new JMenu("Add");
		menuBar.add(menuAdd);
		
		jInput = new JTextField(15);
		jGuesses = new JTextArea(5,15);
		JScrollPane scrollPane = new JScrollPane(jGuesses);
		JPanel left = new JPanel(new BorderLayout());
		left.add(jInput,BorderLayout.NORTH);
		left.add(scrollPane,BorderLayout.SOUTH);
		
		jCheck = new JButton("查词");
		//jCheck.setPreferredSize(new Dimension(25,10));
		jCheck.addActionListener(this);
		jClear = new JButton("清空");
		jClear.addActionListener(this);
		jLoad = new JButton("载入");
		jLoad.addActionListener(this);
		jQuit = new JButton("退出");
		jQuit.addActionListener(this);
		
		JPanel right = new JPanel();
		right.setLayout(new GridLayout(4,1));
		right.add(jCheck);
		right.add(jLoad);
		right.add(jClear);
		right.add(jQuit);
		
		JPanel top = new JPanel(new BorderLayout());
		top.add(left,BorderLayout.WEST);
		top.add(right,BorderLayout.EAST);
		
		jMsg = new JLabel("Enter a word to check");
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main,BoxLayout.PAGE_AXIS));
		main.add(top);
		main.add(jMsg);
		
		appFrame.setJMenuBar(menuBar);
		appFrame.setContentPane(main);
   	}
   	
   	public void actionPerformed (ActionEvent e) {
   		if ( e.getSource() == jCheck ) {
   			doCheck();
   		} else if ( e.getSource() == jClear) {
   			doClear();
   		} else if ( e.getSource() == jLoad ) {
   			doLoad();
   		} else if ( e.getSource() == jQuit ) {
   			doQuit();
   		} else if ( e.getSource() == exitMenuItem ) {
   			doQuit();
   		} else if ( e.getSource() == defaultMenuItem ) {
   			try {
				doDefault();
			} catch (FileNotFoundException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
   		} else {
   			doUnload(e);
   		}
   	}
   	
   	private void doCheck() {
   		print("doCheck()");
   		String word = jInput.getText();
   		jGuesses.setText("");
		if ( sc.contains(word) )
			jMsg.setText(word + " 存在");
		else {
			jMsg.setText(word + " 不存在");
			String output = "Maybe you meant:";
			String[] guesses = sc.guess(word);
			for ( int i = 0; i < guesses.length; i++ )
				output += "\n" + guesses[i];
			jGuesses.setText(output);
		}  		
   	}
 
   	private void doClear() {
   		print("doClear()");
   		jInput.setText("");
   		jGuesses.setText("");
   		jMsg.setText(" ");
   	}
   	
   	private void doLoad() {
   		print("doLoad()");
  		int returnVal = fc.showOpenDialog(appFrame);
   		String name = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            name = fc.getSelectedFile().getName();
            if(!checksamefile(name)){
            try {
            	sc.load(name);
            }
            catch ( Exception e ) {
            	jMsg.setText("Problem loading " + name);
            	return;
            }
    		JMenuItem menuItem = new JMenuItem(name);
    		menuItem.addActionListener(this);
    		menuList.add(menuItem);
			menuItem = new JMenuItem(name);
			menuItem.addActionListener(menuAddListener);
			menuAdd.add(menuItem);
        }
        }
   	}
   	private boolean checksamefile(String name){
   		for(int i=0;i<sc.dictionaryList().size();i++){
   			if(sc.dictionaryList().get(i).equals(name))return true;
   		}
   		return false;
   			
   		
   				
   			
   	}
   	
   	private void doQuit() {
   		print("doQuit()");
   		System.exit(0);
   	}
   	
   	private void doDefault() throws FileNotFoundException {
   		print("doDefault()");
   		PrintStream Default = new PrintStream(new File("D:\\dictionary\\default.txt"));
		for(int i=0;i<sc.dictionaryList().size();i++)
			Default.println(sc.dictionaryList().get(i));
   	}
   	
   	private void doUnload(ActionEvent e) {
   		print("doUnload()");
   		if ( JOptionPane.showConfirmDialog(appFrame,"Remove " + e.getActionCommand() + "?","Unloading a dictionary",JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION ) {
   			menuList.remove((JMenuItem) e.getSource());
   			sc.unload(e.getActionCommand());
		}
   	}
  	
	private void loadDefault() throws FileNotFoundException {
		print("loadDefaut()");
		System.out.print(">载入默认字典:");
		File defaultFile =new File(PARENTFOLDER,"default.txt");
		if(defaultFile.exists()&&defaultFile.length()>0){	
			Scanner LDefault=new Scanner(defaultFile);
			while(LDefault.hasNextLine()){
				String name=LDefault.nextLine();
				System.out.println(name);
				sc.load(name);
				JMenuItem menuItem = new JMenuItem(name);
				menuItem.addActionListener(this);
				menuList.add(menuItem);
				menuItem = new JMenuItem(name);
				menuItem.addActionListener(menuAddListener);
				menuAdd.add(menuItem);
			}
		}
		
	}

  	
	private class MenuAddListener implements ActionListener {
		
		public void actionPerformed (ActionEvent e) {
			try {
				sc.addWord(jInput.getText(),e.getActionCommand());
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
	}
   	
	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					new GraphicSpellChecker();
				} catch (FileNotFoundException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
            }
        });
	}
	
	private void print(String call) {
		System.out.println("Calling " + call + " from GraphicSpellChecker");
	}	
}
