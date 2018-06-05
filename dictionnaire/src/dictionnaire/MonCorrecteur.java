package dictionnaire;
import dictionnaire.Correcteur ;
import org.eclipse.swt.* ;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.commons.lang3.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Listener;

public class MonCorrecteur extends JFrame implements KeyListener,FocusListener, ActionListener, MouseListener {

  private JPanel container = new JPanel();
  private JPanel boutons = new JPanel () ;
  private JTextArea jtf;   
  private JLabel label = new JLabel();
  private JButton b = new JButton ("Modifier mot selectionné");
  private JButton bc = new JButton ("Importer un Fichier");
  private JComboBox<String> liste1 = new JComboBox<String>();
  private JFileChooser choix = new JFileChooser();
  private JPanel pane = new JPanel(); 
  String ch ="" ;
  String chaine ;
  ArrayList<String> listeMots = new ArrayList<String>() ;
  Boolean mouse =false ;
  Correcteur	c ;
 
  public MonCorrecteur() throws IOException{      
    this.setTitle("Animation");
    this.setSize(800, 400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    container.setBackground(Color.white);
    container.setLayout(new BorderLayout());
    

    try {
  	 c=new Correcteur() ;
  	} catch (IOException e2) {
  		e2.printStackTrace();
  	}
    liste1.setBounds(21, 92, 130, 25); 
    JLabel image = new JLabel( new ImageIcon( "correcteur.png"));
    pane.add(image);
    jtf = new JTextArea();
    JPanel top = new JPanel();      
    Font police = new Font("Arial", Font.BOLD, 14);
    jtf.setFont(police);
    jtf.setPreferredSize(new Dimension(350, 300));
    jtf.setForeground(Color.BLUE);
    jtf.setLineWrap(true);
	//On ajoute les écouteurs à nos composants
    jtf.addKeyListener(this);
    b.addActionListener(this);
    bc.addActionListener(this);
    label.setText("Indicaion");
    jtf.addMouseListener(this);	
    top.add(jtf) ;
    boutons.add(b);
    boutons.add(bc) ;
    boutons.add(liste1) ;
    this.getContentPane().add(label,BorderLayout.SOUTH);
    this.getContentPane().add(pane,BorderLayout.WEST);
    this.getContentPane().add(top,BorderLayout.CENTER);
    this.getContentPane().add(boutons, BorderLayout.NORTH);
    this.setVisible(true);         
  }
  
  
 @Override
public void keyTyped(KeyEvent e) {
	
	 int k = 0 ;
	 jtf.setText(jtf.getText());
	if(e.getKeyChar()!= ' '  ) 
		{
		
		ch=ch+e.getKeyChar() ;
		/*if (e.getKeyChar()=='\b') 
		{
			ch.replace(""+ch.charAt(ch.length()-1),""); System.out.println(ch);
		}*/}
	else 
		
		if (Pattern.matches("[a-zA-Z]+", ch)) 
		try {
			 liste1.removeAllItems();
			 System.out.println(raffinageMot(ch));
			 listeMots=c.testerMot(raffinageMot(ch));
			 String [] motProbable = new String[listeMots.size()]  ;
			 for (int j =0;j<listeMots.size();j++)
			{
				chaine =listeMots.get(j) ;
				liste1.addItem(chaine);
				motProbable[k]=listeMots.get(j) ; 
				k++ ;
			}
			chaine=ch ; //Variable qu'on va utiliser ailleurs
			ch= "" ; // Reinitialisation pour le prochain mot qu'on va taper
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		else ch=""; // Reinitialisation pour le prochain mot qu'on va taper
	
}
	 
		

@Override
public void keyPressed(KeyEvent e) {
	// TODO Auto-generated method stub
	if (e.getKeyCode()==KeyEvent.VK_SHIFT)
	{
		System.out.println(StringUtils.replaceOnce(jtf.getText(), raffinageMot(chaine), (String) liste1.getSelectedItem()));
		jtf.setText(StringUtils.replaceOnce(jtf.getText(), chaine, (String) liste1.getSelectedItem()));
	
	}
}

@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
}

@Override
public void focusGained(org.eclipse.swt.events.FocusEvent arg0) {

}

@Override
public void focusLost(org.eclipse.swt.events.FocusEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	if (e.getSource()==bc)
	{	
		int retour=choix.showOpenDialog(getParent());
		if(retour==JFileChooser.APPROVE_OPTION)
		{
			// un fichier a été choisi (sortie par OK)
			// nom du fichier  choisi 
			BufferedReader lecteurAvecBuffer = null;
		    String ligne;
		    String ch=""; 
		    System.setProperty("file.encoding","Fr-UTF-8");
		    try
		      {
		    	lecteurAvecBuffer = new BufferedReader(new FileReader(choix.getSelectedFile().getAbsolutePath()));
		      }
		    catch(FileNotFoundException exc)
		      {
			System.out.println("Erreur d'ouverture");
		      }
		    try {
				while ((ligne = lecteurAvecBuffer.readLine()) != null)
				{
					ch=ch+ligne+"\n" ; 
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	   // chemin absolu du fichier choisi
	   System.out.println(choix.getSelectedFile().getAbsolutePath());
	   jtf.setText(ch);
	}
}
		
	if(e.getSource()==b)
	{
		if (!liste1.getSelectedItem().equals("Existe"))
		jtf.setText(StringUtils.replaceOnce(jtf.getText(), jtf.getSelectedText(), (String) liste1.getSelectedItem()));
	}
}

@Override
public void mouseClicked(MouseEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void mousePressed(MouseEvent e) {

}

@Override
public void mouseReleased(MouseEvent e) {
	// TODO Auto-generated method stub
	 int k = 0 ;
	if (jtf.getSelectedText() != null)
	{
		try {
			liste1.removeAllItems();
			//System.out.println("ICI");
			//System.out.println(raffinageMot(jtf.getSelectedText()));
			listeMots=c.testerMot(raffinageMot(jtf.getSelectedText()));
			String [] motProbable = new String[listeMots.size()]  ;
		for (int j =0;j<listeMots.size();j++)
		{
			chaine =listeMots.get(j) ;
			liste1.addItem(chaine); //Ajout des mots possibles a la liste de selection
			motProbable[k]=listeMots.get(j) ; k++ ;}
			ch= "" ;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}


@Override
public void mouseEntered(MouseEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void mouseExited(MouseEvent e) {
	// TODO Auto-generated method stub

}

public String raffinageMot(String S) 
{
	if (Pattern.matches("[0-9]+", S))
	S = S.replaceAll("[0-9]","") ;
	S=StringUtils.remove(S, ' ') ;
	S=StringUtils.remove(S, '.') ;
	S=StringUtils.remove(S, ',') ;
	S=StringUtils.remove(S, '\b') ;

	//S=S.replaceAll("m'", "") ;
	S=StringUtils.remove(S, "m'") ;
	S=StringUtils.remove(S, "s'") ;
	S=StringUtils.remove(S, "t'") ;
	S=StringUtils.remove(S, "j'") ;
	S=StringUtils.remove(S, "l'") ;
	return S ;
	
	
	
	
}

public void ajoutElementListe()
{
	int k =0 ;
try {
	liste1.removeAllItems();
	System.out.println(jtf.getSelectedText());
	listeMots=c.testerMot(jtf.getSelectedText());
	String [] motProbable = new String[listeMots.size()]  ;
	for (int j =0;j<listeMots.size();j++)
		{
		chaine =listeMots.get(j) ;
		liste1.addItem(chaine);
		motProbable[k]=listeMots.get(j) ;
		k++ ;
		}
	ch= "" ;
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
}

}

  