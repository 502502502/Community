package com.ningct.db2;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class guiTest {

    private JFrame jf = new JFrame("test");

    public void init(){
        jf.setBounds(400,200,1200,800);
        jf.setLayout(new FlowLayout(FlowLayout.LEFT));

    }
    public void last(){
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public void jDialog(){
        init();
        JDialog jd = new JDialog(jf,"jd");
        jd.setSize(800,600);
        jd.setLocation(400,300);
        jd.setVisible(true);
        jd.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        last();
    }
    public void jPanel(){
        init();
        JPanel jp = new JPanel(new FlowLayout());
        JButton jb1 = new JButton("button1");
        JButton jb2 = new JButton("button2");
        jp.add(jb1);
        jp.add(jb2);
        jf.add(jp);

        last();
    }
    public void jScrollPanel(){
        init();
        JPanel jp = new JPanel(new FlowLayout());
        jp.setPreferredSize(new Dimension(1000,700));
        JScrollPane jsp = new JScrollPane(jp);
        jsp.setSize(800,600);
        jf.add(jsp);
        last();
    }

    public void jLable(){
        init();
        JLabel jl = new JLabel("芜湖",SwingConstants.CENTER);
        jf.add(jl);
        last();
    }
    public void jButton(){
        init();
        JButton jb = new JButton("button");
        //是否可用
        jb.setEnabled(true);
        //边界是否显示
        jb.setBorderPainted(true);
        jf.add(jb);
        last();
    }
    public void jRadio(){
        init();
        JRadioButton jr1 = new JRadioButton("男");
        JRadioButton jr2 = new JRadioButton("女");
        JRadioButton jr3 = new JRadioButton("中性");
        ButtonGroup group = new ButtonGroup();
        group.add(jr1);
        group.add(jr2);
        group.add(jr3);
        jf.add(jr1);
        jf.add(jr2);
        jf.add(jr3);
        last();
    }
    public void jCheck(){
        init();
        JCheckBox jc1 = new JCheckBox("吃饭", true);
        JCheckBox jc2 = new JCheckBox("吃饭", false);
        JCheckBox jc3 = new JCheckBox("吃饭", false);
        jf.add(jc1);
        jf.add(jc2);
        jf.add(jc3);
        last();
    }
    public void jCombo(){
        init();
        JComboBox jc = new JComboBox();
        jc.addItem("请选择");
        jc.addItem("足球");
        jc.addItem("篮球");
        jc.addItem("网球");
        jf.add(jc);
        last();
    }
    public void jMenu(){
        init();
        JMenuBar jMenuBar = new JMenuBar();

        JMenu jMenu = new JMenu("菜单");
        JMenu jMenu1 = new JMenu("菜单1");
        JMenu jMenu2 = new JMenu("菜单2");

        JMenuItem item = new JMenuItem("选项");
        JMenuItem item1 = new JMenuItem("选项1");
        JMenuItem item2 = new JMenuItem("选项2");
        JMenuItem item3 = new JMenuItem("选项3");
        JMenuItem item4 = new JMenuItem("选项4");

        jMenu.add(item);
        jMenu.add(item1);
        jMenu1.add(item3);
        jMenu1.add(item4);
        jMenu2.add(item2);
        jMenu2.add(item1);

        jMenuBar.add(jMenu);
        jMenuBar.add(jMenu1);
        jMenuBar.add(jMenu2);

        jf.add(jMenuBar);
        last();
    }
    public void jText(){
        init();

        JTextField jt = new JTextField("请输入",20);
        jf.add(jt);

        JTextArea ja = new JTextArea(5,6);
        ja.setLineWrap(true);
        jf.add(ja);

        last();
    }
    public void jPassword(){
        init();
        JPasswordField jp = new JPasswordField("634r8",20);
        jp.setEchoChar('%');
        jf.add(jp);
        last();
    }
    public void jFlowLayout(){
        init();
        jf.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton jb = new JButton("button");
        JButton jb1 = new JButton("button1");
        JButton jb2 = new JButton("button2");
        JButton jb3 = new JButton("button3");
        JButton jb4 = new JButton("button4");
        JButton jb5 = new JButton("button5");
        JButton jb6 = new JButton("button6");
        JButton jb7 = new JButton("button7");

        jf.add(jb);
        jf.add(jb1);
        jf.add(jb2);
        jf.add(jb3);
        jf.add(jb4);
        jf.add(jb5);
        jf.add(jb6);
        jf.add(jb7);


        last();
    }
    public void jBorderLayout(){
        init();
        jf.setLayout(new BorderLayout());

        JButton jb = new JButton("button");
        JButton jb1 = new JButton("button1");
        JButton jb2 = new JButton("button2");
        JButton jb3 = new JButton("button3");
        JButton jb4 = new JButton("button4");

        jf.add(jb, BorderLayout.NORTH);
        jf.add(jb1, BorderLayout.SOUTH);
        jf.add(jb2, BorderLayout.EAST);
        jf.add(jb3, BorderLayout.WEST);
        jf.add(jb4);


        last();
    }
    public void jGridLayout(){
        init();
        jf.setLayout(new GridLayout(2,2,10,10));

        JButton jb = new JButton("button");
        JButton jb1 = new JButton("button1");
        JButton jb2 = new JButton("button2");
        JButton jb3 = new JButton("button3");

        jf.add(jb);
        jf.add(jb1);
        jf.add(jb2);
        jf.add(jb3);


        last();
    }
    public void jAction(){
        init();

        JTextArea textArea = new JTextArea(10,20);
        textArea.setLineWrap(true);
        JButton button = new JButton("点击输出");
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.append("芜湖啊");
            }
        });
        jf.add(textArea);
        jf.add(button);

        last();
    }


    public static void main(String[] args) {
        guiTest gui = new guiTest();

        //gui.jDialog();
        //gui.jPanel();
        //gui.jScrollPanel();
        //gui.jLable();
        //gui.jButton();
        //gui.jRadio();
        gui.jCheck();
        //gui.jCombo();
        //gui.jMenu();
        //gui.jText();
        //gui.jPassword();
        //gui.jText();
        //gui.jFlowLayout();
        //gui.jBorderLayout();
        //gui.jGridLayout();
        //gui.jAction();
    }
}
