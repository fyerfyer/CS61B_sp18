public class NBody {
    public static double readRadius(String file){
        In in=new In(file);
        int firstItemInFile = in.readInt();
		double secondItemInFile = in.readDouble();
        return secondItemInFile;
    }

    public static Planet[] readPlanets(String file) {
        In in=new In(file);
        int firstItemInFile = in.readInt();
		double secondItemInFile = in.readDouble();
        Planet[] planets=new Planet[firstItemInFile];

        for(int i=0;i<firstItemInFile;i+=1){
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String imgFileName = in.readString();

            planets[i] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
        }

        return planets;
    }

    public static void main(String[] args) {
        double T=Double.parseDouble(args[0]);
        double dt=Double.parseDouble(args[1]);
        String filename=args[2];
        double universe_radius=readRadius(filename);
        Planet[] bodies=readPlanets(filename);

        StdDraw.enableDoubleBuffering();

        StdDraw.setScale(-universe_radius,universe_radius);
        StdDraw.picture(0,0,"images/starfield.jpg");

        for(Planet planet:bodies){
            planet.draw();
        }

        double time=0.0;
        int n=bodies.length;

        while(time<=T){
            double[] xForces=new double[n];
            double[] yForces=new double[n];

            for(int i=0;i<n;i+=1){
                xForces[i]=bodies[i].calcNetForceExertedByX(bodies);
                yForces[i]=bodies[i].calcNetForceExertedByY(bodies);
            }

            for(int i=0;i<n;i+=1){
                bodies[i].update(dt,xForces[i],yForces[i]);
            }

            StdDraw.clear();
            StdDraw.picture(0,0,"images/starfield.jpg");

            for(Planet planet:bodies){
                planet.draw();
            }

            StdDraw.show();
            StdDraw.pause(10);
            time+=dt;
        }

        StdOut.printf("%d\n",bodies.length);
        StdOut.printf("%.2e\n",universe_radius);
        for (int i=0;i<n;i++){
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
            bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
            bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);   
        }
    }    
}