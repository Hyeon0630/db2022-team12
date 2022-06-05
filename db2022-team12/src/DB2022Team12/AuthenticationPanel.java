package DB2022Team12;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

// �α��� & ȸ������ �г�
class AuthenticationPanel extends JPanel {

	private JDialog signUpDialog;
	private JPanel authPanel, noticePanel, signInTextFieldPanel;
	private JButton signInBtn, signUpBtn, submitBtn, MypageBtn;
	private JTextField idField, nameField, phoneField, emailField, addressField;
	private JPasswordField pwField;
	private JLabel noticeLabel, idLabel, pwLabel, nameLabel, phoneLabel, emailLabel, addressLabel;

	// ���� ���� �˻� ����
	private final String SIGNIN_QUERY = "SELECT pw, name FROM member WHERE id = ?";

	// ���� ���� ���� ����
	// id, pw, name, phone, email, address
	private final String SIGNUP_QUERY = "INSERT INTO member VALUES (?, ?, ?, ?, ?, ?)";

	// �α��� & ȸ������ �г� ���̾ƿ� ����
	public AuthenticationPanel() {
		this.setLayout(new GridLayout(2, 1));
		
		authPanel = new JPanel();
		noticePanel = new JPanel();
		signInTextFieldPanel = new JPanel(new GridLayout(2, 3));

		noticeLabel = new JLabel();
		noticePanel.add(noticeLabel);
		this.add(noticePanel);

		idLabel = new JLabel("ID: ");
		idField = new JTextField();
		pwLabel = new JLabel("Password: ");
		pwField = new JPasswordField();

		signInTextFieldPanel.add(idLabel);
		signInTextFieldPanel.add(idField);
		signInTextFieldPanel.add(pwLabel);
		signInTextFieldPanel.add(pwField);

		signInBtn = new JButton("�α���");
		signInBtn.addActionListener(new signInBtnListener());
		signUpBtn = new JButton("ȸ������");
		signUpBtn.addActionListener(new signUpBtnListener());

		authPanel.add(signInTextFieldPanel);
		authPanel.add(signInBtn);
		authPanel.add(signUpBtn);
		this.add(authPanel);
	}

	// <�α���> ��ư�� ���� ������
	// DB�� ����ڷκ��� ���� �Է°� ��ġ�ϴ� �����Ͱ� �����ϴ��� Ȯ�� �� �α���
	private class signInBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String id = idField.getText().strip();
			String pw = String.valueOf(pwField.getPassword()).strip();

			// ����� �Է� ��ȿ�� ����
			if (id.isEmpty()) {
				noticeLabel.setText("���̵� �Է����ּ���");
			} else if (pw.isEmpty()) {
				noticeLabel.setText("��й�ȣ�� �Է����ּ���");
			} else {

				// ��� �ʵ尡 ��ȿ�ϴٸ� �����ͺ��̽� ����
				try (Connection conn = new ConnectionClass().getConnection();
						PreparedStatement preStmt = conn.prepareStatement(SIGNIN_QUERY);) {
					// ���� ���� �˻��� ���� ����
					preStmt.setString(1, id);
					ResultSet res = preStmt.executeQuery();

					// �ش� ���̵��� ������ �����ϴ��� Ȯ��
					if (!res.next()) {
						noticeLabel.setText("�������� �ʴ� ���̵��Դϴ�");
					} else {
						String user_pw = res.getString("pw");
						// ��й�ȣ ��
						if (!pw.equals(user_pw)) {
							noticeLabel.setText("�߸��� ��й�ȣ�Դϴ�");
						} else {
							// �α��� ����
							User.setId(id);
							User.setName(res.getString("name"));

							// �α��� & ȸ������ �г� �����
							authPanel.setVisible(false);
							noticeLabel.setText(User.getName() + "�� �ȳ��ϼ��� :)");
							
							MypageBtn = new JButton("����������");
							MypageBtn.addActionListener(new MypageBtnListener());
							noticePanel.add(MypageBtn);
						}
					}
				} catch (SQLException sqle) {
					System.out.println(sqle);
				}
			}
		}

	}

	// <ȸ������> ��ư�� ���� ������
	// ȸ������ Dialog â ����
	private class signUpBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// ȸ������ Dialog ���̾ƿ� ����
			signUpDialog = new JDialog();
			signUpDialog.setSize(300, 400);
			signUpDialog.setLayout(new GridLayout(3, 1));

			JPanel noticePanel = new JPanel();
			JPanel inputPanel = new JPanel(new GridLayout(6, 2));
			JPanel btnPanel = new JPanel();

			noticeLabel = new JLabel();
			noticePanel.add(noticeLabel);

			idLabel = new JLabel("ID: ");
			idField = new JTextField();
			pwLabel = new JLabel("Password: ");
			pwField = new JPasswordField();
			nameLabel = new JLabel("�̸�: ");
			nameField = new JTextField("ȫ�浿");
			phoneLabel = new JLabel("��ȭ��ȣ: ");
			phoneField = new JTextField("010-0000-0000");
			emailLabel = new JLabel("�̸���: ");
			emailField = new JTextField("example@example.com");
			addressLabel = new JLabel("�ּ�: ");
			addressField = new JTextField("����� ���빮�� ��ȭ�����");
			inputPanel.add(idLabel);
			inputPanel.add(idField);
			inputPanel.add(pwLabel);
			inputPanel.add(pwField);
			inputPanel.add(nameLabel);
			inputPanel.add(nameField);
			inputPanel.add(phoneLabel);
			inputPanel.add(phoneField);
			inputPanel.add(emailLabel);
			inputPanel.add(emailField);
			inputPanel.add(addressLabel);
			inputPanel.add(addressField);

			submitBtn = new JButton("�����ϱ�");
			submitBtn.addActionListener(new submitBtnListener());
			btnPanel.add(submitBtn);

			signUpDialog.add(noticePanel);
			signUpDialog.add(inputPanel);
			signUpDialog.add(btnPanel);
			signUpDialog.setVisible(true);
		}

	}

	// <�����ϱ�> ��ư ������
	// ����ڷκ��� ���� �Է����� ���� ���� �� DB�� ����
	private class submitBtnListener implements ActionListener {

		// ��ȿ�� ��ȭ��ȣ���� ����
		private boolean isValidPhone(String phone) {
			String phonePattern = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
			return Pattern.matches(phonePattern, phone);
		}

		// ��ȿ�� �̸��� �ּ����� ����
		private boolean isValidEmail(String email) {
			String emailPattern = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$";
			return Pattern.matches(emailPattern, email);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// ����� �Է� ��������
			String id = idField.getText().strip();
			String pw = String.valueOf(pwField.getPassword()).strip();
			String name = nameField.getText().strip();
			String phone = phoneField.getText().strip();
			String email = emailField.getText().strip();
			String address = addressField.getText().strip();

			// ����ڰ� �Է��� �� �ʵ��� ���뿡 ���� ��ȿ�� ����
			if (id.isEmpty()) {
				noticeLabel.setText("���̵� �Է����ּ���");
			} else if (pw.isEmpty()) {
				noticeLabel.setText("��й�ȣ�� �Է����ּ���");
			} else if (name.isEmpty()) {
				noticeLabel.setText("�̸��� �Է����ּ���");
			} else if (!this.isValidPhone(phone)) {
				noticeLabel.setText("�߸��� ��ȭ��ȣ�Դϴ�");
			} else if (!this.isValidEmail(email)) {
				noticeLabel.setText("�߸��� �̸��� �ּ��Դϴ�");
			} else {

				// ��� �ʵ尡 ��ȿ�ϴٸ� �����ͺ��̽� ����
				try (Connection conn = new ConnectionClass().getConnection();
						PreparedStatement preStmt = conn.prepareStatement(SIGNUP_QUERY);) {
					// Member INSERT�� ���� ���� ���� �� Ŀ��
					preStmt.setString(1, id);
					preStmt.setString(2, pw);
					preStmt.setString(3, name);
					preStmt.setString(4, phone);
					preStmt.setString(5, email);
					preStmt.setString(6, address);
					preStmt.executeUpdate();
				} catch (SQLException sqle) {
					int error = sqle.getErrorCode();
					// ID(Primary Key) �ߺ� ���� ó��
					if (error == ConnectionClass.ER_DUP_ENTRY || error == ConnectionClass.ER_DUP_KEY)
						noticeLabel.setText("�ߺ��� ���̵��Դϴ�");

					System.out.println(sqle);
				}

				// ȸ������ dialog �ݱ�
				signUpDialog.dispose();
			}
		}

	}

}