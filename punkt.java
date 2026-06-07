public class punkt {
    protected double x,y;
    punkt(double x,double y){
        this.x=x;
        this.y=y;
    }
    punkt(double x){
        this.x=x;
        y=x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
