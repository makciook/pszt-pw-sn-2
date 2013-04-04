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
    private final int sizeX = 80;
    private final int sizeY = 80;
    private final int scale = 5;
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
            public void mousePressed(MouseEvent e) {
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
                siec.learn( (double)x/sizeX, (double)y/sizeY, wynik);
            }
        });

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() != KeyEvent.VK_SPACE)
                    return;
                krzywa = new short[sizeX][sizeY];
                double wyniki[] = new double[2];
                double poprzedni = 0;
                double aktualny = 0;
                boolean bylo = false;
                for(int i = 0; i < sizeX; ++i) {
                    for(int j = 0; j < sizeY; ++j) {
                        wyniki = siec.calcResults((double)i/sizeX,(double)j/sizeY);
                       aktualny = wyniki[0] - wyniki[1];
                       
                    //    System.out.println(wyniki[0]  + " "+  wyniki[1] + " i" +i + " j" +j );
                    
                       if(wyniki[0] > wyniki[1]) {
                         krzywa[i][j] = 1;
                       }
                       else {
                        krzywa[i][j] = 2;
                       }
                        
                        /*
                        if(!bylo && j > 0) {
                            if((aktualny<0)==(poprzedni<0))
                                krzywa[i][j] = 0;
                            else {
                                krzywa[i][j] = 1;
                                bylo = true;
                            }
                        }
                        
                        poprzedni = aktualny;
                    }
                    poprzedni = 0;
                    aktualny = 0;
                    bylo = false;
                    */
                  }
                }
                
                       
                       
                rysuj_krzywa = true;
                repaint();
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
                    g.setColor(Color.red);
                    //g.fillRect(i*scale,j*scale,scale,scale);
                    g.fillArc(i*scale,j*scale,scale,scale,0,360);
                }
                else {
                    g.setColor(Color.blue);
                    //g.fillRect(i*scale,j*scale,scale,scale);
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
            rysuj_krzywa = false;
        }

    }
}
