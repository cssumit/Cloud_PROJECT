package com.iiit.cloud.sparkstreaming;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserFactory;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class JBrowser {
	final Browser browser;
	JFrame frame;
	public JBrowser(){
		browser= BrowserFactory.create();
		frame = new JFrame("JxBrowser - Hello World");
	}
	public void browser(String url)
	{
			
		       
	        final JTextField addressBar = new JTextField(url);
	        addressBar.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                browser.loadURL(addressBar.getText());
	            }
	        });
	        
	        JPanel addressPane = new JPanel(new BorderLayout());
	        addressPane.add(new JLabel(" URL: "), BorderLayout.WEST);
	        addressPane.add(addressBar, BorderLayout.CENTER);

	        
	        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        frame.add(addressPane, BorderLayout.NORTH);
	        frame.add(browser.getView().getComponent(), BorderLayout.CENTER);
	        //frame.setSize(1000, 800);
	        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	       /* try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        frame.setVisible(false);*/
	        browser.loadURL(addressBar.getText());
		}

}
