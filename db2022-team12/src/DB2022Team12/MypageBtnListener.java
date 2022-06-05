package DB2022Team12;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class MypageBtnListener implements ActionListener{
	private JDialog signInDialog;
	private JPanel emptyPanel, personalPanel, MypagePanel;
	private JLabel infoLabel;
	private JButton checkTicketBtn, reviewBtn;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// <�α���> ��ư�� ���� ������
		// ���������� ���� Ƽ�� ��ȸ, ���� ���, �ۼ� ���� ���� ��ư ���� dialog ����
		signInDialog = new JDialog();
		signInDialog.setSize(300, 350);
		signInDialog.setTitle("����������");
		signInDialog.setLayout(new GridLayout(3, 1, 20, 5));
		
		emptyPanel = new JPanel();
		signInDialog.add(emptyPanel);
		
		personalPanel = new JPanel(new GridLayout(2, 1, 10, 10));
		MypagePanel = new JPanel(new GridLayout(2, 1, 10, 8));
		
		infoLabel = new JLabel(User.NAME + "��", SwingConstants.CENTER);
		infoLabel.setFont(new Font("���", Font.BOLD, 30));
		
		personalPanel.add(infoLabel);
		signInDialog.add(personalPanel);
		
		checkTicketBtn = new JButton("���� Ƽ�� ��ȸ");
		checkTicketBtn.addActionListener(new checkTicketListener());
		reviewBtn = new JButton("�ۼ� ���� ����");
		reviewBtn.addActionListener(new reviewBtnListener());
		MypagePanel.add(checkTicketBtn);
		MypagePanel.add(reviewBtn);
		signInDialog.add(MypagePanel);
			
		signInDialog.setVisible(true);
			
	}
}