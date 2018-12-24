
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class DialogClassHelper extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField mTFName;
    private JTextField mTfViewName;
    private JTextField mTfViewBase;
    private JTextField mTfViewInterface;
    private JTextField mTfPresenterName;
    private JTextField mTfModelName;
    private JRadioButton mRbBean;
    private JTextField mTfBeanName;
    private JTextField mTfPresenterBase;
    private JTextField mTfModelBase;
    private JTextField mTfBeanBase;
    private JTextField mTfPresenterInterface;
    private JTextField mTfModelInterface;
    private JTextField mTfBeanInterface;

    private String mCurrentLoc;

    public DialogClassHelper() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        dispose();
    }

    public void setTtitle(String title) {
        mTFName.setText(title);
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        DialogClassHelper dialog = new DialogClassHelper();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void setCurrentLoc(String currentLoc) {
        this.mCurrentLoc = currentLoc;
    }
}
