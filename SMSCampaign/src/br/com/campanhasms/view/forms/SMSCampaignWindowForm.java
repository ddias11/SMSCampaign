package br.com.campanhasms.view.forms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import org.quartz.SchedulerException;
import org.smslib.helper.CommPortIdentifier;

import br.com.campanhasms.model.Contato;
import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.persistence.transactions.ApplyFormChangesToSystemPrevayler;
import br.com.campanhasms.properties.FormProperties;
import br.com.campanhasms.properties.Messages;
import br.com.campanhasms.scheduler.jobs.SMSJobsScheduler;
import br.com.campanhasms.sms.contacts.ContactsListBuilder;
import br.com.campanhasms.sms.service.impl.SMSServiceWrapper;
import br.com.campanhasms.view.forms.eventslisteners.AddItemList;
import br.com.campanhasms.view.forms.eventslisteners.MensagemSMSOnChange;
import br.com.campanhasms.view.forms.eventslisteners.RemoveItemList;
import br.com.campanhasms.view.forms.exception.FormDataException;
import br.com.campanhasms.view.forms.model.IFormDataWrapper;
import br.com.campanhasms.view.forms.validation.domain.EmailInputVerifier;

public class SMSCampaignWindowForm implements IFormDataWrapper {

	private static final Font DEFAULT_FONT = new Font(
			FormProperties.getString("Form.FONT_NAME"), Font.PLAIN, Integer.valueOf(FormProperties.getString("Form.FONT_SIZE"))); //$NON-NLS-1$ //$NON-NLS-2$

	private static final DefaultFormatterFactory PHONE_NUMBER_FORMATTER_FACTORY = new DefaultFormatterFactory(
			getPhomeNumberMaskFormatter());

	private static final Font SMS_TEXT_FONT = new Font(
			FormProperties.getString("Form.SMS_TEXT_FONT_NAME"), Font.PLAIN, Integer.valueOf(FormProperties.getString("Form.SMS_TEXT_FONT_SIZE"))); //$NON-NLS-1$ //$NON-NLS-2$

	private static final Font STAT_LINE_FONT = new Font(
			FormProperties.getString("Form.STAT_LINE_FONT_NAME"), Font.BOLD, Integer.valueOf(FormProperties.getString("Form.STAT_LINE_FONT_SIZE"))); //$NON-NLS-1$ //$NON-NLS-2$

	private JButton btnAddContact = null;
	private JButton btnAddReceiver = null;
	private JButton btnApplyChanges = null;
	private JButton btnImportPhoneNumbers = new JButton();
	private JButton btnRemovePhoneNumber = new JButton();
	private JButton btnRemoveReceiver = null;
	private JButton btnStartCamp = null;
	private JComboBox<String> cbCOMPort = null;
	private JPanel emailNotificator = null;
	private JFormattedTextField fmtTextPhoneNumber = null;
	private JFormattedTextField fmtTextReceiverEmailAddress = null;
	private JFrame frmSmscampaign;
	private JLabel lblMessage = null;
	private JLabel lblMessageCharactersRemainingCounter = null;
	private JLabel lblPhoneNumber = null;
	private JLabel lblReceiverEmailAddress = null;
	private JLabel lblStatLine = null;
	private JList<String> lstNotificationReceivers = null;
	private DefaultListModel<String> lstNotificationReceiversModel;
	private JList<String> lstSMSPriorityContactsList = null;
	private DefaultListModel<String> lstSMSPriorityContactsListModel;
	private JPanel priorityContacts = null;

	private JPanel receiversPanel = null;

	private JScrollPane scrollPane;

	private JPanel sMSServicePanel = null;

	private Color statLineBackgroundColor;

	private Color statLIneforegroundColor;

	private JTabbedPane tabbedPane = null;
	private JTextArea textMessage = null;

	private JButton btnTest = null;

	/**
	 * Create the application.
	 */
	public SMSCampaignWindowForm() {
		initialize();
		loadSavedData();
	}

	private static MaskFormatter getPhomeNumberMaskFormatter() {
		try {
			return new MaskFormatter("(##)(##)####-####"); //$NON-NLS-1$
		} catch (ParseException e1) {
			e1.printStackTrace();
			return null;
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SMSCampaignWindowForm window = new SMSCampaignWindowForm();
					window.frmSmscampaign.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings("unused")
	private void clearErrorMessage() {
		getLblStatLine().setForeground(statLIneforegroundColor);
		getLblStatLine().setBackground(statLineBackgroundColor);
		getLblStatLine().setText(""); //$NON-NLS-1$
		getLblStatLine().repaint();
	}

	private JButton getBtnAddContact() {
		if (btnAddContact == null) {
			btnAddContact = new JButton();
			btnAddContact.setToolTipText(FormProperties.getString("Form.ADD_CONTACT_BTN_TOOL_TIP")); //$NON-NLS-1$
			btnAddContact.setFont(DEFAULT_FONT);
			btnAddContact.setText(FormProperties.getString("Form.ADD_CONTACT_BTN_TEXT")); //$NON-NLS-1$
			btnAddContact.setBounds(196, 27, 84, 23);
			btnAddContact.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						new AddItemList<String>(getLstSMSPriorityContactsListModel(), new Contato(
								getFmtTextPhoneNumber().getText()).getFormattedContact()).execute();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		return btnAddContact;
	}

	private JButton getBtnAddReceiver() {
		if (btnAddReceiver == null) {
			btnAddReceiver = new JButton();
			btnAddReceiver.setToolTipText(FormProperties.getString("Form.ADD_RECEIVER_BTN_TOOL_TIP")); //$NON-NLS-1$
			btnAddReceiver.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new AddItemList<String>(getLstNotificationReceiversModel(), getFmtTextReceiverEmailAddress()
							.getText()).execute();
				}
			});
			btnAddReceiver.setFont(DEFAULT_FONT);
			btnAddReceiver.setText(FormProperties.getString("Form.ADD_RECEIVER_BTN_TEXT")); //$NON-NLS-1$
			btnAddReceiver.setBounds(233, 36, 91, 23);
		}
		return btnAddReceiver;
	}

	private JButton getBtnApplyChanges() {
		if (btnApplyChanges == null) {
			btnApplyChanges = new JButton();
			btnApplyChanges.setToolTipText(FormProperties.getString("Form.APPLY_CHANGES_BTN_TOOL_TIP")); //$NON-NLS-1$
			btnApplyChanges.setFont(DEFAULT_FONT);
			btnApplyChanges.setText(FormProperties.getString("Form.APPLY_CHANGES_BTN_TEXT")); //$NON-NLS-1$
			btnApplyChanges.setBounds(288, 324, 120, 23);
			btnApplyChanges.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						SystemPrevayler.execute(new ApplyFormChangesToSystemPrevayler(getSystemPrevaylerModel()));
						if (!SMSJobsScheduler.getScheduler().isStarted()) {
							getBtnStartCamp().setEnabled(true);
							getTestJButton().setEnabled(true);
						}
						setMessage(Messages.getString("MESSAGE.CHANGES_APPPLIED_WITH_SUCCESS_MSG")); //$NON-NLS-1$
					} catch (FormDataException e) {
						setErrorMessage(e.getMessage());
					} catch (SchedulerException e) {
						e.printStackTrace();
					}
				}
			});
		}
		return btnApplyChanges;
	}

	private JButton getBtnImportPhoneNumbers() {
		if (this.btnImportPhoneNumbers == null) {
			this.btnImportPhoneNumbers = new JButton();
		}

		btnImportPhoneNumbers.setText(FormProperties.getString("Form.IMPORT_PHONE_NUMBERS_BTN_TEXT")); //$NON-NLS-1$
		btnImportPhoneNumbers.setFont(DEFAULT_FONT);
		btnImportPhoneNumbers.setBounds(196, 95, 84, 23);
		btnImportPhoneNumbers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jFileChooser = new JFileChooser();
				if (jFileChooser.showDialog(getJFrame(), FormProperties.getString("Form.CHOOSE_WINDOW_TITLE")) == 0) { //$NON-NLS-1$
					Contato[] contactsFromFile = ContactsListBuilder.getContactsFromFile(jFileChooser.getSelectedFile());
					for (Contato contact : contactsFromFile) {
						new AddItemList<String>(getLstSMSPriorityContactsListModel(), contact.getFormattedContact())
								.execute();
					}
				}
			}
		});

		return this.btnImportPhoneNumbers;
	}

	private JButton getBtnRemovePhoneNumber() {
		if (this.btnRemovePhoneNumber == null) {
			this.btnRemovePhoneNumber = new JButton();
		}

		btnRemovePhoneNumber.setText(FormProperties.getString("Form.REMOVE_PHONE_NUMBER_BTN_TEXT")); //$NON-NLS-1$
		btnRemovePhoneNumber.setFont(DEFAULT_FONT);
		btnRemovePhoneNumber.setBounds(196, 61, 84, 23);
		btnRemovePhoneNumber.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (Object itemSelected : getLstSMSPriorityContactsList().getSelectedValuesList()) {
					new RemoveItemList<String>(getLstSMSPriorityContactsListModel(), (String) itemSelected).execute();
				}
			}
		});
		return this.btnRemovePhoneNumber;
	}

	private JButton getBtnRemoveReceiver() {
		if (btnRemoveReceiver == null) {
			btnRemoveReceiver = new JButton();
			btnRemoveReceiver.setToolTipText(FormProperties.getString("Form.REMOVE_RECEIVER_BTN_TOOL_TIP")); //$NON-NLS-1$
			btnRemoveReceiver.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for (Object itemSelected : getLstNotificationReceivers().getSelectedValuesList()) {
						new RemoveItemList<String>(getLstNotificationReceiversModel(), (String) itemSelected).execute();
					}
				}
			});
			btnRemoveReceiver.setFont(DEFAULT_FONT);
			btnRemoveReceiver.setText(FormProperties.getString("Form.REMOVE_RECEIVER_BTN_TEXT")); //$NON-NLS-1$
			btnRemoveReceiver.setBounds(235, 69, 91, 23);
		}
		return btnRemoveReceiver;
	}

	private JButton getBtnStartCamp() {
		if (btnStartCamp == null) {
			btnStartCamp = new JButton();
			btnStartCamp.setBounds(10, 324, 120, 23);
			btnStartCamp.setFont(DEFAULT_FONT);
			btnStartCamp.setText(FormProperties.getString("Form.START_CAMPAIGN_BTN_TEXT")); //$NON-NLS-1$
			btnStartCamp.setEnabled(false);
			btnStartCamp.setToolTipText(FormProperties.getString("Form.START_CAMPAIGN_BTN_TOLL_TIP")); //$NON-NLS-1$
			btnStartCamp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						SMSJobsScheduler.scheduleSendSMSJob();
						SMSJobsScheduler.scheduleMailMessagesReceivedJob();
						SMSJobsScheduler.scheduleSystemPrevaylerModelSnapshotJob();
						SMSJobsScheduler.scheduleQueryRemainCreditJob();
						SMSJobsScheduler.getScheduler().start();
						btnStartCamp.setEnabled(false);
						setMessage(Messages.getString("MESSAGE.CAMPAIGN_STARTED_MSG")); //$NON-NLS-1$
					} catch (SchedulerException e1) {
						e1.printStackTrace();
					}
				}
			});

		}
		return btnStartCamp;
	}

	private JComboBox<String> getCbCOMPort() {
		if (cbCOMPort == null) {
			cbCOMPort = new JComboBox<String>();
			cbCOMPort.setFont(DEFAULT_FONT);
			cbCOMPort.setToolTipText(FormProperties.getString("Form.COM_PORT_CBO_TOOL_TIP")); //$NON-NLS-1$
			cbCOMPort.setBounds(254, 26, 120, 22);

			try {
				Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
				while (portIdentifiers.hasMoreElements()) {
					CommPortIdentifier portIdentifier = portIdentifiers.nextElement();
					cbCOMPort.addItem(new String(portIdentifier.getName()));
				}
			} catch (Exception e) {
			}
			// if (cbCOMPort.getModel().getSize() == 0) {
			//				cbCOMPort.addItem("COMSimul"); //$NON-NLS-1$
			// }
		}

		return cbCOMPort;
	}

	@Override
	public String getCOMPort() {
		return (String) getCbCOMPort().getSelectedItem();
	}

	private JPanel getEmailNotificator() {
		if (emailNotificator == null) {
			emailNotificator = new JPanel();
			emailNotificator.setLayout(null);
			emailNotificator.add(getReceiversPanel());
		}
		return emailNotificator;
	}

	private JFormattedTextField getFmtTextPhoneNumber() {
		if (fmtTextPhoneNumber == null) {
			fmtTextPhoneNumber = new JFormattedTextField();
			fmtTextPhoneNumber.setToolTipText(FormProperties.getString("Form.CONTACT_PHONE_NUMBER_TXT_TOOL_TIP")); //$NON-NLS-1$
			fmtTextPhoneNumber.setFont(DEFAULT_FONT);
			fmtTextPhoneNumber.setHorizontalAlignment(SwingConstants.TRAILING);
			fmtTextPhoneNumber.setBounds(85, 28, 101, 20);
			fmtTextPhoneNumber.setFormatterFactory(PHONE_NUMBER_FORMATTER_FACTORY);

		}
		return fmtTextPhoneNumber;
	}

	private JFormattedTextField getFmtTextReceiverEmailAddress() {
		if (fmtTextReceiverEmailAddress == null) {
			fmtTextReceiverEmailAddress = new JFormattedTextField();
			fmtTextReceiverEmailAddress.setToolTipText(FormProperties
					.getString("Form.RECEIVER_MAIL_ADDRESS_TXT_TOOL_TIP")); //$NON-NLS-1$
			fmtTextReceiverEmailAddress.setFont(DEFAULT_FONT);
			fmtTextReceiverEmailAddress.setBounds(10, 37, 213, 20);
			fmtTextReceiverEmailAddress.setInputVerifier(new EmailInputVerifier());
		}
		return fmtTextReceiverEmailAddress;
	}

	private JFrame getJFrame() {
		if (frmSmscampaign == null) {
			frmSmscampaign = new JFrame();
			frmSmscampaign.getContentPane().setFont(DEFAULT_FONT);
			frmSmscampaign.setTitle(FormProperties.getString("Form.WINDOW_TITLE")); //$NON-NLS-1$
			frmSmscampaign.setForeground(Color.BLACK);
			frmSmscampaign.setFont(DEFAULT_FONT);
			frmSmscampaign.setBounds(100, 100, 427, 399);
			frmSmscampaign.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		return frmSmscampaign;
	}

	private JTabbedPane getJTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.setFont(DEFAULT_FONT);
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
			tabbedPane.setBounds(10, 11, 398, 302);
			tabbedPane.addTab(FormProperties.getString("Form.SMS_SERVICE_TAB_TEXT"), (Icon) null, getSMSServicePanel(), //$NON-NLS-1$
					FormProperties.getString("Form.SMS_SERVICE_TAB_TOOL_TIP")); //$NON-NLS-1$
			tabbedPane.addTab(FormProperties.getString("Form.EMAIL_NOTIFICATOR_TAB_TEXT"), null, getEmailNotificator(), //$NON-NLS-1$
					FormProperties.getString("Form.EMAIL_NOTIFICATOR_TAB_TOOL_TIP")); //$NON-NLS-1$
			tabbedPane.addTab(FormProperties.getString("Form.PRIORITY_CONTACTS_TAB_TEXT"), null, getPriorityContacts(),
					FormProperties.getString("Form.PRIORITY_CONTACTS_TAB_TOOL_TIP")); //$NON-NLS-1$

		}

		return tabbedPane;
	}

	public JLabel getLblMessage() {
		if (lblMessage == null) {
			lblMessage = new JLabel(FormProperties.getString("Form.LABEL_MESSAGE_TEXT")); //$NON-NLS-1$
			lblMessage.setFont(DEFAULT_FONT);
			lblMessage.setBounds(10, 9, 128, 14);
		}

		return lblMessage;
	}

	public JLabel getLblMessageCharactersRemainingCounter() {
		if (lblMessageCharactersRemainingCounter == null) {
			lblMessageCharactersRemainingCounter = new JLabel();
			lblMessageCharactersRemainingCounter.setFont(DEFAULT_FONT);
			lblMessageCharactersRemainingCounter.setText(FormProperties
					.getString("Form.LABEL_MESSAGE_CHARACTERS_REMAINING_COUNTER_TEXT")); //$NON-NLS-1$
			lblMessageCharactersRemainingCounter.setToolTipText(FormProperties
					.getString("Form.LABEL_MESSAGE_CHARACTERS_REMAINING_COUNTER_TOOL_TIP")); //$NON-NLS-1$
			lblMessageCharactersRemainingCounter.setHorizontalAlignment(SwingConstants.CENTER);
			lblMessageCharactersRemainingCounter.setBounds(222, 202, 22, 14);
		}

		return lblMessageCharactersRemainingCounter;
	}

	private JLabel getLblPhoneNumber() {
		if (lblPhoneNumber == null) {
			lblPhoneNumber = new JLabel();
			lblPhoneNumber.setFont(DEFAULT_FONT);
			lblPhoneNumber.setText(FormProperties.getString("Form.LABEL_PHONE_NUMBER_TEXT")); //$NON-NLS-1$
			lblPhoneNumber.setLabelFor(fmtTextPhoneNumber);
			lblPhoneNumber.setBounds(23, 31, 62, 14);

		}
		return lblPhoneNumber;
	}

	private JLabel getLblReceiverEmailAddress() {
		if (lblReceiverEmailAddress == null) {
			lblReceiverEmailAddress = new JLabel();
			lblReceiverEmailAddress.setFont(DEFAULT_FONT);
			lblReceiverEmailAddress.setText(FormProperties.getString("Form.LABEL_EMAIL_ADDRESS_TEXT")); //$NON-NLS-1$); 
			lblReceiverEmailAddress.setBounds(10, 20, 133, 14);
		}
		return lblReceiverEmailAddress;
	}

	private JLabel getLblStatLine() {
		if (lblStatLine == null) {
			lblStatLine = new JLabel();
			lblStatLine.setFont(STAT_LINE_FONT); 
			lblStatLine.setBounds(0, 349, 419, 23);
			statLIneforegroundColor = lblStatLine.getForeground();
			statLineBackgroundColor = lblStatLine.getBackground();
			lblStatLine.setOpaque(true);
		}
		return lblStatLine;
	}

	@Override
	public List<String> getListNotificationReceivers() {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < getLstNotificationReceiversModel().getSize(); i++) {
			arrayList.add(getLstNotificationReceiversModel().get(i));
		}
		return arrayList;
	}

	private JList<String> getLstNotificationReceivers() {
		if (lstNotificationReceivers == null) {
			lstNotificationReceivers = new JList<String>();
			lstNotificationReceivers.setToolTipText(FormProperties
					.getString("Form.NOTIFICATION_RECEIVERS_LST_TOOL_TIP")); //$NON-NLS-1$
			lstNotificationReceivers.setFont(DEFAULT_FONT);
			lstNotificationReceivers.setBounds(10, 66, 213, 144);
			lstNotificationReceivers.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			lstNotificationReceivers.setModel(getLstNotificationReceiversModel());

		}
		return lstNotificationReceivers;
	}

	private DefaultListModel<String> getLstNotificationReceiversModel() {
		if (this.lstNotificationReceiversModel == null) {
			this.lstNotificationReceiversModel = new DefaultListModel<String>();
		}
		return this.lstNotificationReceiversModel;
	}

	private JList<String> getLstSMSPriorityContactsList() {
		if (lstSMSPriorityContactsList == null) {
			lstSMSPriorityContactsList = new JList<String>();
			lstSMSPriorityContactsList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			lstSMSPriorityContactsList.setToolTipText(FormProperties.getString("Form.SMS_PRIORITY_LST_TOOL_TIP")); //$NON-NLS-1$
			lstSMSPriorityContactsList.setFont(DEFAULT_FONT);
			lstSMSPriorityContactsList.setLayoutOrientation(JList.VERTICAL);
			lstSMSPriorityContactsList.setModel(getLstSMSPriorityContactsListModel());
		}
		return lstSMSPriorityContactsList;
	}

	private DefaultListModel<String> getLstSMSPriorityContactsListModel() {
		if (this.lstSMSPriorityContactsListModel == null) {
			this.lstSMSPriorityContactsListModel = new DefaultListModel<String>();

		}
		return this.lstSMSPriorityContactsListModel;
	}

	private JPanel getPriorityContacts() {
		if (priorityContacts == null) {
			priorityContacts = new JPanel();
			priorityContacts.setLayout(null);
			priorityContacts.add(getLblPhoneNumber());
			priorityContacts.add(getFmtTextPhoneNumber());
			priorityContacts.add(getBtnAddContact());
			priorityContacts.add(getScrollPane());
			btnRemovePhoneNumber.setToolTipText(FormProperties.getString("Form.REMOVE_PHONE_NUMBER_BTN_TOOL_TIP")); //$NON-NLS-1$
			priorityContacts.add(getBtnRemovePhoneNumber());
			btnImportPhoneNumbers.setToolTipText(FormProperties.getString("Form.IMPORT_PHONE_NUMBERS_BTN_TOOL_TIP")); //$NON-NLS-1$
			priorityContacts.add(getBtnImportPhoneNumbers());

		}
		return priorityContacts;
	}

	private JPanel getReceiversPanel() {
		if (receiversPanel == null) {
			receiversPanel = new JPanel();
			receiversPanel.setBorder(new TitledBorder(null, FormProperties
					.getString("Form.NOTIFICATION_RECEIVER_INTERNAL_PANEL_TITLE"), TitledBorder.LEADING, //$NON-NLS-1$ 
					TitledBorder.TOP, DEFAULT_FONT, null));
			receiversPanel.setBounds(0, 11, 393, 261);
			receiversPanel.setLayout(null);
			receiversPanel.add(getLblReceiverEmailAddress());
			receiversPanel.add(getFmtTextReceiverEmailAddress());
			receiversPanel.add(getBtnAddReceiver());
			receiversPanel.add(getBtnRemoveReceiver());
			receiversPanel.add(getLstNotificationReceivers());
		}
		return receiversPanel;
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(23, 59, 163, 207);
			scrollPane.setViewportView(getLstSMSPriorityContactsList());
		}
		return scrollPane;
	}

	@Override
	public List<String> getSMSPriorityContactsList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < getLstSMSPriorityContactsListModel().getSize(); i++) {
			arrayList.add(getLstSMSPriorityContactsListModel().get(i));
		}
		return arrayList;

	}

	private JPanel getSMSServicePanel() {
		if (sMSServicePanel == null) {
			sMSServicePanel = new JPanel();
			sMSServicePanel.setLayout(null);
			sMSServicePanel.add(getLblMessage());
			sMSServicePanel.add(getSMSText());
			sMSServicePanel.add(getLblMessageCharactersRemainingCounter());
			sMSServicePanel.add(getCbCOMPort());
			sMSServicePanel.add(getTestJButton());
		}

		return sMSServicePanel;
	}

	private JButton getTestJButton() {
		if (btnTest == null) {
			btnTest = new JButton();
			btnTest.setToolTipText("Test");
			btnTest.setText("Test"); 
			btnTest.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnTest.setBounds(254, 59, 120, 23);
			btnTest.setEnabled(false);
			btnTest.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					SystemPrevaylerModel systemPrevaylerModel = SystemPrevayler.getSystemPrevaylerModel();
					String textMessage = systemPrevaylerModel.getTextMessage();
					String contact = JOptionPane.showInputDialog("Input the test contact number");
					try {
						Contato contactObj = new Contato(contact);
						SMSServiceWrapper.initialize(systemPrevaylerModel.getCOMPort());
						SMSServiceWrapper.sendMessage(contactObj.getFormattedContact() , textMessage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			
		}

		return btnTest;
	}

	public JTextArea getSMSText() {
		if (textMessage == null) {
			textMessage = new JTextArea();
			textMessage.setFont(SMS_TEXT_FONT);

			textMessage.setToolTipText(FormProperties.getString("Form.SMS_TEXT_TOOL_TIP")); //$NON-NLS-1$  
			textMessage.setBounds(10, 26, 234, 175);
			textMessage.setColumns(50);
			textMessage.setLineWrap(true);
			textMessage.setWrapStyleWord(true);
			textMessage.setBorder(new EtchedBorder(EtchedBorder.RAISED));
			MensagemSMSOnChange mensagemSMSOnChange = new MensagemSMSOnChange(getLblMessageCharactersRemainingCounter());
			textMessage.addCaretListener(mensagemSMSOnChange);
			textMessage.addKeyListener(mensagemSMSOnChange);
		}
		return textMessage;
	}

	private IFormDataWrapper getSystemPrevaylerModel() throws FormDataException {
		SystemPrevaylerModel systemPrevaylerModel = new SystemPrevaylerModel();
		IFormDataWrapper formDataWrapper = this;
		systemPrevaylerModel.setCOMPort(formDataWrapper.getCOMPort());
		systemPrevaylerModel.setListNotificationReceivers(formDataWrapper.getListNotificationReceivers());
		systemPrevaylerModel.setSMSPriorityContactsList(formDataWrapper.getSMSPriorityContactsList());
		systemPrevaylerModel.setTextMessage(formDataWrapper.getTextMessage());
		return systemPrevaylerModel;
	}

	@Override
	public String getTextMessage() {
		return getSMSText().getText();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		getJFrame();
		getJFrame().getContentPane().setLayout(null);
		getJFrame().getContentPane().add(getJTabbedPane());
		getJFrame().getContentPane().add(getBtnApplyChanges());
		getJFrame().getContentPane().add(getBtnStartCamp());
		getJFrame().getContentPane().add(getLblStatLine());

	}

	private void loadSavedData() {
		try {
			setCOMPort(SystemPrevayler.getSystemPrevaylerModel().getCOMPort());
		} catch (Exception e) {
		}
		try {
			setListNotificationReceivers(SystemPrevayler.getSystemPrevaylerModel().getListNotificationReceivers());
		} catch (Exception e) {
		}
		try {
			setSMSPriorityContactsList(SystemPrevayler.getSystemPrevaylerModel().getSMSPriorityContactsList());
		} catch (Exception e) {
		}
		try {
			setTextMessage(SystemPrevayler.getSystemPrevaylerModel().getTextMessage());
		} catch (Exception e) {
		}
	}

	@Override
	public void setCOMPort(String commPortIdentifier) {
		getCbCOMPort().setSelectedItem(commPortIdentifier);
	}

	private void setErrorMessage(String message) {
		getLblStatLine().setForeground(Color.WHITE);
		getLblStatLine().setBackground(Color.RED);
		getLblStatLine().setText(message);
		getLblStatLine().repaint();
	}

	@Override
	public void setListNotificationReceivers(List<String> listNotificatinReceivers) {
		getLstNotificationReceiversModel().clear();
		for (String item : listNotificatinReceivers) {
			getLstNotificationReceiversModel().addElement(item);
		}
	}

	private void setMessage(String message) {
		getLblStatLine().setForeground(Color.BLUE);
		getLblStatLine().setBackground(statLineBackgroundColor);
		getLblStatLine().setText(message);
		getLblStatLine().repaint();
	}

	@Override
	public void setSMSPriorityContactsList(List<String> smsContactsBlackList) {
		getLstSMSPriorityContactsListModel().clear();
		for (String item : smsContactsBlackList) {
			getLstSMSPriorityContactsListModel().addElement(item);
		}
	}

	@Override
	public void setTextMessage(String textMessage) {
		getSMSText().setText(textMessage);
	}
}
