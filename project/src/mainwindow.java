import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class mainwindow extends JFrame {
    private short punkty[][];
    private final int sizeX = 40;
    private final int sizeY = 40;
    private final int scale = 10;
    private final NeuronNetwork siec;

    public mainwindow(final NeuronNetwork siec) {
        super("mainwindow");
        this.setPreferredSize(new Dimension(sizeX*scale, sizeY*scale));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        this.siec = siec;

        punkty = new short[sizeX][sizeY];
        for(int i = 0; i < sizeX; ++i)
            for(int j = 0; j < sizeY; ++j)
                punkty[i][j] = 0;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX()/scale;
                int y = e.getY()/scale;
                double wynik[] = new double[2];
                if(punkty[x][y] != 0)
                    return;
                if(e.getButton() == MouseEvent.BUTTON1) {
                    punkty[x][y] = 1;
                    wynik[0] = 0;
                    wynik[1] = 1;
                }
                else {
                    punkty[x][y] = 2;
                    wynik[0] = 1;
                    wynik[1] = 0;
                }

                repaint();
                siec.learn(x/40.0,y/40.0,wynik);
            }
        });

        this.setVisible(true);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for(int i = 0; i < sizeX; ++i) {
            for(int j = 0; j < sizeY; ++j) {
                if(punkty[i][j] == 0)
                    continue;
                if(punkty[i][j] == 1) {
                    g.setColor(Color.cyan);
                    g.fillRect(i*scale,j*scale,scale,scale);
                }
                else {
                    g.setColor(Color.magenta);
                    g.fillRect(i*scale,j*scale,scale,scale);
                }
            }
        }

    }
}
