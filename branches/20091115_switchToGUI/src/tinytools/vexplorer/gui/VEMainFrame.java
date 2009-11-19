/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VEMainFrame.java
 *
 * Created on Nov 11, 2009, 12:56:15 AM
 */

package tinytools.vexplorer.gui;

import javax.swing.JFrame;

import tinytools.vexplorer.EventDispatcher;
import tinytools.vexplorer.FileInfo;
import tinytools.vexplorer.IExplorerListener;
import tinytools.vexplorer.VExplorer;

/**
 *
 * @author krizleebear
 */
public class VEMainFrame extends javax.swing.JFrame implements IExplorerListener {

    /** Creates new form VEMainFrame */
    public VEMainFrame() 
    {
        initComponents();
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH); //start maximized
     
        VExplorer explorer = new VExplorer(new String[]{"./test/testPath1"}); //start business logic
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        statusBar = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jpMovies = new MovieTable();
        jpSettings = new javax.swing.JPanel();
        jpAbout = new AboutPanel();
        jpMenubar = new javax.swing.JPanel();
        btSearchMovies = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("VExplorer");

        statusBar.setPreferredSize(new java.awt.Dimension(400, 20));
        statusBar.setLayout(new javax.swing.BoxLayout(statusBar, javax.swing.BoxLayout.X_AXIS));
        getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);

        jScrollPane1.setViewportView(jpMovies);

        jTabbedPane1.addTab("Movies", jScrollPane1);

        javax.swing.GroupLayout jpSettingsLayout = new javax.swing.GroupLayout(jpSettings);
        jpSettings.setLayout(jpSettingsLayout);
        jpSettingsLayout.setHorizontalGroup(
            jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 379, Short.MAX_VALUE)
        );
        jpSettingsLayout.setVerticalGroup(
            jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 194, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Settings", jpSettings);

        javax.swing.GroupLayout jpAboutLayout = new javax.swing.GroupLayout(jpAbout);
        jpAbout.setLayout(jpAboutLayout);
        jpAboutLayout.setHorizontalGroup(
            jpAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 379, Short.MAX_VALUE)
        );
        jpAboutLayout.setVerticalGroup(
            jpAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 194, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("About", jpAbout);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jpMenubar.setPreferredSize(new java.awt.Dimension(400, 40));

        btSearchMovies.setFocusable(false);
        btSearchMovies.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btSearchMovies.setLabel("Search Movies");
        btSearchMovies.setMaximumSize(new java.awt.Dimension(107, 28));
        btSearchMovies.setMinimumSize(new java.awt.Dimension(57, 18));
        btSearchMovies.setPreferredSize(new java.awt.Dimension(57, 30));
        btSearchMovies.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btSearchMovies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSearchMoviesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpMenubarLayout = new javax.swing.GroupLayout(jpMenubar);
        jpMenubar.setLayout(jpMenubarLayout);
        jpMenubarLayout.setHorizontalGroup(
            jpMenubarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpMenubarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btSearchMovies, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(245, Short.MAX_VALUE))
        );
        jpMenubarLayout.setVerticalGroup(
            jpMenubarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpMenubarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btSearchMovies, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jpMenubar, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>

    private void btSearchMoviesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSearchMoviesActionPerformed
        try
		{
			EventDispatcher.getInstance().startExploring();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block - react with something in the GUI!
			e.printStackTrace();
		}
    }//GEN-LAST:event_btSearchMoviesActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VEMainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSearchMovies;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JEditorPane jpAbout;
    private javax.swing.JPanel jpMenubar;
    private javax.swing.JPanel jpMovies;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JPanel statusBar;
    // End of variables declaration//GEN-END:variables

    public void updateFileList(FileInfo[] filelist) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateFile(FileInfo file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateStatus(int itemsDone, int itemsTotal) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
