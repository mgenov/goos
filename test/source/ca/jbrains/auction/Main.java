package ca.jbrains.auction;

import static ca.jbrains.auction.ApplicationRunner.STATUS_JOINING;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class Main {
	public static class MainWindow extends JFrame {
		private static final long serialVersionUID = -6155423004752976439L;

		private JLabel sniperStatus = createLabel(STATUS_JOINING);

		public MainWindow() {
			super("Auction Sniper");
			setName(MAIN_WINDOW_NAME);
			add(sniperStatus);
			pack();
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		}

		private static JLabel createLabel(String text) {
			final JLabel label = new JLabel(text);
			label.setName("sniper status");
			label.setBorder(new LineBorder(Color.BLACK));
			return label;
		}
	}

	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	public static final String SNIPER_STATUS_NAME = "sniper status";
	private MainWindow ui;

	public Main() throws Exception {
		startUserInterface();
	}

	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				ui = new MainWindow();
			}
		});
	}

	public static void main(String... args) throws Exception {
		new Main();
		final XMPPConnection connection = connectTo(args[0], args[1], args[2]);
		final Chat chat = connection.getChatManager().createChat(
				auctionId(args[3], connection), new MessageListener() {
					@Override
					public void processMessage(Chat chat, Message message) {
						// Don't do anything yet
					}
				});
		chat.sendMessage(new Message());
	}

	private static String auctionId(String itemId, XMPPConnection connection) {
		return String.format("auction-%s@%s/Auction", itemId,
				connection.getServiceName());
	}

	private static XMPPConnection connectTo(String hostname, String username,
			String password) throws XMPPException {
		final XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, "Auction");
		return connection;
	}
}