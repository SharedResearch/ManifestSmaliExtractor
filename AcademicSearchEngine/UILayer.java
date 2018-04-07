import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JSlider;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import java.awt.Canvas;


public class UILayer extends JFrame {

	private JPanel contentPane;
	private JTextField searchEditText;
	public static JTextField statusText;
	private JLabel lblStatus;
	private JButton btnACM;
	private JTextField textFieldIP;
	private JTextField textFieldPort;
	private JLabel lblIP;
	private JLabel lblPort;
	public static JCheckBox chckbxAcm;
	public static JCheckBox chckbxIeee;
	public static JCheckBox chckbxScienceDirect;
	public static JCheckBox chckbxIosPress;
	public static JCheckBox chckbxSpringer;
	public static JCheckBox chckbxSiteSeerX;
	public static JCheckBox chckbxInderScience;
	public static JCheckBox chckbxWiley;
	public static JCheckBox checkBoxYearsFilter;
	public static JComboBox startYearComboBox;
	public static JComboBox endYearComboBox;
	private JButton buttonIEEE;
	private JButton buttonScienceDirect;
	private JButton buttonIOS;
	private JButton buttonSpringer;
	private JButton buttonCiteSeerX;
	private JButton buttonInderScience;
	private JButton buttonWiley;
	public JSpinner spinnerPageDepth;
	private JSeparator separator;
	private JLabel lblStartYear;
	public static JTextField currPageStatus;
	private JButton buttonProcessData;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UILayer frame = new UILayer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public UILayer() {
		setTitle("Academic Search Engine");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton submitButton = new JButton("Search");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Scrapper.TOTAL_PAGES_DEPTH=Integer.parseInt(spinnerPageDepth.getValue().toString());
					
					if(searchEditText.getText().length()>0 && Scrapper.TOTAL_PAGES_DEPTH >0)
					{
						Thread queryThread = new Thread() {
						      public void run() {
						    		try {
										Scrapper.main1(searchEditText.getText());
									} catch (Exception e) {
										
										statusText.setText("Internet Connection Timed Out");
										e.printStackTrace();
									}
						      }
						};
						queryThread.start();
					}
					else
					{
						statusText.setText("Select Proper Page Depth and Search String");
						//System.exit(0);
					}	
							
					
				} catch (Exception e) {
					statusText.setText("Internet Connection Timed Out");	
				}
			}
		});
		
		submitButton.setBounds(595, 57, 124, 23);
		contentPane.add(submitButton);
		
		
		searchEditText = new JTextField();
		searchEditText.setBounds(232, 58, 310, 20);
		contentPane.add(searchEditText);
		searchEditText.setColumns(10);
		
		JLabel lblEnterSearchString = new JLabel("Enter Search String");
		lblEnterSearchString.setBounds(99, 61, 124, 14);
		contentPane.add(lblEnterSearchString);
		
		statusText = new JTextField();
		statusText.setEditable(false);
		statusText.setBounds(10, 527, 495, 23);
		contentPane.add(statusText);
		statusText.setColumns(10);
		
		lblStatus = new JLabel("Status:");
		lblStatus.setBounds(10, 505, 46, 14);
		contentPane.add(lblStatus);
		
		btnACM = new JButton("Open Result");
		btnACM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			try {
				ShowResults.openResult(Scrapper.OutputACM);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				statusText.setText("Error Opening Results. File Not Found");
			}
			}
		});
		btnACM.setBounds(284, 241, 124, 14);
		contentPane.add(btnACM);
		
		final JCheckBox chckbxSetProxy = new JCheckBox("Set Proxy");
		chckbxSetProxy.setBounds(477, 329, 97, 23);
		contentPane.add(chckbxSetProxy);
		
		textFieldIP = new JTextField();
		textFieldIP.setEnabled(false);
		textFieldIP.setText("192.168.1.10");
		textFieldIP.setBounds(593, 376, 86, 20);
		contentPane.add(textFieldIP);
		textFieldIP.setColumns(10);
		
		textFieldPort = new JTextField();
		textFieldPort.setEnabled(false);
		textFieldPort.setText("8080");
		textFieldPort.setBounds(593, 422, 38, 20);
		contentPane.add(textFieldPort);
		textFieldPort.setColumns(10);
		
		lblIP = new JLabel("IP Address");
		lblIP.setEnabled(false);
		lblIP.setBounds(510, 379, 71, 14);
		contentPane.add(lblIP);
		
		lblPort = new JLabel("Port#");
		lblPort.setEnabled(false);
		lblPort.setBounds(510, 425, 39, 14);
		contentPane.add(lblPort);
		
		spinnerPageDepth = new JSpinner();
		spinnerPageDepth.setModel(new SpinnerNumberModel(new Integer(1), null, null, new Integer(1)));
		spinnerPageDepth.setBounds(232, 100, 46, 20);
		contentPane.add(spinnerPageDepth);
		
		
		JLabel lblSelectPageDepth = new JLabel("Select Page Depth");
		lblSelectPageDepth.setBounds(99, 103, 111, 14);
		contentPane.add(lblSelectPageDepth);
		
		chckbxAcm = new JCheckBox("ACM Digital Library");
		chckbxAcm.setBounds(99, 237, 185, 23);
		contentPane.add(chckbxAcm);
		
		chckbxIeee = new JCheckBox("IEEE Xplorer");
		chckbxIeee.setBounds(99, 263, 185, 23);
		contentPane.add(chckbxIeee);
		
		chckbxScienceDirect = new JCheckBox("Science Direct");
		chckbxScienceDirect.setBounds(99, 289, 185, 23);
		contentPane.add(chckbxScienceDirect);
		
		chckbxIosPress = new JCheckBox("IOS Press");
		chckbxIosPress.setBounds(99, 315, 178, 23);
		contentPane.add(chckbxIosPress);
		
		chckbxSpringer = new JCheckBox("Springer");
		chckbxSpringer.setBounds(99, 211, 155, 23);
		contentPane.add(chckbxSpringer);
		
		chckbxSiteSeerX = new JCheckBox("Cite Seer X");
		chckbxSiteSeerX.setBounds(99, 341, 169, 23);
		contentPane.add(chckbxSiteSeerX);
		
		chckbxInderScience = new JCheckBox("Inder Science");
		chckbxInderScience.setBounds(99, 185, 155, 23);
		contentPane.add(chckbxInderScience);
		
		chckbxWiley = new JCheckBox("Wiley");
		chckbxWiley.setBounds(99, 367, 97, 23);
		contentPane.add(chckbxWiley);
		
		buttonIEEE = new JButton("Open Result");
		buttonIEEE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ShowResults.openResult(Scrapper.OutputIEEE);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
					statusText.setText("Error Opening Results. File Not Found");
				}
				}
			});
		buttonIEEE.setBounds(284, 267, 124, 14);
		contentPane.add(buttonIEEE);
		
		buttonScienceDirect = new JButton("Open Result");
		buttonScienceDirect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ShowResults.openResult(Scrapper.OutputScienceDirect);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
					statusText.setText("Error Opening Results. File Not Found");
				}
				}
			});
		buttonScienceDirect.setBounds(284, 293, 124, 14);
		contentPane.add(buttonScienceDirect);
		
		buttonIOS = new JButton("Open Result");
		buttonIOS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ShowResults.openResult(Scrapper.OutputIOSPress);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
					statusText.setText("Error Opening Results. File Not Found");
				}
				}
			});
		buttonIOS.setBounds(284, 319, 124, 14);
		contentPane.add(buttonIOS);
		
		buttonSpringer = new JButton("Open Result");
		buttonSpringer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ShowResults.openResult(Scrapper.OutputSpringer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
					statusText.setText("Error Opening Results. File Not Found");
				}
				}
			});
		buttonSpringer.setBounds(284, 215, 124, 14);
		contentPane.add(buttonSpringer);
		
		buttonCiteSeerX = new JButton("Open Result");
		buttonCiteSeerX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ShowResults.openResult(Scrapper.OutputCiteSeerX);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
					statusText.setText("Error Opening Results. File Not Found");
				}
				}
			});
		buttonCiteSeerX.setBounds(284, 345, 124, 14);
		contentPane.add(buttonCiteSeerX);
		
		buttonInderScience = new JButton("Open Result");
		buttonInderScience.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ShowResults.openResult(Scrapper.OutputInderScience);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
					statusText.setText("Error Opening Results. File Not Found");
				}
				}
			});
		buttonInderScience.setBounds(284, 189, 124, 14);
		contentPane.add(buttonInderScience);
		
		buttonWiley = new JButton("Open Result");
		buttonWiley.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ShowResults.openResult(Scrapper.OutputWiley);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
					statusText.setText("Error Opening Results. File Not Found");
				}
				}
			});
		buttonWiley.setBounds(284, 371, 124, 14);
		contentPane.add(buttonWiley);
		
		JLabel lblSelectTheDatabase = new JLabel("Select the Database Websites you want to Search");
		lblSelectTheDatabase.setBounds(99, 159, 302, 14);
		contentPane.add(lblSelectTheDatabase);
		
		startYearComboBox = new JComboBox();
		
		startYearComboBox.setEnabled(false);
		
		for(int i=1980;i<=2015;i++)		
		startYearComboBox.addItem(i);
		startYearComboBox.setSelectedItem(2014);
		
		
		startYearComboBox.setBounds(593, 198, 111, 22);
		contentPane.add(startYearComboBox);
		
		separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(467, 146, 22, 330);
		contentPane.add(separator);
		
		lblStartYear = new JLabel("Start Year");
		lblStartYear.setBounds(512, 202, 78, 14);
		lblStartYear.setEnabled(false);
		contentPane.add(lblStartYear);
		
		endYearComboBox = new JComboBox();
		endYearComboBox.setBounds(593, 264, 111, 22);
		endYearComboBox.setEnabled(false);
		contentPane.add(endYearComboBox);
		

		for(int i=1980;i<=2015;i++)		
			endYearComboBox.addItem(i);
			
		endYearComboBox.setSelectedItem(2015);
		
		
		final JLabel lblEndYear = new JLabel("End Year");
		lblEndYear.setBounds(512, 268, 78, 14);
		lblEndYear.setEnabled(false);
		contentPane.add(lblEndYear);
		
		JButton btnNewButton = new JButton("View Combined Results");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	
			//	Thread queryThread = new Thread() {
			//	      public void run() {
				    	  try {
				    		  	statusText.setText("Combining All Results....");
				    		  	currPageStatus.setText("Please Wait....");
								Scrapper.CombineResults();
								ShowResults.openResult(Scrapper.OutputALL);
							} catch (IOException e) {	
								statusText.setText("Error Opening Results.");
							}
				    	  statusText.setText("Results Combined Successfully");
			    		  currPageStatus.setText("Combination Completed!");
				      }    
			//	};
			//	queryThread.start();
				
			//}
		});
		
		btnNewButton.setBounds(55, 435, 178, 23);
		contentPane.add(btnNewButton);
		
		checkBoxYearsFilter = new JCheckBox("Enable Filter");
		checkBoxYearsFilter.setBounds(477, 159, 97, 23);
		contentPane.add(checkBoxYearsFilter);
		
		currPageStatus = new JTextField();
		currPageStatus.setEditable(false);
		currPageStatus.setColumns(10);
		currPageStatus.setBounds(544, 528, 225, 23);
		contentPane.add(currPageStatus);
		
		JLabel lblCurrentPage = new JLabel("Current Page Progress :");
		lblCurrentPage.setBounds(544, 505, 141, 14);
		contentPane.add(lblCurrentPage);
		
		buttonProcessData = new JButton("Process Results");
		buttonProcessData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					
				Thread queryThread = new Thread() {
				      public void run() {
				    	  
				    	  statusText.setText("Combining Results...");
			    		  currPageStatus.setText("Please Wait...");
				    	  
			    		  try {
							Scrapper.CombineResults();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		  
			    		  statusText.setText("Gathering Results...");
			    		  currPageStatus.setText("Please Wait...");
				    	  Scrapper.populateList();
				    	  
				    	  statusText.setText("Processing Results...");
			    		  currPageStatus.setText("Please Wait...");
				    	  Scrapper.ProcessResults();
				    	  Scrapper.WriteProcessedDataToFile();
				    	  statusText.setText("Results Processed Successfully");
			    		  currPageStatus.setText("Opening Processed Results!");
			    		  try {
							ShowResults.openResult(Scrapper.OutputProcessed);
			    		  }
			    		  catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		  
				      }    
				};
				queryThread.start();
				
				
				
				
				
				
				
			}
		});
		buttonProcessData.setBounds(264, 421, 178, 23);
		contentPane.add(buttonProcessData);
		
		JButton btnViewProcessedResults = new JButton("View Processed Results");
		btnViewProcessedResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				Thread queryThread = new Thread() {
				      public void run() {
				    	  
				    	  statusText.setText("Opening Results...");
			    		  currPageStatus.setText("Please Wait...");
				    	  
				    	  try {
								ShowResults.openResult(Scrapper.OutputProcessed);
							} catch (IOException e) {
								e.printStackTrace();
							}
				    	  statusText.setText("Results Opened Successfully");
			    		  currPageStatus.setText("Completed!");
				      }    
				};
				queryThread.start();
			}
		});
		btnViewProcessedResults.setBounds(264, 453, 178, 23);
		contentPane.add(btnViewProcessedResults);
		chckbxSetProxy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
							
				textFieldPort.setEnabled(chckbxSetProxy.isSelected());
				textFieldIP.setEnabled(chckbxSetProxy.isSelected());
				Scrapper.PROXY = textFieldIP.getText()+":"+textFieldPort.getText();
				Scrapper.enableProxy=chckbxSetProxy.isSelected();
				statusText.setText("Proxy Set to: "+chckbxSetProxy.isSelected());
				
			}
		});
		
		checkBoxYearsFilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				lblStartYear.setEnabled(checkBoxYearsFilter.isSelected());
				lblEndYear.setEnabled(checkBoxYearsFilter.isSelected());
				startYearComboBox.setEnabled(checkBoxYearsFilter.isSelected());
				endYearComboBox.setEnabled(checkBoxYearsFilter.isSelected());
				statusText.setText("Years Filter: "+checkBoxYearsFilter.isSelected());
				
			}
		});
		
	}
}
