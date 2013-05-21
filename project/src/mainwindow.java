import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;


public class mainwindow extends JFrame {
    private short punkty[][];
    private short krzywa[][];
    private boolean rysuj_krzywa;
    private final int sizeX = 80;                       // przeskalowana szerokość okna
    private final int sizeY = 80;                       // przeskalowana wysokość okna
    private final int scale = 5;                        // współczynnik skalowania
    private final NeuronNetwork siec;

    public mainwindow(final NeuronNetwork siec) {
        super("Klasyfikator PSZT 2013");
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
            public void mousePressed(MouseEvent e) {
                if(rysuj_krzywa) {
                    rysuj_krzywa = false;
                    repaint();
                    return;
                }
                /**
                 * Wartości powinny być od -1 do 1. Za duże sumy iloczynów dla funkcji aktywacji.
                 * f: R^2 -> [-1,1];
                 */
                int x = e.getX()/scale;
                int y = e.getY()/scale;
                double wynik[] = new double[2];
                if(punkty[x][y] != 0)
                    return;
                if(e.getButton() == MouseEvent.BUTTON1) {
                    punkty[x][y] = 1;
                    wynik[0] = 0.1;//0;
                    wynik[1] = 0.9;//1;
                    siec.addPoint(1, (double)x/sizeX, (double)y/sizeY);
                }
                else {
                    punkty[x][y] = 2;
                    wynik[0] = 0.9;//1;
                    wynik[1] = 0.1;//0;
                    siec.addPoint(2, (double)x/sizeX, (double)y/sizeY);
                }

                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_A) {
                    siec.recalcBase();
                    System.out.println("Przeliczylem!");
                }

                else if(e.getKeyCode() != KeyEvent.VK_SPACE || ( e.getKeyCode() == KeyEvent.VK_SPACE && rysuj_krzywa == true) )
                {
                    rysuj_krzywa = false;
                    repaint();
                    return;
                }


                krzywa = new short[sizeX][sizeY];
                double wyniki[] = new double[2];

                for(int i = 0; i < sizeX; ++i) {
                    for(int j = 0; j < sizeY; ++j) {
                        wyniki = siec.calcResults((double)i/sizeX,(double)j/sizeY);
                        if(wyniki[0] > wyniki[1]) {
                            krzywa[i][j] = 1;
                        }
                            else {
                            krzywa[i][j] = 2;
                        }
                        

                  }
                  //System.out.println();
                }

                rysuj_krzywa = true;
                repaint();
                //rysuj_krzywa = false;
            }
        });

        this.setVisible(true);

    }

    public void closeWindow() {
        this.setVisible(false);
        this.dispose();

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for(int i = 0; i < sizeX; ++i) {
            for(int j = 0; j < sizeY; ++j) {
                if(punkty[i][j] == 0)
                    continue;
                if(punkty[i][j] == 1) {
                    g.setColor(Color.red);
                    g.fillArc(i*scale,j*scale,scale,scale,0,360);
                }
                else {
                    g.setColor(Color.blue);
                    g.fillArc(i*scale,j*scale,scale,scale,0,360);
                }
            }
        }
        if(rysuj_krzywa) {    
            for(int i = 0; i < sizeX; ++i) {
                for(int j = 0; j < sizeY; ++j) {
                    if(krzywa[i][j] == 1) {
                        g.setColor(Color.blue);
                        g.fillRect(i*scale, j*scale, scale, scale); 
                    }
                    else {
                        g.setColor(Color.red);
                        g.fillRect(i*scale, j*scale, scale, scale); 
                    }
                }
            }
            //rysuj_krzywa = false;
        }
    }

    int getSizeX() {  return sizeX; }
    int getSizeY() { return sizeY; }
    int getScale() { return scale; }
    Rectangle getCurrentLocation() {
        return this.getBounds();
    }

    public void setLoc(int x,int y) {
        this.setLocation(x,y);
    }
}
