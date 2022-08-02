package Graphics_Calculator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

class GraphPanel extends JPanel {
    /**
     * 专门用于绘制图形的类
     * 接受散点以及代表如何绘制这个图形的变量，选择方法绘制图形
     */
    static final double xl = 0.95;
    static final double xr = 0.90;
    static final double yd = 0.97;
    static final double yu = 0.97;
    static final int UP = 1;
    static final int DOWN = 2;
    static final int RIGHT = 3;
    static final int LEFT = 4;
    static final int FORMULA_LAYOUT_HEIGHT = 650;
    static final int FORMULA_LAYOUT_INTERVAL = 20;
    static final int DOT_INTERVAL = 80;
    int THICKNESS_LINE = 2;
    boolean showShuZhi = false;
    boolean showAxis = true;
    boolean paintImage = false;

    DecimalFormat fmt = new DecimalFormat("0.###");
    CalculateModel model_1;
    PointMap.Point[] points_1;
    CalculateModel model_2;
    PointMap.Point[] points_2;
    CalculateModel model_3;
    PointMap.Point[] points_3;

    TransformModel T_model;

    public GraphPanel() {
    }

    public void setThicknessOfLine(String thickness) {
        if (thickness.equals("DEFAULT")) this.THICKNESS_LINE = 2;
        else this.THICKNESS_LINE = (int) Double.parseDouble(thickness);
    }
    public void setModels(ArrayList<CalculateModel> models,TransformModel T_model) {
        int size = models.size();
        if (size == 1) {
            this.model_1 = models.get(0);
            points_1 = (model_1.getAllScatters()).toPoint();
            model_2 = null;
            model_3 = null;
            this.T_model = T_model;
        } else if (size == 2) {
            this.model_1 = models.get(0);
            this.model_2 = models.get(1);
            points_1 = (model_1.getAllScatters()).toPoint();
            points_2 = (model_2.getAllScatters()).toPoint();
            model_3 = null;
            this.T_model = T_model;
        } else if (size == 3) {
            this.model_1 = models.get(0);
            this.model_2 = models.get(1);
            this.model_3 = models.get(2);
            points_1 = (model_1.getAllScatters()).toPoint();
            points_2 = (model_2.getAllScatters()).toPoint();
            points_3 = (model_3.getAllScatters()).toPoint();
            this.T_model = T_model;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.fillRect(TransformModel.ORI_POINT_X-1, TransformModel.ORI_POINT_Y-1, 3, 3);
        Graphics2D g2d = (Graphics2D) g;
        if(showAxis) this.Zhou(g2d, T_model);
        g2d.setColor(this.readColor(model_1));
        this.QuXian_Third(g2d, points_1,model_1);
        //this.xianShiFormula(g2d, model_1, FORMULA_LAYOUT_HEIGHT);
        g2d.setColor(this.readColor(model_2));
        this.QuXian_Third(g2d, points_2,model_2);
        //this.xianShiFormula(g2d, model_2, FORMULA_LAYOUT_HEIGHT - FORMULA_LAYOUT_INTERVAL);
        g2d.setColor(this.readColor(model_3));
        this.QuXian_Third(g2d, points_3,model_3);
        //this.xianShiFormula(g2d, model_3, FORMULA_LAYOUT_HEIGHT - 2 * FORMULA_LAYOUT_INTERVAL);
    }

    public Color readColor(CalculateModel model) {
        if (model == null)
            return new Color(100, 100, 100);
        if (model.color == 0)
            return new Color(200, 0, 0);
        else if (model.color == 1)
            return new Color(0, 160, 0);
        else
            return new Color(0, 0, 250);
    }

    /**
     * 坐标轴应当根据当前的自变量，因变量进行相关的调整
     * 希望能够对于输入的多条曲线进行比较，选出其中最合适的一条
     * 以此设计最合适的坐标轴
     */
    public void Zhou(Graphics2D g2d, TransformModel model) {
        int x1 = (int) (TransformModel.ORI_POINT_X - CalculateModel.BLANK - xl * TransformModel.MAX_WINDOW_WIDTH / 2);
        int x2 = (int) (TransformModel.ORI_POINT_X + CalculateModel.BLANK + xr * TransformModel.MAX_WINDOW_WIDTH / 2);
        int y = TransformModel.ORI_POINT_Y;
        int vx = TransformModel.ORI_POINT_X;
        int vy1 = (int) (TransformModel.ORI_POINT_Y - CalculateModel.BLANK - yd * TransformModel.MAX_WINDOW_HEIGHT / 2);
        int vy2 = (int) (TransformModel.ORI_POINT_Y + CalculateModel.BLANK + yu * TransformModel.MAX_WINDOW_HEIGHT / 2);
        g2d.drawLine(x1, y, x2, y);
        g2d.drawLine(vx, vy1, vx, vy2);
        if (model!=null) this.ShuZhi(g2d, model);
        this.drawEnds(x2, y, RIGHT, g2d, model);
        this.drawEnds(vx, vy1, UP, g2d, model);
    }

    /**
     * 依据当前的坐标轴来拉伸数值的范围
     */
    public void ShuZhi(Graphics2D g2d, TransformModel model) {
        ArrayList<PointMap.Point> points = new ArrayList<>();
        double curRatio = DOT_INTERVAL/model.stretchRatio;
        for (int i = 0;i<=(TransformModel.MAX_WINDOW_WIDTH/2+(int)(CalculateModel.BLANK))/DOT_INTERVAL;i++) {
            points.add(new PointMap.Point(TransformModel.ORI_POINT_X+i*DOT_INTERVAL,TransformModel.ORI_POINT_Y,i*curRatio));
            points.add(new PointMap.Point(TransformModel.ORI_POINT_X-i*DOT_INTERVAL,TransformModel.ORI_POINT_Y,-i*curRatio));
        }
        for (int i = 0;i<=(TransformModel.MAX_WINDOW_HEIGHT/2+(int)(CalculateModel.BLANK))/DOT_INTERVAL;i++) {
            points.add(new PointMap.Point(TransformModel.ORI_POINT_X,TransformModel.ORI_POINT_Y+i*DOT_INTERVAL,-i*curRatio));
            points.add(new PointMap.Point(TransformModel.ORI_POINT_X,TransformModel.ORI_POINT_Y-i*DOT_INTERVAL,i*curRatio));
        }
        for (PointMap.Point p : points) {
            if (showShuZhi)
                g2d.drawString(fmt.format(p.wuCha), (int) p.x + 3, (int) p.y - 1);
            g2d.fillRect((int) p.x, (int) p.y, 3, 3);
        }
    }

    public void xianShiFormula(Graphics2D g2d, CalculateModel model, int height) {
        if (model == null)
            return;
        String out = model.input+"=0";
        g2d.drawString(out, 20, height);
    }

    public void drawEnds(int x, int y, int dir, Graphics2D g2d, TransformModel model) {
        PointMap.Point p1, p2, p3;
        if (dir == UP) {
            p1 = new PointMap.Point(x, y - 7);
            p2 = new PointMap.Point(x - 4, y);
            p3 = new PointMap.Point(x + 4, y);
        } else if (dir == DOWN) {
            p1 = new PointMap.Point(x, y + 7);
            p2 = new PointMap.Point(x - 4, y);
            p3 = new PointMap.Point(x + 4, y);
        } else if (dir == RIGHT) {
            p1 = new PointMap.Point(x + 7, y);
            p2 = new PointMap.Point(x, y + 4);
            p3 = new PointMap.Point(x, y - 4);
        } else {
            p1 = new PointMap.Point(x - 7, y);
            p2 = new PointMap.Point(x, y + 4);
            p3 = new PointMap.Point(x, y - 4);
        }
        g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        g2d.drawLine((int) p3.x, (int) p3.y, (int) p2.x, (int) p2.y);
        g2d.drawLine((int) p1.x, (int) p1.y, (int) p3.x, (int) p3.y);
    }

    public void QuXian_Third(Graphics2D g2d, PointMap.Point[] points,CalculateModel model) {
        if (model==null)
            return;
        if (!model.isFunction) {
            for (PointMap.Point point : points) {
                int x = (int)point.x;
                int y = (int)point.y;
                g2d.fillRect(x,y,THICKNESS_LINE,THICKNESS_LINE);
            }
        } else {
            for (int i = 1;i<points.length;i++) {
                int x1 = (int) points[i-1].x;
                int y1 = (int) points[i-1].y;
                int x2 = (int) points[i].x;
                int y2 = (int) points[i].y;
                g2d.setStroke(new BasicStroke(THICKNESS_LINE));
                if (Math.abs(y2-y1)<Math.abs(T_model.range_P[0]-T_model.range_P[1]))
                    g2d.drawLine(x1,y1,x2,y2);
            }
        }
    }

    /**
     * 利用ImageIO将图片保存到本地的方法
     * 来自 C S D N
     */
    BufferedImage image;
    Graphics graphics;

    public void save() {
        image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        graphics = image.getGraphics();

        paint(graphics);
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter =
                new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
        chooser.setFileFilter(filter);
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File imageFile = chooser.getSelectedFile();
            try {
                ImageIO.write(image, "JPG", imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
