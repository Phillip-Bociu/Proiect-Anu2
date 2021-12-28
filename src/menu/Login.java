package menu;

import javax.swing.*;

import main.Game;
import networking.LoginConfirmationPacket;
import networking.LoginPacket;
import networking.RegisterConfirmationPacket;
import networking.RegisterPacket;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Login extends JFrame implements ActionListener {

	private static final long serialVersionUID = -2158562903267220028L;
	Container container = getContentPane();
	JLabel usernameLabel = new JLabel("USERNAME");
	JLabel passwordLabel = new JLabel("PASSWORD");
	JTextField userTextField = new JTextField();
	JPasswordField passwordField = new JPasswordField();
	JButton loginButton = new JButton("LOGIN");
	JButton resetButton = new JButton("RESET");
	JButton registerButton = new JButton("Register");

	InetAddress ipAddress;
	DatagramSocket socket;

	public Login(String ipAddress) {
		try {

			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setLayoutManager();
		setLocationAndSize();
		addComponentsToContainer();
		addActionEvent();
	}

	public void setLayoutManager() {
		container.setLayout(null);
	}

	public void setLocationAndSize() {
		usernameLabel.setBounds(50, 20, 100, 30);
		passwordLabel.setBounds(50, 60, 100, 30);
		userTextField.setBounds(150, 20, 150, 30);
		passwordField.setBounds(150, 60, 150, 30);
		loginButton.setBounds(50, 120, 100, 30);
		resetButton.setBounds(200, 120, 100, 30);
		registerButton.setBounds(125, 170, 100, 30);

	}

	public void addComponentsToContainer() {
		container.add(usernameLabel);
		container.add(passwordLabel);
		container.add(userTextField);
		container.add(passwordField);
		container.add(loginButton);
		container.add(resetButton);
		container.add(registerButton);
	}

	public void addActionEvent() {
		loginButton.addActionListener(this);
		resetButton.addActionListener(this);
		registerButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final Object src = e.getSource();
		if (src == loginButton) {

			LoginPacket loginPacket = new LoginPacket(userTextField.getText(), String.valueOf(passwordField.getPassword()));

			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);

			try {
				packet.setData(loginPacket.toBytes());
				socket.send(packet);
				packet.setData(data);
				socket.receive(packet);
				LoginConfirmationPacket conf = new LoginConfirmationPacket();
				conf.readFromBytes(packet.getData());

				if (conf.ID == -1) {
					// Login Failed
					System.out.println("Fail");
				} else {
					// Login Succeded
					System.out.println("Success");
					this.setVisible(false);
					new Game(userTextField.getText(), conf.elo, conf.ID, socket, ipAddress).start();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} else if (src == resetButton) {
			userTextField.setText("");
			passwordField.setText("");
		} else if (src == registerButton) {
			RegisterPacket rp = new RegisterPacket(userTextField.getText(), String.valueOf(passwordField.getPassword()));

			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);

			try {
				packet.setData(rp.toBytes());
				socket.send(packet);
				packet.setData(data);
				socket.receive(packet);
				RegisterConfirmationPacket conf = new RegisterConfirmationPacket();
				conf.readFromBytes(packet.getData());
				if(conf.registered)
				{
					System.out.println("Registered");
				} else
				{
					System.out.println("Not Registered");					
				}
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}