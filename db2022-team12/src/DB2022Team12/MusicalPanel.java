package DB2022Team12;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;



public class MusicalPanel extends JPanel{
	
	//������ �˻� ����
	private final String SEARCH_MUSICAL_QUERY = "SELECT Title, Summary FROM Musical WHERE Title = ?";
	//������ ���� �ҷ����� ����
	private final String LOAD_MUSICAL_QUERY = "SELECT Title, Summary FROM Musical";
	//���� ��
	private final String PRICE_VIEW_1 = "SELECT * FROM under_10";
	private final String PRICE_VIEW_2 = "SELECT * FROM between10_15";
	private final String PRICE_VIEW_3 = "SELECT * FROM over_15";
	//���� �� �˻�
	private final String PRICE1_MUSICAL_QUERY = "SELECT Title, Summary FROM under_10 WHERE Title = ?";
	private final String PRICE2_MUSICAL_QUERY = "SELECT Title, Summary FROM between10_15 WHERE Title = ?";
	private final String PRICE3_MUSICAL_QUERY = "SELECT Title, Summary FROM over_15 WHERE Title = ?";

	private JPanel searchPanel, listPanel;
	private JLabel searchLabel, resultLabel, fakeLabel;
	private JLabel titleLabel, dateLabel, timeLabel, theaterLabel, summaryLabel, reviewLabel;
	private JButton searchBtn, buyBtn; 
	private JRadioButton under10Btn, bw1015Btn, over15Btn, allBtn;
	private JTextField searchTextField;
	private JList<String> resultList;
	
	private JDialog infoDialog, buyDialog;
	
	public Vector<String> getMusicals(String query){
		Connection conn;
		Vector<String> musicalList = new Vector<String>();
		
		try {
			conn = new ConnectionClass().getConnection();
			PreparedStatement preStmt = conn.prepareStatement(query);
			ResultSet res = preStmt.executeQuery();
			
			// Fetch each row from the result set
			while(res.next()) {
			  String title = res.getString("Title");
			  String summary = res.getString("Summary");

			  musicalList.add(title + "  " + summary + " �������� ");

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(musicalList.isEmpty()) {
			musicalList.add("�˻��Ͻ� �������� �������� �ʽ��ϴ�.");
		}
		return musicalList;

	}
	// title search
	public Vector<String> getMusicals(String query, String searchkey){
		Connection conn;
		Vector<String> musicalList = new Vector<String>();

		
		try {
			conn = new ConnectionClass().getConnection();
			PreparedStatement preStmt = conn.prepareStatement(query);
			preStmt.setString(1,searchkey);
			ResultSet res = preStmt.executeQuery();
			
			// Fetch each row from the result set
			while(res.next()) {
			  String title = res.getString("Title");
			  String summary = res.getString("Summary");

			  musicalList.add(title + "  " + summary + " �������� ");

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(musicalList.isEmpty()) {
			musicalList.add("�˻��Ͻ� �������� �������� �ʽ��ϴ�.");
		}
		return musicalList;

	}
	
	public MusicalPanel() {
		
		this.setLayout(new BorderLayout(10,10));
		
		searchPanel = new JPanel();
		//searchPanel.setSize(1200, 50);
		this.add(searchPanel, BorderLayout.NORTH);
		
		listPanel = new JPanel();
		//listPanel.setSize(1200, 250);
		this.add(listPanel, BorderLayout.SOUTH);
		
		searchPanel.setLayout(new GridLayout(2,4));
		
		searchLabel = new JLabel("                                         ������ ��ȸ : ");

		searchPanel.add(searchLabel);
		
		searchTextField = new JTextField(20);
		searchPanel.add(searchTextField);
		
		searchBtn = new JButton("Search");
		searchPanel.add(searchBtn);
		searchBtn.addActionListener(new searchBtnListner());
		
		fakeLabel = new JLabel("                ");
		searchPanel.add(fakeLabel);
		
		ButtonGroup group = new ButtonGroup();
		
		allBtn = new JRadioButton("��ü");
		searchPanel.add(allBtn);
		allBtn.addActionListener(new priceBtnListner(LOAD_MUSICAL_QUERY));
		group.add(allBtn);
		
		under10Btn = new JRadioButton("~10����");
		searchPanel.add(under10Btn);
		under10Btn.addActionListener(new priceBtnListner(PRICE_VIEW_1));
		group.add(under10Btn);
		
		bw1015Btn = new JRadioButton("10����~15����");
		searchPanel.add(bw1015Btn);
		bw1015Btn.addActionListener(new priceBtnListner(PRICE_VIEW_2));
		group.add(bw1015Btn);
		
		over15Btn = new JRadioButton("15����~");
		searchPanel.add(over15Btn);
		over15Btn.addActionListener(new priceBtnListner(PRICE_VIEW_3));
		group.add(over15Btn);
		
		
		resultList = new JList<String>(getMusicals(LOAD_MUSICAL_QUERY));
		// musicalList = new JList<String>(getMusicals(LOAD_MUSICAL_QUERY));
		resultList.addMouseListener(mouseListener);
		listPanel.add(resultList);
		listPanel.add(new JScrollPane(resultList));
		
		setVisible(true);
	}
	

	// <�˻�> ��ư�� ���� ������
	// DB�� ����ڷκ��� ���� �Է°� ��ġ�ϴ� �����Ͱ� �����ϴ��� Ȯ�� �� ����
	private class searchBtnListner implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String title = searchTextField.getText().strip();
			searchTextField.setText("");
			
			// ����� �Է� ��ȿ�� ����
			if (title.isEmpty()) {
				searchTextField.setText("������ ������ �Է����ּ���.");
			} 
			else if(under10Btn.isSelected()){
				resultList.setListData(getMusicals(PRICE1_MUSICAL_QUERY,title));
			}
			else if(bw1015Btn.isSelected()){
				resultList.setListData(getMusicals(PRICE2_MUSICAL_QUERY,title));
			} 
			else if(over15Btn.isSelected()){
				resultList.setListData(getMusicals(PRICE3_MUSICAL_QUERY,title));
			} 
			else {
				resultList.setListData(getMusicals(SEARCH_MUSICAL_QUERY,title));
				
			}
		}
	}
	
	//���� ��ư
	private class priceBtnListner implements ActionListener {
		String view;
		
		priceBtnListner(String view){
			this.view = view;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			resultList.setListData(getMusicals(view));
		}
	}
	
	// JList click listener
	MouseListener mouseListener = new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
	        if (e.getClickCount() == 2) {

	        	//����������
	        	String selectedMusical = resultList.getSelectedValue();
	        	String [] text = selectedMusical.split(" ");
	        	String selectedtitle = text[0].strip();
	        	
	        	System.out.print(selectedtitle);
	        	
    			infoDialog = new JDialog();
    			infoDialog.setSize(500, 400);
    			infoDialog.setLayout(new GridLayout(3, 1));

    			JPanel topPanel = new JPanel(new GridLayout(4, 1));
    			JPanel midPanel = new JPanel();
    			JPanel botPanel = new JPanel();
    			
    			Musical musical = new Musical(selectedtitle);

    			titleLabel = new JLabel("  ����  " + musical.getTitle());
    			theaterLabel = new JLabel("  ����  " + musical.getTheaterName());
    			summaryLabel = new JLabel("  �ٰŸ�  "  + musical.getSummary());
    			reviewLabel = new JLabel("  ��պ���  " + musical.getAvgRate());
    			topPanel.add(titleLabel);			
    			topPanel.add(theaterLabel);
    			topPanel.add(summaryLabel);
    			topPanel.add(reviewLabel);
    			
    			//buyBtn= new JButton("�����ϱ�");
    			//buyBtn.addActionListener(new buyBtnListener());
    			//botPanel.add(buyBtn);
    			TicketPanel ticketPanel = new TicketPanel(musical.getTitle());
    			midPanel.add(ticketPanel);

    			infoDialog.add(topPanel);
    			infoDialog.add(midPanel);
    			infoDialog.add(botPanel);
    			infoDialog.setVisible(true);
	        }
	    }
	    
	};
	
}