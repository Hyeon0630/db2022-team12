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


//<�ۼ� ���� ����> ��ư�� ���� ������
//�ۼ� ���� ������ ���� ���� dialog ����
class reviewBtnListener implements ActionListener {
	
	private JDialog myreviewDialog;
	private JLabel myreviewLabel, ReviewinfoLabel1, ReviewinfoLabel2, ReviewinfoLabel3, ReviewinfoLabel4, ReviewinfoLabel5;
	private JButton deleRBtn, closeRBtn;
	private JPanel myreviewPanel, BtnRPanel;
	
	// ���� ���� �˻� ����
	private static String REVIEWSEARCH_QUERY = "SELECT id, musical_title, rate, written_at FROM Review WHERE member_id = ?";
	
	@Override
	public void actionPerformed(ActionEvent e) {
		myreviewDialog = new JDialog();
		myreviewDialog.setSize(400, 250);
		myreviewDialog.setTitle("�ۼ� ���� ����");
		myreviewDialog.setLayout(new GridLayout(3, 1, 10, 20));
		
		myreviewLabel = new JLabel(User.getName() + "���� REVIEW", SwingConstants.CENTER);
		myreviewLabel.setFont(new Font("���", Font.BOLD, 20));
		myreviewDialog.add(myreviewLabel);
		
		myreviewPanel = new JPanel();
		
		try (Connection conn = new ConnectionClass().getConnection();
				PreparedStatement preStmt = conn.prepareStatement(REVIEWSEARCH_QUERY)) {
				
			// ���� Ƽ�� �˻��� ���� ����
			preStmt.setString(1, User.getId());
			ResultSet res = preStmt.executeQuery();
				
			// �ش� ���̵��� ���䰡 �����ϴ��� Ȯ��
			if (!res.next()) {
				ReviewinfoLabel1 = new JLabel("�ۼ��� ���䰡 �����ϴ�.");
				myreviewPanel.add(ReviewinfoLabel1);

				myreviewDialog.add(myreviewPanel);
				
				BtnRPanel = new JPanel();
				closeRBtn = new JButton("CLOSE");
				closeRBtn.addActionListener(new deleRDlgListener());
				BtnRPanel.add(closeRBtn);

				myreviewDialog.add(BtnRPanel);
			} else {
				// ���� ���� ���� ����
				// ID, Musical_id, Rate, Written_at
				// review_id. musical_id, musical_name, rate, written_at
				UserReview.ID = res.getInt("id");
				UserReview.title = res.getString("musical_title");
				UserReview.rate = res.getInt("rate");
				UserReview.time = res.getString("written_at");
			
			// ID, Musical_id, Rate, Written_at
			// review_id. musical_id, musical_name, rate, written_at
			ReviewinfoLabel2 = new JLabel("����ID : " + UserReview.ID);
			ReviewinfoLabel3 = new JLabel("���� ���� : " + UserReview.title);
			ReviewinfoLabel4 = new JLabel("���� : " + UserReview.rate);
			ReviewinfoLabel5 = new JLabel("���� �ۼ� ��¥ : " + UserReview.time);
			
			myreviewPanel.add(ReviewinfoLabel2);
			myreviewPanel.add(ReviewinfoLabel3);
			myreviewPanel.add(ReviewinfoLabel4);
			myreviewPanel.add(ReviewinfoLabel5);
			
			myreviewDialog.add(myreviewPanel);
			
			BtnRPanel = new JPanel();
			
			deleRBtn = new JButton("���� ��ü ����");
			deleRBtn.addActionListener(new delRcheckListener());
			closeRBtn = new JButton("CLOSE");
			closeRBtn.addActionListener(new deleRDlgListener());
			
			BtnRPanel.add(deleRBtn);
			BtnRPanel.add(closeRBtn);

			myreviewDialog.add(BtnRPanel);
			}
		} catch (SQLException sqle) {
			System.out.println(sqle);
		}
		
		myreviewDialog.setVisible(true);
	}
	
	// �ۼ� ���� ���� dialog���� <CLOSE> ��ư�� ���� ������
	// �ۼ� ���� ���� dialog �ݱ�
	class deleRDlgListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			myreviewDialog.dispose();
		}
	}
	
	// �ۼ� ���� ���� dialog���� <���� ��ü ����> ��ư�� ���� ������
	// ������ ���並 DB���� ����, ���� �Ϸ� dialog ����
	class delRcheckListener implements ActionListener {
		private JLabel msgRLabel;
		private JButton checkRBtn;
		private JDialog delRcheckDialog;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			delRcheckDialog = new JDialog();
			delRcheckDialog.setSize(250, 100);
			delRcheckDialog.setTitle("���� ����");
			delRcheckDialog.setLayout(new GridLayout(2, 1, 10, 10));
			
			try (	Connection conn = new ConnectionClass().getConnection();
					Statement preStmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)){
				ResultSet res = preStmt.executeQuery("SELECT * FROM Review WHERE Member_id = '" + User.getId() + "'");
				
				// �ش� ���̵��� Ƽ���� �����ϴ��� Ȯ��
				if (res.next()) {
					preStmt.executeUpdate("DELETE FROM Review WHERE Member_id = '" + User.getId() + "'");
					msgRLabel = new JLabel("�ۼ��� ���䰡 ��� �����Ǿ����ϴ�.", SwingConstants.CENTER);
					
				} else {
					msgRLabel = new JLabel("ERROR", SwingConstants.CENTER);
				}	
			}catch (SQLException sqle) {
				System.out.println(sqle);
			}
			
			delRcheckDialog.add(msgRLabel);
			
			checkRBtn = new JButton("OK");
			checkRBtn.addActionListener(new deleODlgListener());
			delRcheckDialog.add(checkRBtn);
			
			delRcheckDialog.setVisible(true);
			
			//�ۼ� ���� ���� dialog �ݱ�
			myreviewDialog.dispose();
		}
		
		// ���� ���� dialog���� <OK> ��ư�� ���� ������
		// ���� ���� dialog �ݱ�
		class deleODlgListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				delRcheckDialog.dispose();
			}
		}
	}
}

