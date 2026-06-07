import javax.swing.*;
import java.awt.*;

static class BezierExample2 extends JPanel{
public static punkt[][] punkty2;
     BezierExample2(punkt[][] punkty2) {
         this.punkty2 = punkty2;
     }

    public void rysujKrzywa(Graphics2D g2, punkt[] punkty) {

        punkt prev = krzywej_Béziera_n_stopnia(0, punkty);

        for (double t = 0; t <= 1; t += 0.01) {

            punkt p = krzywej_Béziera_n_stopnia(t, punkty);

            g2.drawLine(
                    (int) prev.getX(),
                    (int) prev.getY(),
                    (int) p.getX(),
                    (int) p.getY()
            );

            prev = p;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (punkt[] k : punkty2) {
            rysujKrzywa(g2, k);
        }
    }




    }
    public static double silnia(int a){
        double silnia=1;
        for (int i=a;i>0;i--)
            silnia*=i;
        return silnia;
    }
    public static double zure_Niutona(int n ,int i){
        double nytone=0;
        nytone=silnia(n)/(silnia(i)*silnia(n-i));
        return nytone;
    }

    public static punkt krzywej_Béziera_n_stopnia(double t ,punkt[] punktyk){
        double krzBezi_x =0,krzBezi_y =0;
        int n=punktyk.length-1;

        double bernsteina;

        for (int i=0;i<=n;i++){
            bernsteina =zure_Niutona(n,i)
                    *(Math.pow((1.0-t),n-i))
                    *(Math.pow(t,i));
            krzBezi_x+=bernsteina*punktyk[i].getX();
            krzBezi_y+=bernsteina*punktyk[i].getY();
        }
        System.out.println("x="+krzBezi_x +" y="+krzBezi_y);
        return new punkt(krzBezi_x,krzBezi_y);
    }

    public static void main(String[] args) {
        punkt[][] krzywe = {

                // F pion
                {
                        new punkt(50,50),
                        new punkt(50,150),
                        new punkt(50,250),
                        new punkt(50,350)
                },

                // F górna kreska
                {
                        new punkt(50,50),
                        new punkt(100,50),
                        new punkt(150,50),
                        new punkt(200,50)
                },

                // F środkowa kreska
                {
                        new punkt(50,150),
                        new punkt(100,150),
                        new punkt(140,150),
                        new punkt(180,150)
                },

                // M lewy bok
                {
                        new punkt(250,350),
                        new punkt(250,250),
                        new punkt(250,150),
                        new punkt(250,50)
                },

                // M środek
                {
                        new punkt(250,50),
                        new punkt(300,150),
                        new punkt(350,150),
                        new punkt(400,50)
                },

                // M prawy bok
                {
                        new punkt(400,50),
                        new punkt(400,150),
                        new punkt(400,250),
                        new punkt(400,350)
                }
        };
        JFrame frame = new JFrame("Krzywa Beziera");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.add(new BezierExample2(krzywe));
        frame.setVisible(true);


    }

