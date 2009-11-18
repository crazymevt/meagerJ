/**
 * This is a generic framework for any application that uses plugins.
 * It auto loads plugins that are located in the modules directory.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author Jessie Hughart
 */
public class Main extends JFrame implements ActionListener{
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu modulesMenu;
    private JMenu helpMenu;
    private ArrayList modules;

    /**
     * Create the main Gui, make the menu bar, and load any plugins.
     */
    public Main()
    {
        setTitle("Desktop Application Framework");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        makeMenus();
        setJMenuBar(menuBar);

        loadModules();

        setSize(800, 600);
        setVisible(true);
    }

    /**
     * Create a new menu item
     *
     * @param name - the name of the menu item
     * @return - the new menu item
     */
    private JMenuItem newMenuItem (String name)
    {
        JMenuItem menu = new JMenuItem (name);
        menu.addActionListener(this);
        return menu;
    }

    /**
     * Create the menus for the application
     */
    private void makeMenus()
    {
        menuBar = new JMenuBar();

        fileMenu = new JMenu ("File");
        // extra items in the file menu, should be added here.
        fileMenu.addSeparator();
        fileMenu.add(newMenuItem("Exit"));

        modulesMenu = new JMenu ("Modules");    // no modules loaded yet, so lets disable menu until they are loaded.
        modulesMenu.setEnabled(false);

        helpMenu = new JMenu ("Help");
        helpMenu.add(newMenuItem("About"));

        menuBar.add(fileMenu);
        menuBar.add(modulesMenu);
        menuBar.add(helpMenu);
    }

    /**
     * Load the dynamic modules (plugins) from the ./modules directory
     */
    private void loadModules() {
        modules = new ArrayList();
        File dir = new File("modules");     // this is the directory where modules (plugins) go.

        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        };

        String[] children = dir.list(filter);
        if (children == null) {
            System.out.println ("Directory does not exist");
        }
        else {
            for (int i=0; i<children.length; i++) {
                // Get filename of file or directory
                String filename = children[i];
                System.out.println (filename);
                try{
                    ModuleInterface module;
                    int period = filename.indexOf(".");
                    String path = filename.substring(0, period);
                    module = (ModuleInterface)Class.forName(path).getConstructor().newInstance();

                    modules.add(module);
                    modulesMenu.add(newMenuItem (module.getMenu()));
                    modulesMenu.setEnabled(true);

                } catch (Exception e) {
                    System.out.println ("Opps Module " + filename + " could not load");
                }
            }
        }
    }

    /**
     * This handles the application events
     *
     * @param e - the event that was triggered
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit"))
            System.exit(0);
        if (e.getActionCommand().equals("About"))
        {
            JOptionPane.showMessageDialog(this,
                    "Desktop Application Framework\n\n"
                    + "By:  Jessie Hughart\n"
                    + "(C) 2009");
            return;
        }
        // this section handles selecting a menu from a dynamically loaded module
        ModuleInterface module;
        for (int i = 0; i < modules.size(); i++)
        {
            module = (ModuleInterface)modules.get(i);
            if (module.getMenu().equals(e.getActionCommand()))
            {
                module.setVisible(true);
                return;
            }
        }
    }

//==============================================================================

    public static void main(String[] args) {
        Main app = new Main();
    }
}
