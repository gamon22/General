package com.home.UI;

import com.home.custom.Throwexceptions.ThrowTableException;
import com.home.custom.exceptions.TablesNotFoundException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class DictionaryLookUp implements ActionListener {
	JFrame frame;
	JFrame frame1;
	JTextField textbox;
	JLabel label;
	JButton button;
	JButton bupdate;
	JButton bclear;
	JPanel panel;
	static JTable table;
	int band = 0, TableNumber = 0, ntable = 0;
	Component ob;
	Connection con, con2;
	ThrowTableException throwTableException;
	String sql, sql2;
	String driverName = "com.mysql.jdbc.Driver";
	String[] columnNumber0 = { "id", "keyword", "LinuxCommand", "Details", "Description" };
	String[] columNumber1 = { "id", "Title", "Link" };
	String[] columNumber2={"id", "Topic", "Description", "Link", "Link Relation"};
	String[] columNumber3 = { "id", "Title", "Link" };
	String[] columNumber4 = { "id", "Title", "Description", "Link" };
	String[] columNumber5 = { "id", "keyword", "type", "date", "time", "name", "brand", "model", "serial number", "values", "Relation With", "details", "input", "output", "comments" };
	String[][] columns = { columnNumber0, columNumber1, columNumber2, columNumber3, columNumber4, columNumber5};
	Process runtimeProcess;
	Properties prop;
	InputStream input;

	public DictionaryLookUp() {
		prop = new Properties();
		input = null;
		File file = new File("/opt/DictionaryLookupSW/config.properties");
		File file2 = new File("/opt/DictionaryLookupSW/fromServertoLocalhost.sh");
		File file3 = new File("/opt/DictionaryLookupSW/localhostToserver.sh");
		File file4 = new File("/opt/DictionaryLookupSW/progressbar.sh");
		if (file.exists() == false)
			JOptionPane.showMessageDialog(null, "/opt/DictionaryLookupSW/config.properties", "File Not Found",
					JOptionPane.WARNING_MESSAGE);
		if (file2.exists() == false)
			JOptionPane.showMessageDialog(null, "/opt/DictionaryLookupSW/fromServertoLocalhost.sh", "File Not Found",
					JOptionPane.WARNING_MESSAGE);
		if (file3.exists() == false)
			JOptionPane.showMessageDialog(null, "/opt/DictionaryLookupSW/localhostToserver.sh", "File Not Found",
					JOptionPane.WARNING_MESSAGE);
		if (file4.exists() == false)
			JOptionPane.showMessageDialog(null, "/opt/DictionaryLookupSW/progressbar.sh", "File Not Found",
					JOptionPane.WARNING_MESSAGE);
		try {
			input = new FileInputStream("/opt/DictionaryLookupSW/config.properties");
			prop.load(input);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage().toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void createUI() {
		frame = new JFrame("Database Search Result");
		frame.setDefaultCloseOperation(3);
		frame.setLayout(null);
		textbox = new JTextField();
		textbox.setBounds(170, 30, 150, 20);
		label = new JLabel("KeyWord to Search:");
		label.setBounds(10, 30, 150, 20);
		button = new JButton("search");
		button.setBounds(80, 100, 150, 20);
		button.addActionListener(this);
		bupdate = new JButton("update");
		bclear=new JButton("clear");
		bclear.setBounds(160, 140, 150, 20);
		bclear.addActionListener(this);
		bupdate.setBounds(250, 100, 150, 20);
		bupdate.addActionListener(this);
		frame.add(bclear);
		frame.add(textbox);
		frame.add(label);
		frame.add(button);
		frame.add(bupdate);
		frame.setVisible(true);
		frame.setSize(500, 400);
	}

	public void actionPerformed(ActionEvent ae) {
		String bSelected = "";
		button = ((JButton) ae.getSource());
		bSelected = this.button.getLabel();
		System.out.println("Showing Table Data.......");
		System.out.println("Selected Button" + bSelected);
		if(bSelected.equals("clear"))
			clearTextbox();
		if (bSelected.equals("search"))
			showTableData();
		else if (bSelected.equals("update"))
			try {
				updatedb();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
	}
	
	public void clearTextbox(){
		textbox.setText("");
	}

	private void updatedb() {
		System.out.println("is going to update");

		int errorType = 0;
		try {
			Class.forName(driverName);

			con = DriverManager.getConnection(prop.getProperty("externalhosturl"),
					prop.getProperty("externalhostuserName"), prop.getProperty("externalhostpassword"));
			sql = prop.getProperty("countTables");
			con2 = DriverManager.getConnection(prop.getProperty("localhosturl"),
					prop.getProperty("localhostuserName"), prop.getProperty("localhostpassword"));
			sql2 = prop.getProperty("countTables");

			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			PreparedStatement ps2 = con2.prepareStatement(sql2);
			ResultSet rs2 = ps2.executeQuery();
			throwTableException = new ThrowTableException();
			if (rs.next()) {
				System.out.println("Tables Found:" + Integer.parseInt(rs.getString("count(*)")));
			}
			if(rs2.next())
			//throwTableException.checkTables(Integer.parseInt(rs.getString("count(*)")), Integer.parseInt(rs2.getString("count(*)")));
				throwTableException.checkTables(Integer.parseInt(rs.getString("count(*)")), Integer.parseInt(rs2.getString("count(*)")));
			con.close();
		} catch (TablesNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "Error while reading database data              ",
					"database corrupted!", JOptionPane.WARNING_MESSAGE);

			errorType = 1;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error in Database", "Database not found!", JOptionPane.ERROR_MESSAGE);

			errorType = 2;
		}

		if (errorType > 0) {
			System.out.println("Restoring Database from LocalHost");
			JOptionPane.showMessageDialog(null, "from LocalHost to Server!     ", "Restoring Database",
					JOptionPane.INFORMATION_MESSAGE);
			try {
				String[] cmd = { "/bin/sh", "/opt/DictionaryLookupSW/localhostToserver.sh" };
				Process pr = Runtime.getRuntime().exec(cmd);

				String[] cmd2 = { "/bin/sh", "/opt/DictionaryLookupSW/progressbar.sh" };

				Runtime.getRuntime().exec(cmd2);

				bupdate.setEnabled(false);
				button.setEnabled(false);
				Thread.sleep(50000);
				bupdate.setEnabled(true);
				button.setEnabled(true);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			try {
				JOptionPane.showMessageDialog(null, "Database is ok     ", "Database", 1);
				String[] cmd = { "/bin/sh", "/opt/DictionaryLookupSW/fromServertoLocalhost.sh" };
				Process pr = Runtime.getRuntime().exec(cmd);
				String[] cmd2 = { "/bin/sh", "/opt/DictionaryLookupSW/progressbar.sh" };
				Runtime.getRuntime().exec(cmd2);

				bupdate.setEnabled(false);
				button.setEnabled(false);
				Thread.sleep(50000);
				bupdate.setEnabled(true);
				button.setEnabled(true);
				JOptionPane.showMessageDialog(null, "Database's Localhost", "Database Updated",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void showTableData() {
		frame1 = new JFrame("Database Search Result");
		frame1.setLayout(new BorderLayout());
		int i = 0;
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns[TableNumber]);
		table = new JTable();
		table.setModel(model);
		table.setAutoResizeMode(4);
		table.setFillsViewportHeight(true);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setHorizontalScrollBarPolicy(30);
		scroll.setVerticalScrollBarPolicy(20);
		String textvalue = textbox.getText();
		String id = "", type="", name="", date="", time="", brand="", mmodel="", input="", output="", comments="";
		String keyword = "", title = "", topic="", serialno="", values="", relationw;
		String linuxCommand = "", link = "", link2="";
		String details = "";
		String description = "";
		try {
			Class.forName(driverName);
			con = DriverManager.getConnection(prop.getProperty("localhosturl"), prop.getProperty("localhostuserName"),
					prop.getProperty("localhostpassword"));
			sql = prop.getProperty("countTables");

			PreparedStatement ps2 = con.prepareStatement(sql);
			ResultSet rs2 = ps2.executeQuery();
			throwTableException = new ThrowTableException();
			if (rs2.next()) {
				ntable = Integer.parseInt(rs2.getString("count(*)"));
			}

			if ((textvalue.equals("")) || (textvalue.equals(null)) || textvalue.equals(" "))
				sql = prop.getProperty("simpleSelect" + TableNumber);
			else
				sql = prop.getProperty("selectCondition" + TableNumber) + " '%" + textvalue + "%'";
			System.out.println("Query output: " + sql);
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				if (TableNumber == 0) {
					id = rs.getString("id");
					keyword = rs.getString("keyword");
					linuxCommand = rs.getString("linuxCommand");
					details = rs.getString("details");
					description = rs.getString("description");
					model.addRow(new Object[] { id, keyword, linuxCommand, details, description });
					TableNumber = 0;
				} else if (TableNumber == 1) {
					id = rs.getString("id");
					title = rs.getString("title");
					link = rs.getString("link");
					model.addRow(new Object[] { id, title, link });
					TableNumber = 0;
				}else if (TableNumber == 2) {
					id = rs.getString("id");
					topic = rs.getString("topic");
					description= rs.getString("description");
					link = rs.getString("link");
					link2=rs.getString("linkrelation");
					model.addRow(new Object[] { id, topic, description, link, link2});
					TableNumber = 0;
				}else if (TableNumber == 3) {
					id = rs.getString("id");
					title = rs.getString("title");
					link = rs.getString("link");
					model.addRow(new Object[] { id, title, link });
					TableNumber = 0;
				}else if (TableNumber == 4) {
					id = rs.getString("id");
					title = rs.getString("title");
					description = rs.getString("description");
					link = rs.getString("link");
					model.addRow(new Object[] { id, title, description, link });
					TableNumber = 0;
				}else if (TableNumber == 5) {
					id = rs.getString("id");
					keyword = rs.getString("keyword");
					type = rs.getString("type");
					date = rs.getString("date");
					time = rs.getString("time");
					name = rs.getString("name");
					brand = rs.getString("brand");
					mmodel = rs.getString("model");
					serialno = rs.getString("serialno");
					values = rs.getString("valuess");
					relationw = rs.getString("relationw");
					details = rs.getString("details");
					input = rs.getString("input");
					output = rs.getString("output");
					comments = rs.getString("comments");
					model.addRow(new Object[] { id, keyword, type, date, time, name, brand, mmodel, serialno, values, relationw, details, input, output, comments });
					TableNumber = 0;
				}
								

				i++;
				ntable = 0;
			}
			if (i < 1) {
				if(TableNumber<7)
				TableNumber++;
				if (TableNumber < ntable) {
					showTableData();
				}  
				if(TableNumber==6){
					con.close();
					TableNumber = 0;
					JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
					frame1.dispose();
				}
			} 
			con.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error ", JOptionPane.ERROR_MESSAGE);
		}

		if (i > 0) {
			frame1.add(scroll);
			frame1.setVisible(true);
			if (i < 701)
				frame1.setSize(1000, 100 * i);
			else
				frame.setSize(1000, 700);
		}
	}

	public static void main(String[] args) {
		DictionaryLookUp dl = new DictionaryLookUp();
		dl.createUI();
	}
}