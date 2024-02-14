public class Planet{
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    static double G=6.67e-11;

    public Planet(double xxP,double yyP,double xxV,double yyV,double mas,String s){
        xxPos=xxP;
        yyPos=yyP;
        xxVel=xxV;
        yyVel=yyV;
        mass=mas;
        imgFileName=s;
    }

    public Planet(Planet b){
        b.xxPos=xxPos;
        b.yyPos=yyPos;
        b.xxVel=xxVel;
        b.yyVel=yyVel;
        b.mass=mass;
        b.imgFileName=imgFileName;
    }

    public double calcDistance(Planet b){
        if(b==this){
            return 0.0;
        }
        double dx=b.xxPos-this.xxPos;
        double dy=b.yyPos-this.yyPos;
        return (double)Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
    }

    public double calcForceExertedBy(Planet b){
        return(G*b.mass*this.mass/calcDistance(b)/calcDistance(b));
    }

    public double calcForceExertedByX(Planet b){
        return (double)calcForceExertedBy(b)*(b.xxPos-this.xxPos)/calcDistance(b);
    }

    public double calcForceExertedByY(Planet b){
        return (double)calcForceExertedBy(b)*(b.yyPos-this.yyPos)/calcDistance(b);
    }

    public double calcNetForceExertedByX(Planet[] b){
        double sumx=0;
        for(Planet planet:b){
            if(planet!=this)
            {
                sumx+=calcForceExertedByX(planet);
            }
        }
        return sumx;
    }

    public double calcNetForceExertedByY(Planet[] b){
        double sumy=0;
        for(Planet planet:b){
            if(planet!=this)
            {
                sumy+=calcForceExertedByY(planet);
            }
        }
        return sumy;
    }

    public void update(double dt,double fX,double fY){
        double acc_x=fX/this.mass;
        double acc_y=fY/this.mass;
        this.xxVel+=acc_x*dt;
        this.yyVel+=acc_y*dt;
        this.xxPos+=this.xxVel*dt;
        this.yyPos+=this.yyVel*dt;
    }

    public void draw(){
        StdDraw.picture(xxPos,yyPos,"images/"+imgFileName);
    }
}
