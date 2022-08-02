package Graphics_Calculator;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;

public class MainFrame extends JFrame {
    /**
     * 搭建的显示窗口
     * 包含多个小的组件，采用绝对布局
     */
    static final double version = 4.1;
    String combo1Select = "2.0";
    String combo2Select = "FAST CORE";
    boolean chooseFlag1 = false;
    boolean chooseFlag2 = false;
    InputPanel ip = new InputPanel();
    GraphPanel gp = new GraphPanel();
    OutputPanel op = new OutputPanel();
    SettingsPanel sp = new SettingsPanel();
    Container container = getContentPane();
    JScrollPane jsp;

    public MainFrame()  {
        setAllFunction();
        this.setSize(TransformModel.IP_WIDTH+TransformModel.GP_WIDTH
                ,TransformModel.GP_HEIGHT);
        this.setLocationRelativeTo(null);

        container.setLayout(null);
        ip.setBounds(TransformModel.IP_POS_X,TransformModel.IP_POS_Y
                ,TransformModel.IP_WIDTH,TransformModel.IP_HEIGHT);
        container.add(ip);

        op.setBounds(TransformModel.OP_POS_X,TransformModel.OP_POS_Y
                ,TransformModel.OP_WIDTH,TransformModel.OP_HEIGHT);
        container.add(op);

        sp.setBounds(TransformModel.SP_POS_X,TransformModel.SP_POS_Y
                ,TransformModel.SP_WIDTH,TransformModel.SP_HEIGHT);
        container.add(sp);

        gp.setPreferredSize(new Dimension((int) (TransformModel.MAX_WINDOW_WIDTH+CalculateModel.BLANK*2),
                (int) (TransformModel.MAX_WINDOW_HEIGHT+CalculateModel.BLANK*2)));
        jsp = new JScrollPane(gp);

        jsp.getViewport().setViewPosition(new Point(320,330));

        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp.setViewportBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        container.add(jsp);
        jsp.setBounds(TransformModel.GP_POS_X+5,TransformModel.GP_POS_Y+5
                ,TransformModel.GP_WIDTH-20,TransformModel.GP_HEIGHT-45);
    }
    public void launch() {
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Graphics Calculator ver: "+version);
        ImageIcon imageIcon = new ImageIcon("icon/icon.png");
        this.setIconImage(imageIcon.getImage());
        this.setVisible(true);
    }

    private void setAllFunction() {
        ip.plot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Date pre = new Date();
                long Pre = pre.getTime();
                ArrayList<CalculateModel> temp = new ArrayList<>();
                TransformModel T_model = new TransformModel();
                op.jta.append("++act "+(op.actCount++)+" : "+"plot button pressed.\n");
                double[] dorm = new double[2];
                double[] range = new double[2];
                dorm[0] = Double.parseDouble(ip.dorm1.getText());
                dorm[1] = Double.parseDouble(ip.dorm2.getText());
                String ZBL = "x";
                String YBL = "y";
                range[0] = Double.parseDouble(ip.range1.getText());
                range[1] = Double.parseDouble(ip.range2.getText());
                String[] input = new String[3];
                input[0] = ip.tf1.getText();
                input[1] = ip.tf2.getText();
                input[2] = ip.tf3.getText();
                ErrorDetector ed = new ErrorDetector();
                for (int i = 0; i < 3; i++) {
                    int state = ed.judge(input[i]);
                    if (state==ErrorDetector.CORRECT)
                        op.jta.append("   formula" + (i + 1) + " : " + input[i]+"\n");
                    else {
                        op.jta.append("--error "+(op.errorCount++)+" at : formula"+(i+1));
                        if (state==ErrorDetector.BRACKET_NOT_EQUALS) {
                            op.jta.append(" (bracket in wrong pairs)\n");
                            return;
                        } else if (state==ErrorDetector.NO_EQUAL_SIGN) {
                            op.jta.append(" (no equal sign found)\n");
                            return;
                        }
                    }
                }
                T_model.setDormAndRange(dorm, range);
                boolean[] b = new boolean[]{
                        input[0].contains("="),
                        input[1].contains("="),
                        input[2].contains("=")
                };
                for (int i = 0; i < 3; i++) {
                    if (b[i]) {
                        temp.add(new CalculateModel(input[i], ZBL, YBL, T_model, i, combo2Select));
                    }
                }
                gp.setModels(temp, T_model);

                repaint();
                Date aft = new Date();
                long Aft = aft.getTime();
                op.jta.append("++act"+(op.actCount++)+" : "+"graph finished in : "+(Aft-Pre)+" microseconds"+"\n");
                jsp.getViewport().setViewPosition(new Point(320,330));
                //jsp.getViewport().setViewPosition(new Point(320,330));
            }
        });
        ip.center.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jsp.getViewport().setViewPosition(new Point(320,330));
                //jsp.getViewport().setViewPosition(new Point(320,330));
            }
        });
        ip.clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                op.jta.append("++act "+(op.actCount++)+" : "+"clear button pressed.\n");
                ip.tf1.setText("");
                ip.tf2.setText("");
                ip.tf3.setText("");
                ip.dorm1.setText("");
                ip.dorm2.setText("");
                ip.range1.setText("");
                ip.range2.setText("");
            }
        });
        ip.clear1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                op.jta.append("++act "+(op.actCount++)+" : "+"formula1 clear button pressed.\n");
                ip.tf1.setText("");
            }
        });
        ip.clear2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                op.jta.append("++act "+(op.actCount++)+" : "+"formula2 clear button pressed.\n");
                ip.tf2.setText("");
            }
        });
        ip.clear3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                op.jta.append("++act "+(op.actCount++)+" : "+"formula3 clear button pressed.\n");
                ip.tf3.setText("");
            }
        });
        ip.save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                op.jta.append("++act "+(op.actCount++)+" : "+"save button pressed.\n");
                gp.save();
            }
        });
        ip.exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                op.jta.append("++act "+(op.actCount++)+" : "+"exit button pressed.\n");
                System.exit(0);
            }
        });
        sp.combo1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                combo1Select = (String)e.getItem();
                if (chooseFlag1) {
                    op.jta.append("++act "+(op.actCount++)+" : "+"line thickness changed to "+combo1Select+"\n");
                    //chooseFlag1 = !chooseFlag1;
                    gp.setThicknessOfLine(combo1Select);
                }
                chooseFlag1 = !chooseFlag1;
            }
        });
        sp.combo2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                combo2Select = (String)e.getItem();
                if (chooseFlag2) {
                    String name = combo2Select.equals("FAST CORE") ? "fast core":"slow core";
                    op.jta.append("++act "+(op.actCount++)+" : "+"calculate core changed to "+name+"\n");
                    //chooseFlag2 = !chooseFlag2;
                }
                chooseFlag2 = !chooseFlag2;
            }
        });
        sp.checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JCheckBox jcb = (JCheckBox) e.getItem();
                gp.showShuZhi = jcb.isSelected();
                if (gp.showShuZhi)
                    op.jta.append("++act "+(op.actCount++)+" : "+"showing scale\n");
                else op.jta.append("++act "+(op.actCount++)+" : "+"hiding scale\n");
            }
        });
        sp.checkBox1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JCheckBox jcb = (JCheckBox) e.getItem();
                gp.showAxis = jcb.isSelected();
                if (gp.showAxis)
                    op.jta.append("++act "+(op.actCount++)+" : "+"showing axis\n");
                else op.jta.append("++act "+(op.actCount++)+" : "+"hiding axis\n");
            }
        });
    }
}
class InputPanel extends JPanel {

    Box whole = Box.createVerticalBox();
    Box bottom = Box.createHorizontalBox();
    Box upBottom = Box.createHorizontalBox();
    Box form1 = Box.createHorizontalBox();
    Box form2 = Box.createHorizontalBox();
    Box form3 = Box.createHorizontalBox();
    Box headBox = Box.createHorizontalBox();
    JLabel label1 = new JLabel("Formula1: ");
    JLabel label2 = new JLabel("Formula2: ");
    JLabel label3 = new JLabel("Formula3: ");
    JLabel dorm = new JLabel("Dorm: ");
    JLabel range = new JLabel("Range: ");
    JLabel head = new JLabel("OPERATE :");
    JTextField tf1 = new JTextField();
    JTextField tf2 = new JTextField();
    JTextField tf3 = new JTextField();
    JTextField dorm1 = new JTextField();
    JTextField dorm2 = new JTextField();
    JTextField range1 = new JTextField();
    JTextField range2 = new JTextField();
    JButton plot = new JButton("plot");
    JButton clear = new JButton("clear");
    JButton clear1 = new JButton("clear");
    JButton clear2 = new JButton("clear");
    JButton clear3 = new JButton("clear");
    JButton save = new JButton("save");
    JButton exit = new JButton("exit");
    JButton center = new JButton("center");

    public InputPanel() {
        this.setBackground(Color.gray);
        initialize();
    }
    private void setDefaultFormula() {
        plot.setBackground(Color.LIGHT_GRAY);
        plot.setIcon(new ImageIcon("icon/plot.png"));
        plot.setFont(new Font("",Font.BOLD,10));
        clear.setBackground(Color.LIGHT_GRAY);
        clear.setIcon(new ImageIcon("icon/clear.png"));
        clear.setFont(new Font("",Font.BOLD,10));
        clear1.setBackground(Color.LIGHT_GRAY);
        clear1.setIcon(new ImageIcon("icon/clear1.png"));
        clear1.setFont(new Font("",Font.BOLD,10));
        clear2.setBackground(Color.LIGHT_GRAY);
        clear2.setIcon(new ImageIcon("icon/clear2.png"));
        clear2.setFont(new Font("",Font.BOLD,10));
        clear3.setBackground(Color.LIGHT_GRAY);
        clear3.setIcon(new ImageIcon("icon/clear3.png"));
        clear3.setFont(new Font("",Font.BOLD,10));
        save.setBackground(Color.LIGHT_GRAY);
        save.setIcon(new ImageIcon("icon/save.png"));
        save.setFont(new Font("",Font.BOLD,10));
        exit.setBackground(Color.LIGHT_GRAY);
        exit.setIcon(new ImageIcon("icon/exit.png"));
        exit.setFont(new Font("",Font.BOLD,10));
        center.setBackground(Color.LIGHT_GRAY);
        center.setFont(new Font("",Font.BOLD,10));
        tf1.setForeground(new Color(200, 0, 0));
        tf1.setFont(new Font("",Font.ITALIC,14));
        tf1.setBackground(Color.LIGHT_GRAY);
        tf2.setForeground(new Color(0, 160, 0));
        tf2.setFont(new Font("",Font.ITALIC,14));
        tf2.setBackground(Color.LIGHT_GRAY);
        tf3.setForeground(new Color(0, 0, 250));
        tf3.setFont(new Font("",Font.ITALIC,14));
        tf3.setBackground(Color.LIGHT_GRAY);

        label1.setForeground(new Color(200,0,0));
        label1.setFont(new Font("",Font.BOLD,12));
        label2.setForeground(new Color(0,160,0));
        label2.setFont(new Font("",Font.BOLD,12));
        label3.setForeground(new Color(0,0,250));
        label3.setFont(new Font("",Font.BOLD,12));
        range1.setText("-2");
        range1.setBackground(Color.LIGHT_GRAY);
        range2.setText("2");
        range2.setBackground(Color.LIGHT_GRAY);
        dorm1.setText("-2");
        dorm1.setBackground(Color.LIGHT_GRAY);
        dorm2.setText("2");
        dorm2.setBackground(Color.LIGHT_GRAY);
    }
    public void initialize() {
        this.setDefaultFormula();
        headBox.add(head);

        bottom.add(plot);
        bottom.add(clear);
        bottom.add(center);
        bottom.add(save);
        bottom.add(exit);

        upBottom.add(range);
        upBottom.add(range1);
        upBottom.add(range2);
        upBottom.add(Box.createHorizontalStrut(5));
        upBottom.add(dorm);
        upBottom.add(dorm1);
        upBottom.add(dorm2);

        form1.add(label1);
        form1.add(Box.createHorizontalStrut(5));
        form1.add(tf1);
        form1.add(clear1);
        form2.add(label2);
        form2.add(Box.createHorizontalStrut(5));
        form2.add(tf2);
        form2.add(clear2);
        form3.add(label3);
        form3.add(Box.createHorizontalStrut(5));
        form3.add(tf3);
        form3.add(clear3);

        whole.add(headBox);
        whole.add(Box.createVerticalStrut(3));
        whole.add(form1);
        whole.add(form2);
        whole.add(form3);
        whole.add(Box.createVerticalStrut(3));
        whole.add(upBottom);
        whole.add(Box.createVerticalStrut(5));
        whole.add(bottom);

        this.add(whole);
    }

}
class OutputPanel extends JPanel {

    Box whole = Box.createVerticalBox();
    Box headBox = Box.createHorizontalBox();
    Box bottom = Box.createHorizontalBox();
    JTextArea jta = new JTextArea();
    JLabel head = new JLabel("REPORT");
    JScrollPane jsp;
    int actCount = 1;
    int errorCount = 1;

    public OutputPanel() {
        this.setBackground(Color.GRAY);
        jta.setSize(new Dimension(350,240));
        jta.setFont(new Font("",Font.ITALIC,14));
        jta.setBackground(Color.LIGHT_GRAY);
        jta.setRows(15);
        jta.setEditable(false);
        jta.setLineWrap(true);
        jsp = new JScrollPane(jta);
        jsp.setBackground(Color.LIGHT_GRAY);
        jsp.setSize(350,240);
        JScrollBar vb = new JScrollBar(JScrollBar.VERTICAL,0,20,0,280);
        vb.setBackground(Color.WHITE);
        vb.setForeground(Color.darkGray);
        jsp.setVerticalScrollBar(vb);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setViewportBorder(new BevelBorder(BevelBorder.LOWERED));
        headBox.add(head);
        bottom.add(jsp);
        whole.add(headBox);
        whole.add(bottom);
        this.add(whole);
    }
}
class SettingsPanel extends JPanel {

    JLabel head = new JLabel("SETTINGS");
    JLabel thickness = new JLabel("THICKNESS : ");
    JLabel show = new JLabel("SHOW SCALE :");
    JLabel show1 = new JLabel("SHOW AXIS :");
    JLabel core = new JLabel("CORE :");
    /**
     *     JLabel THR1 = new JLabel("THRESHOLD :");
     *     JLabel WAL1 = new JLabel("WALK LENGTH :");
     *     JTextField thr1 = new JTextField();
     *     JTextField wal1 = new JTextField();
     *     JLabel THR2 = new JLabel("THRESHOLD :");
     *     JLabel WAL2 = new JLabel("WALK LENGTH :");
     *     JTextField thr2 = new JTextField();
     *     JTextField wal2 = new JTextField();
     *     JLabel THR3 = new JLabel("THRESHOLD :");
     *     JLabel WAL3 = new JLabel("WALK LENGTH :");
     *     JTextField thr3 = new JTextField();
     *     JTextField wal3 = new JTextField();
     */
    Box headBox = Box.createHorizontalBox();
    Box b1 = Box.createHorizontalBox();
    Box b2 = Box.createHorizontalBox();
    Box b3 = Box.createHorizontalBox();
    Box b4 = Box.createHorizontalBox();
    Box whole = Box.createVerticalBox();
    JCheckBox checkBox = new JCheckBox();
    JCheckBox checkBox1 = new JCheckBox("",true);
    JComboBox<String> combo1 = new JComboBox<>(new String[]{"DEFAULT","1.0","2.0","3.0","4.0","5.0"});
    JComboBox<String> combo2 = new JComboBox<>(new String[]{"FAST CORE","SLOW CORE"});

    public SettingsPanel() {
        this.setBackground(Color.gray);
        combo1.setBackground(Color.LIGHT_GRAY);
        //combo1.setSize(15,10);
        combo2.setBackground(Color.LIGHT_GRAY);
        //combo2.setSize(15,10);
        checkBox.setBackground(Color.gray);
        checkBox1.setBackground(Color.gray);
        thickness.setFont(new Font("",Font.BOLD,10));
        show.setFont(new Font("",Font.BOLD,10));
        show1.setFont(new Font("",Font.BOLD,10));
        core.setFont(new Font("",Font.BOLD,10));
        headBox.add(head);
        b1.add(show);
        b1.add(checkBox);
        b1.add(Box.createHorizontalStrut(10));
        b2.add(thickness);
        b2.add(combo1);
        b3.add(core);
        b3.add(Box.createHorizontalStrut(10));
        b3.add(combo2);

        /**
         *         THR1.setFont(new Font("",Font.BOLD,10));
         *         b3.add(THR1);
         *         thr1.setText("10");
         *         thr1.setFont(new Font("",Font.BOLD,10));
         *         b3.add(thr1);
         *         b3.add(Box.createHorizontalStrut(5));
         *         WAL1.setFont(new Font("",Font.BOLD,10));
         *         b3.add(WAL1);
         *         wal1.setText("8");
         *         wal1.setFont(new Font("",Font.BOLD,10));
         *         b3.add(wal1);
         *
         *         THR2.setFont(new Font("",Font.BOLD,10));
         *         b4.add(THR2);
         *         thr2.setText("10");
         *         thr2.setFont(new Font("",Font.BOLD,10));
         *         b4.add(thr2);
         *         b4.add(Box.createHorizontalStrut(5));
         *         WAL2.setFont(new Font("",Font.BOLD,10));
         *         b4.add(WAL2);
         *         wal2.setText("8");
         *         wal2.setFont(new Font("",Font.BOLD,10));
         *         b4.add(wal2);
         *
         *         THR3.setFont(new Font("",Font.BOLD,10));
         *         b5.add(THR3);
         *         thr3.setText("10");
         *         thr3.setFont(new Font("",Font.BOLD,10));
         *         b5.add(thr3);
         *         b5.add(Box.createHorizontalStrut(5));
         *         WAL3.setFont(new Font("",Font.BOLD,10));
         *         b5.add(WAL3);
         *         wal3.setText("8");
         *         wal3.setFont(new Font("",Font.BOLD,10));
         *         b5.add(wal3);
         */


        b1.add(show1);
        b1.add(checkBox1);
        whole.add(headBox);
        whole.add(Box.createVerticalStrut(8));
        whole.add(b1);
        whole.add(Box.createVerticalStrut(8));
        whole.add(b2);
        whole.add(Box.createVerticalStrut(8));
        whole.add(b3);
        this.add(whole);
    }
}