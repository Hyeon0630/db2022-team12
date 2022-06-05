package DB2022Team12;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

class ReviewInsertionPanel extends JPanel {
	
	private JPanel noticePanel, reviewPanel, scorePanel, btnPanel;
	private JLabel noticeLabel;
	private JRadioButton[] scoreBtn;
	private JButton submitBtn;
	private ButtonGroup btnGroup;

	// ���並 �ۼ��Ϸ��� ������ ������ ���� Musical ��ü
	private Musical musical;
	
	// ����(0~5)
	private final static String[] scoreList = {"��", "�ڡ�", "�ڡڡ�", "�ڡڡڡ�", "�ڡڡڡڡ�"};
	
	// ����ڰ� �ۼ��� ���並 DB�� �����ϴ� ����
	private final static String INSERT_REVIEW_QUERY = "INSERT INTO review(musical_title, member_id, rate, written_at) "
			+ "VALUES (?, ?, ?, ?)";
	
	// ���� �ۼ� �г� ���̾ƿ� ����
	// ���並 �ۼ��Ϸ��� ������ ������ ���� Musical ��ü�� ���� ����
	public ReviewInsertionPanel(Musical musical) {
		this.musical = musical;
		this.setSize(300, 100);
		this.setLayout(new GridLayout(2, 1));
		
		noticePanel = new JPanel();
		reviewPanel = new JPanel();

		noticeLabel = new JLabel("���� ���");
		noticePanel.add(noticeLabel);
		
		scorePanel = new JPanel();
		btnPanel = new JPanel();

		// ����(0~5��)�� ��Ÿ���� ���� ��ư ����
		scoreBtn = new JRadioButton[5];
		btnGroup = new ButtonGroup();
		for (int i=0; i<5; i++) {
			scoreBtn[i] = new JRadioButton(scoreList[i]);
			btnGroup.add(scoreBtn[i]);
			scorePanel.add(scoreBtn[i]);
		}
		
		submitBtn = new JButton("����ϱ�");
		submitBtn.addActionListener(new submitBtnListener());
		btnPanel.add(submitBtn);
		
		reviewPanel.add(scorePanel);
		reviewPanel.add(btnPanel);
		this.add(noticePanel);
		this.add(reviewPanel);
	}
	
	// <����ϱ�> ��ư�� ���� ������
	// ����ڷκ��� �Է� ���� ����(����)�� DB�� ����
	private class submitBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// �˸�â ����
			String dialogTitle = "���� ��� | " + musical.getTitle();
			
			// �̷α��� ������ ���, ���� ��� �Ұ�
			if (User.getId() == null) {
				NotificationClass.createNotifDialog(dialogTitle, "�α����� �ʿ��մϴ�");
				return;
			}
			
			// ����ڰ� �Է��� ���� ����(����ڰ� ������ ���� ��ư ��ȣ + 1)
			int rate = 0;
			
			for (int i=0; i<5; i++) {
				if (scoreBtn[i].isSelected())
					rate = i + 1;
			}
			
			// ������ ���õ��� ���� ���, ��ư�� �������� ����
			if (rate == 0)
			{
				noticeLabel.setText("������ �������ּ���");
				return;
			}

			try (
				Connection conn = new ConnectionClass().getConnection();
				PreparedStatement pStmt = conn.prepareStatement(INSERT_REVIEW_QUERY);
			){
				pStmt.setString(1, musical.getTitle());
				pStmt.setString(2, User.getId());
				pStmt.setInt(3, rate);
				pStmt.setString(4, DateClass.getCurrentDate());
				pStmt.executeUpdate();
				
				// ���� ��� ���� �˸�â ����
				NotificationClass.createNotifDialog(dialogTitle, "���䰡 ��ϵǾ����ϴ� :)");
				
			} catch (SQLException sqle) {
				System.out.println(sqle);
			}
		}
		
	}
}