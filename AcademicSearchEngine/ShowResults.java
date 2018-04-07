import java.awt.Desktop;
import java.awt.Frame;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

public class ShowResults
{
  public static void openResult(String fileName) throws IOException
  {
  
	  // Create something to display it in
    final JEditorPane editor = new JEditorPane();
    editor.setEditable(false);               // making ReadOnly
    editor.setContentType("text/html");      // specifying HTML text
   
    
    JFrame frame = new JFrame(fileName);
    frame.getContentPane().add(new JScrollPane(editor));
    frame.setSize(800,600);
    frame.setVisible(true);
    
    HyperlinkListener hyperlinkListener = new ActivatedHyperlinkListener(
            frame, editor);
    
    editor.addHyperlinkListener(hyperlinkListener);
    
    //Opening HTML file from current directory
    InputStream in = new FileInputStream(fileName);
    editor.read(in, null);
   
    
    // Putting the JEditorPane in a scrolling window and display it
    

  }
  private static void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { /* TODO: error handling */ }
	    } else { /* TODO: error handling */ }
	  }
}



class ActivatedHyperlinkListener implements HyperlinkListener {

	  Frame frame;

	  JEditorPane editorPane;

	  public ActivatedHyperlinkListener(Frame frame, JEditorPane editorPane) {
	    this.frame = frame;
	    this.editorPane = editorPane;
	  }

	  public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
	    HyperlinkEvent.EventType type = hyperlinkEvent.getEventType();
	    final URL url = hyperlinkEvent.getURL();
	    if (type == HyperlinkEvent.EventType.ENTERED) {
	      System.out.println("URL: " + url);
	    } else if (type == HyperlinkEvent.EventType.ACTIVATED) {
	      System.out.println("Activated");
	      Runnable runner = new Runnable() {
	        public void run() {
	          // Retain reference to original
	          Document doc = editorPane.getDocument();
	          try {
//	            editorPane.setPage(url);
	        	  UILayer.statusText.setText("Opening link in your Default Browser...");
	        	  UILayer.currPageStatus.setText("Please Wait...");
	        	  Desktop.getDesktop().browse(new URI(url.toString()));
	        	
	        	  UILayer.statusText.setText("Opened link");
	        	  UILayer.currPageStatus.setText("Success!");

	          } catch (NullPointerException | IOException | URISyntaxException ioException) {
	            JOptionPane.showMessageDialog(frame,
	                "Error following link", "Invalid link",
	                JOptionPane.ERROR_MESSAGE);
	            editorPane.setDocument(doc);
	          }
	        }
	      };
	      SwingUtilities.invokeLater(runner);
	    }
	  }
	}

