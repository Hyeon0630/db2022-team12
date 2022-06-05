package DB2022Team12;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


//<���� Ƽ�� ��ȸ> ��ư�� ���� ������
//���� Ƽ�� ���� DB���� �˻�, Ƽ�� ������ �����ִ� dialog ����
class checkTicketListener implements ActionListener {
	
	// Ƽ�� ���� �˻� ����
	private static String TICKETSEARCH_QUERY = "SELECT id, musical_title, order_date FROM ticket WHERE member_id = ?";
	
	private JLabel myInfoLabel, myTicketLabel1, myTicketLabel2, myTicketLabel3, myTicketLabel4;
	private JButton cancelTicketBtn, closeBtn;
	private JPanel myTicketPanel, BtnPanel;
	private JDialog checkTicketDialog;
		
	@Override
	public void actionPerformed(ActionEvent e) {
		checkTicketDialog = new JDialog();
		checkTicketDialog.setSize(200, 300);
		checkTicketDialog.setTitle("���� Ƽ�� ��ȸ");
		checkTicketDialog.setLayout(new GridLayout(3, 1, 10, 1));
		
		myInfoLabel = new JLabel(User.getName() + "���� TICKET", SwingConstants.CENTER);
		myInfoLabel.setFont(new Font("���", Font.BOLD, 20));
		checkTicketDialog.add(myInfoLabel);
		
		myTicketPanel = new JPanel();
			
		try (Connection conn = new ConnectionClass().getConnection();
				PreparedStatement preStmt = conn.prepareStatement(TICKETSEARCH_QUERY)) {
				
			// ���� Ƽ�� �˻��� ���� ����
			preStmt.setString(1, User.getId());
			ResultSet res = preStmt.executeQuery();
				
			// �ش� ���̵��� Ƽ���� �����ϴ��� Ȯ��
			if (!res.next()) {
				myTicketLabel1 = new JLabel("������ Ƽ���� �����ϴ�.");
				myTicketPanel.add(myTicketLabel1);

				checkTicketDialog.add(myTicketPanel);
				
				BtnPanel = new JPanel();
				closeBtn = new JButton("CLOSE");
				closeBtn.addActionListener(new delePDlgListener());
				BtnPanel.add(closeBtn);

				checkTicketDialog.add(BtnPanel);
				
			} else {
				// ���� ���� ����
				// Musical_id, Member_id, Theater_name, Order_date
				// musical_id, musical_name, theater, orderDate
				UserTicket.ID = res.getInt("id");
				UserTicket.title = res.getString("musical_title");
				UserTicket.orderDate = res.getString("order_date"); 
			
			myTicketLabel2 = new JLabel("Ƽ�� ID : " + UserTicket.ID);
			myTicketLabel3 = new JLabel("���� ���� : " + UserTicket.title);
			myTicketLabel4 = new JLabel("Ƽ�� ���� ��¥ : " + UserTicket.orderDate);
			
			myTicketPanel.add(myTicketLabel2);
			myTicketPanel.add(myTicketLabel3);
			myTicketPanel.add(myTicketLabel4);
			
			checkTicketDialog.add(myTicketPanel);
			
			BtnPanel = new JPanel(new GridLayout(1, 2, 1, 10));
			cancelTicketBtn = new JButton("���� ���");
			cancelTicketBtn.addActionListener(new cancelTicketBtnListener());
			closeBtn = new JButton("CLOSE");
			closeBtn.addActionListener(new delePDlgListener());
			BtnPanel.add(cancelTicketBtn);
			BtnPanel.add(closeBtn);
			
			checkTicketDialog.add(BtnPanel);
			
			}
		} catch (SQLException sqle) {
			System.out.println(sqle);
		}
		
		checkTicketDialog.setVisible(true);
	}
	
	// ���� Ƽ�� ��ȸ dialog���� <CLOSE> ��ư�� ���� ������
	// ���� Ƽ�� ��ȸ dialog �ݱ�
	class delePDlgListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			checkTicketDialog.dispose();
		}
	}
	
	// <���� ���> ��ư�� ���� ������
	// ������ Ƽ�� ������ ���� ��� ���� dialog ����
	class cancelTicketBtnListener implements ActionListener {
		private JLabel deleTicketInfoLabel1, deleTicketInfoLabel2, deleTicketInfoLabel3;
		private JButton deleTBtn;
		private JPanel reserveTicketPanel;
		private JDialog deleTAgreeDialog;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
				deleTAgreeDialog = new JDialog();
				deleTAgreeDialog.setSize(200, 150);
				deleTAgreeDialog.setTitle("���� ���");
				deleTAgreeDialog.setLayout(new GridLayout(2, 1, 10, 5));
				
				reserveTicketPanel = new JPanel(new GridLayout(4, 1, 10, 1));
				
				deleTicketInfoLabel1 = new JLabel("Ƽ�� ID : " + UserTicket.ID);
				deleTicketInfoLabel2 = new JLabel("���� ���� : " + UserTicket.title);
				deleTicketInfoLabel3 = new JLabel("Ƽ�� ���� ��¥ : " + UserTicket.orderDate);
				
				reserveTicketPanel.add(deleTicketInfoLabel1);
				reserveTicketPanel.add(deleTicketInfoLabel2);
				reserveTicketPanel.add(deleTicketInfoLabel3);
				
				deleTAgreeDialog.add(reserveTicketPanel);
				
				deleTBtn = new JButton("���� ��� ����");
				deleTBtn.addActionListener(new delTcheckListener());
				deleTAgreeDialog.add(deleTBtn);
				
				deleTAgreeDialog.setVisible(true);
		
		}
		
		// <���� ��� ����> ��ư�� ���� ������
		// ���� Ƽ�� ���� DB���� ����, ���� ��� �Ϸ� dialog ����
		class delTcheckListener implements ActionListener {
			private JLabel msgTLabel;
			private JButton checkTBtn;	
			private JDialog delTcheckDialog;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				delTcheckDialog = new JDialog();
				delTcheckDialog.setSize(120, 100);
				delTcheckDialog.setLayout(new GridLayout(2, 1, 10, 10));
				
				try (	Connection conn = new ConnectionClass().getConnection();
						Statement preStmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)){
					ResultSet res = preStmt.executeQuery("SELECT * FROM Ticket WHERE Member_id = '" + User.getId() + "'");
					
					// �ش� ���̵��� Ƽ���� �����ϴ��� Ȯ��
					if (res.next()) {
						preStmt.executeUpdate("DELETE FROM Ticket WHERE Member_id = '" + User.getId() + "'");
						
						msgTLabel = new JLabel("���� ��� �Ϸ�", SwingConstants.CENTER);
					} else {
						msgTLabel = new JLabel("ERROR", SwingConstants.CENTER);
					}	
				}catch (SQLException sqle) {
					System.out.println(sqle);
				}
				
				delTcheckDialog.add(msgTLabel);
				
				checkTBtn = new JButton("OK");
				checkTBtn.addActionListener(new deleTDlgListener());
				delTcheckDialog.add(checkTBtn);
				
				delTcheckDialog.setVisible(true);
				
				//���� ��� dialog �ݱ�
				deleTAgreeDialog.dispose();
			}
			
			// Ƽ�� ���� ��� �Ϸ� dialog���� <OK> ��ư�� ���� ������
			// ���� ��� ���� dialog �ݱ�
			class deleTDlgListener implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					delTcheckDialog.dispose();
				}
			}
		}

	}
}




