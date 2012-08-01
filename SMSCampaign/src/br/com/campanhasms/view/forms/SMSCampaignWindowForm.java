package br.com.campanhasms.view.forms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;

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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.quartz.SchedulerException;
import org.smslib.helper.CommPortIdentifier;

import br.com.campanhasms.model.Contato;
import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.persistence.transactions.ApplyFormChangesToSystemPrevayler;
import br.com.campanhasms.properties.FormProperties;
import br.com.campanhasms.properties.Messages;
import br.com.campanhasms.properties.SMSServiceProperties;
import br.com.campanhasms.scheduler.jobs.SMSJobsScheduler;
import br.com.campanhasms.sms.contacts.ContactsListBuilder;
import br.com.campanhasms.sms.contacts.normalization.model.ContactFactory;
import br.com.campanhasms.sms.service.impl.SMSServiceWrapper;
import br.com.campanhasms.view.forms.eventslisteners.AddItemList;
import br.com.campanhasms.view.forms.eventslisteners.MensagemSMSOnChange;
import br.com.campanhasms.view.forms.eventslisteners.RemoveItemList;
import br.com.campanhasms.view.forms.exception.FormDataException;
import br.com.campanhasms.view.forms.model.IFormDataWrapper;
import br.com.campanhasms.view.forms.validation.domain.EmailInputVerifier;
import br.com.campanhasms.view.forms.validation.domain.PhoneFieldDocumentFilter;

public class SMSCampaignWindowForm implements IFormDataWrapper {

	private static final String SP_DDD = "11";

	private static final PhoneFieldDocumentFilter PHONE_NUMBER_DOCUMENT_FILTER = new PhoneFieldDocumentFilter(8);
	
	private static final PhoneFieldDocumentFilter SP_PHONE_NUMBER_DOCUMENT_FILTER = new PhoneFieldDocumentFilter(9);
	
	private static final PhoneFieldDocumentFilter DDD_DOCUMENT_FILTER = new PhoneFieldDocumentFilter(2);

	private static final Font DEFAULT_FONT = new Font(
			FormProperties.getString("Form.FONT_NAME"), Font.PLAIN, Integer.valueOf(FormProperties.getString("Form.FONT_SIZE"))); //$NON-NLS-1$ //$NON-NLS-2$

	private static final Logger LOGGER = Logger.getLogger(SMSCampaignWindowForm.class);

	private static final Font SMS_TEXT_FONT = new Font(
			FormProperties.getString("Form.SMS_TEXT_FONT_NAME"), Font.PLAIN, Integer.valueOf(FormProperties.getString("Form.SMS_TEXT_FONT_SIZE"))); //$NON-NLS-1$ //$NON-NLS-2$

	private static final Font STAT_LINE_FONT = new Font(
			FormProperties.getString("Form.STAT_LINE_FONT_NAME"), Font.BOLD, Integer.valueOf(FormProperties.getString("Form.STAT_LINE_FONT_SIZE"))); //$NON-NLS-1$ //$NON-NLS-2$

	private JButton btnAddPriorityContact = null;
	private JButton btnAddContactInBlackList = null;
	private JButton btnAddReceiver = null;
	private JButton btnApplyChanges = null;
	private JButton btnImportPriorityPhoneNumbers = null;
	private JButton btnRemovePriorityPhoneNumber = null;
	private JButton btnRemovePhoneNumberFromBlackList = null;
	private JButton btnRemoveReceiver = null;
	private JButton btnStartCamp = null;
	private JButton btnTest = null;
	private JComboBox<String> cbCOMPort = null;
	private JPanel emailNotificator = null;
	private JFormattedTextField fmtTextPriorityPhoneDDD = null;
	private JTextField fmtTextPriorityPhoneNumber = null;
	private JTextField fmtTextBlackListPhoneDDD = null;
	private JTextField fmtTextBlackListPhoneNumber = null;
	private JTextField fmtTextReceiverEmailAddress = null;
	private JFrame frmSmscampaign;
	private JLabel lblMessage = null;
	private JLabel lblMessageCharactersRemainingCounter = null;
	private JLabel lblPriorityPhoneNumber = null;
	private JLabel lblBlackListPhoneNumber = null;
	
	private JLabel lblReceiverEmailAddress = null;
	private JLabel lblStatLine = null;
	private JList<String> lstNotificationReceivers = null;
	private DefaultListModel<String> lstNotificationReceiversModel;
	private JList<String> lstSMSPriorityContactsList = null;
	private JList<String> lstSMSBlackListContacts = null;
	private DefaultListModel<String> lstSMSPriorityContactsListModel;
	private DefaultListModel<String> lstSMSBlackListContactsModel;

	private JPanel priorityContacts = null;
	
	private JPanel blackListContacts = null;

	private JPanel receiversPanel = null;

	private JScrollPane scrollPanePriorityContacts;
	private JScrollPane scrollPaneContactsInBlackList;

	private JPanel sMSServicePanel = null;

	private Color statLineBackgroundColor;

	private Color statLIneforegroundColor;
	private JTabbedPane tabbedPane = null;

	private JTextArea textMessage = null;

	/**
	 * Create the application.
	 */
	public SMSCampaignWindowForm() {
		System.out.println(ContactFactory.getInstance().createContact(5511986446670l).getFormattedContact());
		System.out.println(ContactFactory.getInstance().createContact(0l).getFormattedContact());
		DOMConfigurator.configure(new File("res/properties/log4jAppConf.xml").getAbsolutePath());
		initialize();
		loadSavedData();
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

	private JButton getBtnAddPriorityContact() {
		if (btnAddPriorityContact == null) {
			btnAddPriorityContact = new JButton();
			btnAddPriorityContact.setToolTipText(FormProperties.getString("Form.ADD_CONTACT_BTN_TOOL_TIP")); //$NON-NLS-1$
			btnAddPriorityContact.setFont(DEFAULT_FONT);
			btnAddPriorityContact.setText(FormProperties.getString("Form.ADD_CONTACT_BTN_TEXT")); //$NON-NLS-1$
			btnAddPriorityContact.setBounds(196, 27, 84, 23);
			btnAddPriorityContact.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						String phoneNumber = getFmtTextPriorityPhoneNumber().getText().replaceAll("\\D", "");
						String phoneDDD = getFmtTextPriorityPhoneDDD().getText().replaceAll("\\D", "");
						Contato contato = new Contato(new Long(phoneDDD + phoneNumber));
						if(getLstSMSBlackListContactsModel().contains(contato.getFormattedContact())) {
							LOGGER.info("This contact is in the Black List: " + contato.getFormattedContact());
							getLstSMSBlackListContacts().setSelectedValue(contato.getFormattedContact(), true);
							setMessage("This contact is in the Black List");
						} else {
							new AddItemList<String>(getLstSMSPriorityContactsListModel(), contato.getFormattedContact())
									.execute();
							LOGGER.info("Adding priority contact: " + contato.getFormattedContact());
						}
					} catch (Exception e) {
						LOGGER.error("Error when adding priority contact", e);
					}
				}
			});
		}
		return btnAddPriorityContact;
	}

	private JButton getBtnAddContactInBlackList() {
		if (btnAddContactInBlackList == null) {
			btnAddContactInBlackList = new JButton();
			btnAddContactInBlackList.setToolTipText(FormProperties.getString("Form.ADD_CONTACT_BTN_TOOL_TIP")); //$NON-NLS-1$
			btnAddContactInBlackList.setFont(DEFAULT_FONT);
			btnAddContactInBlackList.setText(FormProperties.getString("Form.ADD_CONTACT_BTN_TEXT")); //$NON-NLS-1$
			btnAddContactInBlackList.setBounds(196, 27, 84, 23);
			btnAddContactInBlackList.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						String phoneNumber = getFmtTextBlackListPhoneNumber().getText().replaceAll("\\D", "");
						String phoneDDD = getFmtTextBlackListPhoneDDD().getText().replaceAll("\\D", "");
						Contato contato = new Contato(new Long(phoneDDD+phoneNumber));
						if (getLstSMSPriorityContactsListModel().contains(contato.getFormattedContact())) {
							LOGGER.info("This contact is in the Priority List: " + contato.getFormattedContact());
							getLstSMSPriorityContactsList().setSelectedValue(contato.getFormattedContact(), true);
							setMessage("This contact is in the Priority List");
						} else {

							new AddItemList<String>(getLstSMSBlackListContactsModel(), contato.getFormattedContact())
									.execute();
							LOGGER.info("Adding contact into Black List: " + contato.getFormattedContact());
							SystemPrevayler.getSystemPrevaylerModel().addContactInBlackList(contato);
						}
					} catch (Exception e) {
						LOGGER.error("Error when adding contact in Black List", e);
					}
				}
			});
		}
		return btnAddContactInBlackList;
	}

	private JButton getBtnAddReceiver() {
		if (btnAddReceiver == null) {
			btnAddReceiver = new JButton();
			btnAddReceiver.setToolTipText(FormProperties.getString("Form.ADD_RECEIVER_BTN_TOOL_TIP")); //$NON-NLS-1$
			btnAddReceiver.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String itemToAdd = getFmtTextReceiverEmailAddress().getText();
					new AddItemList<String>(getLstNotificationReceiversModel(), itemToAdd).execute();
					LOGGER.info("Adding receiver: " + itemToAdd);
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
						setMessage(Messages.getString("MESSAGE.INITIALIZING_SMS_SERVICE"));
						SystemPrevayler.execute(new ApplyFormChangesToSystemPrevayler(getSystemPrevaylerModel()));
						if (!SMSJobsScheduler.getScheduler().isStarted()) {
							getBtnStartCamp().setEnabled(true);
							getTestJButton().setEnabled(true);
						}
						setMessage(Messages.getString("MESSAGE.CHANGES_APPPLIED_WITH_SUCCESS_MSG")); //$NON-NLS-1$
						SMSServiceWrapper.initialize(getSystemPrevaylerModel().getCOMPort());
						LOGGER.info("Form Changes committed with success");
					} catch (FormDataException e) {
						setErrorMessage(e.getMessage());
					} catch (SchedulerException e) {
						LOGGER.error("Error when schedule the jobs", e);
					} catch (Exception e) {
						LOGGER.error("Error when applying the form changes", e);
					}
				}
			});
		}
		return btnApplyChanges;
	}

	private JButton getBtnImportPriorityPhoneNumbers() {
		if (btnImportPriorityPhoneNumbers == null) {
			btnImportPriorityPhoneNumbers = new JButton();

			btnImportPriorityPhoneNumbers.setText(FormProperties.getString("Form.IMPORT_PHONE_NUMBERS_BTN_TEXT")); //$NON-NLS-1$
			btnImportPriorityPhoneNumbers.setToolTipText(FormProperties.getString("Form.IMPORT_PHONE_NUMBERS_BTN_TOOL_TIP")); //$NON-NLS-1$
			btnImportPriorityPhoneNumbers.setFont(DEFAULT_FONT);
			btnImportPriorityPhoneNumbers.setBounds(196, 95, 84, 23);
			btnImportPriorityPhoneNumbers.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser jFileChooser = new JFileChooser();
					if (jFileChooser.showDialog(getJFrame(), FormProperties.getString("Form.CHOOSE_WINDOW_TITLE")) == JFileChooser.APPROVE_OPTION) { //$NON-NLS-1$
						Contato[] contactsFromFile = ContactsListBuilder.getContactsFromFile(jFileChooser.getSelectedFile());
						for (Contato contact : contactsFromFile) {
							new AddItemList<String>(getLstSMSPriorityContactsListModel(), contact.getFormattedContact())
									.execute();
						}
						LOGGER.info("Succes importing contacts");
					}
				}
			});
		}

		return btnImportPriorityPhoneNumbers;
	}

	private JButton getBtnRemovePriorityPhoneNumber() {
		if (btnRemovePriorityPhoneNumber == null) {
			btnRemovePriorityPhoneNumber = new JButton();

			btnRemovePriorityPhoneNumber.setText(FormProperties.getString("Form.REMOVE_PHONE_NUMBER_BTN_TEXT")); //$NON-NLS-1$
			btnRemovePriorityPhoneNumber.setToolTipText(FormProperties.getString("Form.REMOVE_PHONE_NUMBER_BTN_TOOL_TIP")); //$NON-NLS-1$
			btnRemovePriorityPhoneNumber.setFont(DEFAULT_FONT);
			btnRemovePriorityPhoneNumber.setBounds(196, 61, 84, 23);
			btnRemovePriorityPhoneNumber.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					for (Object itemSelected : getLstSMSPriorityContactsList().getSelectedValuesList()) {
						new RemoveItemList<String>(getLstSMSPriorityContactsListModel(), (String) itemSelected).execute();
						LOGGER.info("Success removing contact " + (String) itemSelected);
					}
				}
			});
		}
		return btnRemovePriorityPhoneNumber;
	}

	private JButton getBtnRemovePhoneNumberFromBlackList() {
		if (btnRemovePhoneNumberFromBlackList == null) {
			btnRemovePhoneNumberFromBlackList = new JButton();

			btnRemovePhoneNumberFromBlackList.setText(FormProperties.getString("Form.REMOVE_PHONE_NUMBER_BTN_TEXT")); //$NON-NLS-1$
			btnRemovePhoneNumberFromBlackList.setToolTipText(FormProperties.getString("Form.REMOVE_PHONE_NUMBER_BTN_TOOL_TIP")); //$NON-NLS-1$
			btnRemovePhoneNumberFromBlackList.setFont(DEFAULT_FONT);
			btnRemovePhoneNumberFromBlackList.setBounds(196, 61, 84, 23);
			btnRemovePhoneNumberFromBlackList.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					for (Object itemSelected : getLstSMSBlackListContacts().getSelectedValuesList()) {
						new RemoveItemList<String>(getLstSMSBlackListContactsModel(), (String) itemSelected).execute();
						LOGGER.info("Success removing contact " + (String) itemSelected + " from Black List");
						try {
							Contato contato = new Contato(new Long(itemSelected.toString()));
							SystemPrevayler.getSystemPrevaylerModel().removeContactFromBlackList(contato );
						} catch (Exception e) {
							LOGGER.error("Erro generating the contact to be removed from persisted Black list", e);
						}
					}
				}
			});
		}
		return btnRemovePhoneNumberFromBlackList;
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
						LOGGER.info("Success removing receiver " + (String) itemSelected);
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
						LOGGER.info("Success starting SMS Campaign");
					} catch (SchedulerException ex) {
						LOGGER.error("Error when starting SMS Campaign", ex);
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

	private JTextField getFmtTextPriorityPhoneNumber() {
		if (fmtTextPriorityPhoneNumber == null) {
			fmtTextPriorityPhoneNumber = new JFormattedTextField();
			fmtTextPriorityPhoneNumber.setToolTipText(FormProperties.getString("Form.CONTACT_PHONE_NUMBER_TXT_TOOL_TIP")); //$NON-NLS-1$
			fmtTextPriorityPhoneNumber.setFont(DEFAULT_FONT);
			fmtTextPriorityPhoneNumber.setHorizontalAlignment(SwingConstants.TRAILING);
			fmtTextPriorityPhoneNumber.setBounds(102, 28, 84, 20);
			fmtTextPriorityPhoneNumber.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent arg0) {
				}
				
				@Override
				public void focusGained(FocusEvent arg0) {
					if(isDDDFromSP(getFmtTextPriorityPhoneDDD().getText())) {
						SP_PHONE_NUMBER_DOCUMENT_FILTER.installFilter(fmtTextPriorityPhoneNumber);
					} else {
						PHONE_NUMBER_DOCUMENT_FILTER.installFilter(fmtTextPriorityPhoneNumber);
					}
				}
			});

		}
		return fmtTextPriorityPhoneNumber;
	}

	private JTextField getFmtTextPriorityPhoneDDD() {
		if (fmtTextPriorityPhoneDDD == null) {
			fmtTextPriorityPhoneDDD = new JFormattedTextField();
			fmtTextPriorityPhoneDDD.setFont(DEFAULT_FONT);
			fmtTextPriorityPhoneDDD.setHorizontalAlignment(SwingConstants.TRAILING);
			fmtTextPriorityPhoneDDD.setBounds(81, 28, 20, 20);
			fmtTextPriorityPhoneDDD.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					getFmtTextPriorityPhoneNumber().setText("");
				}
				
				@Override
				public void insertUpdate(DocumentEvent arg0) {
					getFmtTextPriorityPhoneNumber().setText("");
				}
				
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					getFmtTextPriorityPhoneNumber().setText("");
				}
			});

		}
		return fmtTextPriorityPhoneDDD;
	}

	private JTextField getFmtTextBlackListPhoneDDD() {
		if (fmtTextBlackListPhoneDDD == null) {
			fmtTextBlackListPhoneDDD = new JFormattedTextField();
			fmtTextBlackListPhoneDDD.setFont(DEFAULT_FONT);
			fmtTextBlackListPhoneDDD.setHorizontalAlignment(SwingConstants.TRAILING);
			fmtTextBlackListPhoneDDD.setBounds(81, 28, 20, 20);
			fmtTextBlackListPhoneDDD.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					getFmtTextBlackListPhoneNumber().setText("");
				}
				
				@Override
				public void insertUpdate(DocumentEvent arg0) {
					getFmtTextBlackListPhoneNumber().setText("");
				}
				
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					getFmtTextBlackListPhoneNumber().setText("");
				}
			});

		}
		return fmtTextBlackListPhoneDDD;
	}

	
	private JTextField getFmtTextBlackListPhoneNumber() {
		if (fmtTextBlackListPhoneNumber == null) {
			fmtTextBlackListPhoneNumber = new JFormattedTextField();
			fmtTextBlackListPhoneNumber.setToolTipText(FormProperties.getString("Form.CONTACT_PHONE_NUMBER_TXT_TOOL_TIP")); //$NON-NLS-1$
			fmtTextBlackListPhoneNumber.setFont(DEFAULT_FONT);
			fmtTextBlackListPhoneNumber.setHorizontalAlignment(SwingConstants.TRAILING);
			fmtTextBlackListPhoneNumber.setBounds(102, 28, 84, 20);
			fmtTextBlackListPhoneNumber.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent arg0) {
				}
				
				@Override
				public void focusGained(FocusEvent arg0) {
					if(isDDDFromSP(getFmtTextBlackListPhoneDDD().getText())) {
						SP_PHONE_NUMBER_DOCUMENT_FILTER.installFilter(fmtTextBlackListPhoneNumber);
					} else {
						PHONE_NUMBER_DOCUMENT_FILTER.installFilter(fmtTextBlackListPhoneNumber);
					}
				}
			});

		}
		return fmtTextBlackListPhoneNumber;
	}

	protected boolean isDDDFromSP(String text) {
		return SP_DDD.equals(text);
	}


	private JTextField getFmtTextReceiverEmailAddress() {
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
			JFrame.setDefaultLookAndFeelDecorated(true);
			frmSmscampaign = new JFrame();
			frmSmscampaign.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frmSmscampaign.setIconImage(Toolkit.getDefaultToolkit().getImage(
					new File("images/smscampaign.gif").getAbsolutePath()));
			frmSmscampaign.getContentPane().setFont(DEFAULT_FONT);
			frmSmscampaign.setTitle(FormProperties.getString("Form.WINDOW_TITLE")); //$NON-NLS-1$
			frmSmscampaign.setForeground(Color.BLACK);
			frmSmscampaign.setFont(DEFAULT_FONT);
			frmSmscampaign.setBounds(100, 100, 427, 399);
		}
		return frmSmscampaign;
	}

	private JTabbedPane getJTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.setTabPlacement(JTabbedPane.TOP);
			tabbedPane.setFont(DEFAULT_FONT);
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
			tabbedPane.setBounds(10, 11, 398, 302);
			tabbedPane.addTab(FormProperties.getString("Form.SMS_SERVICE_TAB_TEXT"), (Icon) null, getSMSServicePanel(), //$NON-NLS-1$
					FormProperties.getString("Form.SMS_SERVICE_TAB_TOOL_TIP")); //$NON-NLS-1$
			tabbedPane.addTab(FormProperties.getString("Form.EMAIL_NOTIFICATOR_TAB_TEXT"), null, getEmailNotificator(), //$NON-NLS-1$
					FormProperties.getString("Form.EMAIL_NOTIFICATOR_TAB_TOOL_TIP")); //$NON-NLS-1$
			tabbedPane.addTab(FormProperties.getString("Form.PRIORITY_CONTACTS_TAB_TEXT"), null, getPriorityContacts(),
					FormProperties.getString("Form.PRIORITY_CONTACTS_TAB_TOOL_TIP")); //$NON-NLS-1$
			tabbedPane.addTab(FormProperties.getString("Form.BLACK_LIST_CONTACTS_TAB_TEXT"), null, getBlackListContacts(),
					FormProperties.getString("Form.BLACK_LIST_CONTACTS_TAB_TOOL_TIP")); //$NON-NLS-1$

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
			lblMessageCharactersRemainingCounter.setText(SMSServiceProperties.getString("MensagemSMS.SMS_LENGTH")); //$NON-NLS-1$
			lblMessageCharactersRemainingCounter.setToolTipText(SMSServiceProperties
					.getString("MensagemSMS.SMS_LENGTH")); //$NON-NLS-1$
			lblMessageCharactersRemainingCounter.setHorizontalAlignment(SwingConstants.CENTER);
			lblMessageCharactersRemainingCounter.setBounds(222, 202, 22, 14);
		}

		return lblMessageCharactersRemainingCounter;
	}

	private JLabel getLblPriorityPhoneNumber() {
		if (lblPriorityPhoneNumber == null) {
			lblPriorityPhoneNumber = new JLabel();
			lblPriorityPhoneNumber.setFont(DEFAULT_FONT);
			lblPriorityPhoneNumber.setText(FormProperties.getString("Form.LABEL_PHONE_NUMBER_TEXT")); //$NON-NLS-1$
			lblPriorityPhoneNumber.setLabelFor(fmtTextPriorityPhoneNumber);
			lblPriorityPhoneNumber.setBounds(23, 31, 62, 14);

		}
		return lblPriorityPhoneNumber;
	}

	private JLabel getLblBlackListPhoneNumber() {
		if (lblBlackListPhoneNumber== null) {
			lblBlackListPhoneNumber= new JLabel();
			lblBlackListPhoneNumber.setFont(DEFAULT_FONT);
			lblBlackListPhoneNumber.setText(FormProperties.getString("Form.LABEL_PHONE_NUMBER_TEXT")); //$NON-NLS-1$
			lblBlackListPhoneNumber.setLabelFor(fmtTextPriorityPhoneNumber);
			lblBlackListPhoneNumber.setBounds(23, 31, 62, 14);

		}
		return lblBlackListPhoneNumber;
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
		if (lstNotificationReceiversModel == null) {
			lstNotificationReceiversModel = new DefaultListModel<String>();
		}
		return lstNotificationReceiversModel;
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

	private JList<String> getLstSMSBlackListContacts() {
		if (lstSMSBlackListContacts == null) {
			lstSMSBlackListContacts = new JList<String>();
			lstSMSBlackListContacts.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			lstSMSBlackListContacts.setToolTipText(FormProperties.getString("Form.SMS_BLACK_LST_CONTACTS_TOOL_TIP")); //$NON-NLS-1$
			lstSMSBlackListContacts.setFont(DEFAULT_FONT);
			lstSMSBlackListContacts.setLayoutOrientation(JList.VERTICAL);
			lstSMSBlackListContacts.setModel(getLstSMSBlackListContactsModel());
		}
		return lstSMSBlackListContacts;
	}

	private DefaultListModel<String> getLstSMSPriorityContactsListModel() {
		if (lstSMSPriorityContactsListModel == null) {
			lstSMSPriorityContactsListModel = new DefaultListModel<String>();

		}
		return lstSMSPriorityContactsListModel;
	}

	
	private DefaultListModel<String> getLstSMSBlackListContactsModel() {
		if (lstSMSBlackListContactsModel == null) {
			lstSMSBlackListContactsModel = new DefaultListModel<String>();

		}
		return lstSMSBlackListContactsModel;
	}

	private JPanel getPriorityContacts() {
		if (priorityContacts == null) {
			priorityContacts = new JPanel();
			priorityContacts.setLayout(null);
			priorityContacts.add(getLblPriorityPhoneNumber());
			priorityContacts.add(getFmtTextPriorityPhoneDDD());
			priorityContacts.add(getFmtTextPriorityPhoneNumber());
			priorityContacts.add(getBtnAddPriorityContact());
			priorityContacts.add(getScrollPanePriorityContacts());
			priorityContacts.add(getBtnRemovePriorityPhoneNumber());
			priorityContacts.add(getBtnImportPriorityPhoneNumbers());

		}
		return priorityContacts;
	}

	private JPanel getBlackListContacts() {
		if (blackListContacts == null) {
			blackListContacts = new JPanel();
			blackListContacts.setLayout(null);
			blackListContacts.add(getLblBlackListPhoneNumber());
			blackListContacts.add(getFmtTextBlackListPhoneDDD());
			blackListContacts.add(getFmtTextBlackListPhoneNumber());
			blackListContacts.add(getBtnAddContactInBlackList());
			blackListContacts.add(getScrollPaneContactsInBlackList());
			blackListContacts.add(getBtnRemovePhoneNumberFromBlackList());
		}
		return blackListContacts;
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

	private JScrollPane getScrollPanePriorityContacts() {
		if (scrollPanePriorityContacts == null) {
			scrollPanePriorityContacts = new JScrollPane();
			scrollPanePriorityContacts.setBounds(23, 59, 163, 207);
			scrollPanePriorityContacts.setViewportView(getLstSMSPriorityContactsList());
		}
		return scrollPanePriorityContacts;
	}
	
	private JScrollPane getScrollPaneContactsInBlackList() {
		if (scrollPaneContactsInBlackList == null) {
			scrollPaneContactsInBlackList = new JScrollPane();
			scrollPaneContactsInBlackList.setBounds(23, 59, 163, 207);
			scrollPaneContactsInBlackList.setViewportView(getLstSMSBlackListContacts());
		}
		return scrollPaneContactsInBlackList;
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
						Contato contactObj = new Contato(new Long(contact.replaceAll("\\D", "")));
						SMSServiceWrapper.sendMessage(contactObj.getFormattedContact(), textMessage);
						LOGGER.info("SMS Test Message Sended to: " + contactObj.getFormattedContact() + " with text: "
								+ textMessage);
					} catch (Exception e) {
						LOGGER.error("Erro when Send SMS Test Message ", e);
					}

				}
			});

		}

		return btnTest;
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
		
		SP_PHONE_NUMBER_DOCUMENT_FILTER.installFilter(getFmtTextBlackListPhoneNumber());
		SP_PHONE_NUMBER_DOCUMENT_FILTER.installFilter(getFmtTextPriorityPhoneNumber());
		DDD_DOCUMENT_FILTER.installFilter(getFmtTextBlackListPhoneDDD());
		DDD_DOCUMENT_FILTER.installFilter(getFmtTextPriorityPhoneDDD());

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
		try {
			SystemPrevayler.getSystemPrevaylerModel().loadBlackListContactsFromXml();
			setSMSContactsInBlackList(SystemPrevayler.getSystemPrevaylerModel().getBlackListContacts());
		} catch (Exception e) {
		}
		
		setBlackListDDDPhoneNumber(SP_DDD);
		setPriorityContactDDDPhoneNumber(SP_DDD);

	}

	private void setBlackListDDDPhoneNumber(String string) {
		getFmtTextBlackListPhoneDDD().setText(SP_DDD);
	}

	private void setPriorityContactDDDPhoneNumber(String string) {
		getFmtTextPriorityPhoneDDD().setText(SP_DDD);
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
	public void setSMSPriorityContactsList(List<String> smsPriorityContact) {
		getLstSMSPriorityContactsListModel().clear();
		for (String item : smsPriorityContact) {
			getLstSMSPriorityContactsListModel().addElement(item);
		}
	}

	
	public void setSMSContactsInBlackList(TreeSet<Contato> contatos) {
		getLstSMSBlackListContactsModel().clear();
		for (Contato item : contatos) {
			getLstSMSBlackListContactsModel().addElement(item.getFormattedContact());
		}
	}

	@Override
	public void setTextMessage(String textMessage) {
		getSMSText().setText(textMessage);
	}
	
}
