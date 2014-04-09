/**
 * cgShape.java
 *
 * Class that includes routines for tessellating a number of basic shapes
 *
 * Students are to supply their implementations for the
 * functions in this file using the function "addTriangle()" to do the 
 * tessellation.
 *
 */

import java.awt.*;
import java.nio.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import java.io.*;


public class cgShape extends simpleShape
{
    /**
     * constructor
     */
    public cgShape()
    {
    }

    /**
     * makeCube - Create a unit cube, centered at the origin, with a given number
     * of subdivisions in each direction on each face.
     *
     * @param subdivisions - number of equal subdivisons to be made in each
     *        direction along each face
     *
     * Can only use calls to addTriangle()
     */
    public void makeCube (int subdivisions)
    {
        if( subdivisions < 1 )
            subdivisions = 1;

        MyPoint frontLowerLeft = new MyPoint(-.5f,-.5f,-.5f),
                frontLowerRight = new MyPoint(.5f,-.5f,-.5f),
                frontUpperRight = new MyPoint(.5f,.5f,-.5f),
                frontUpperLeft = new MyPoint(-.5f,.5f,-.5f),
                backLowerLeft = new MyPoint(-.5f,-.5f,.5f),
                backUpperLeft = new MyPoint(-.5f,.5f,.5f),
                backLowerRight = new MyPoint(.5f,-.5f,.5f),
                backUpperRight = new MyPoint(.5f,.5f,.5f)
                ;
        makeQuad(frontLowerLeft,frontLowerRight,frontUpperRight,
                frontUpperLeft, subdivisions);

        makeQuad(backLowerLeft,frontLowerLeft,frontUpperLeft,
                backUpperLeft, subdivisions);

        makeQuad(backLowerRight, backLowerLeft, backUpperLeft,
                backUpperRight, subdivisions);

        makeQuad(frontUpperLeft, frontUpperRight, backUpperRight,
                backUpperLeft, subdivisions);

        makeQuad(backLowerLeft, backLowerRight, frontLowerRight,
                frontLowerLeft, subdivisions);

        makeQuad(frontLowerRight, backLowerRight, backUpperRight,
                frontUpperRight, subdivisions);

    }

    public void makeQuad(MyPoint ll, MyPoint lr, MyPoint ur, MyPoint ul, int subs ){
        float subsize = 1.0f/subs;

        for(int i=0; i<subs; i++){
            float curPos = subsize * i;
            MyPoint q = ul.mult(1f-curPos).add(ur.mult(curPos));
            MyPoint r = ll.mult(1f-curPos).add(lr.mult(curPos));

            float iprime = curPos + subsize;
            MyPoint qPrime = ul.mult(1f-iprime).add(ur.mult(iprime));
            MyPoint rprime = ll.mult(1f-iprime).add(lr.mult(iprime));

            makeQuadCol(q,r,qPrime,rprime,subs);
        }
    }


    public void makeQuadCol(MyPoint q, MyPoint r, MyPoint qp, MyPoint rp, int numSubs){
        float sublength = 1f/numSubs;
        for(int i=0; i < numSubs; i++){
            float f = i * sublength;
            MyPoint p1 = q.mult(1-f).add(r.mult(f)),
                    p2= qp.mult(1-f).add(rp.mult(f)),
                    p3 = q.mult(1-(f+sublength)).add(r.mult(f+sublength)),
                    p4 = qp.mult(1-(f+sublength)).add(rp.mult(f+sublength));

            addTriangle(p3,p4,p2);
            addTriangle(p3,p2,p1);
        }
    }

    public void addTriangle(MyPoint p1, MyPoint p2, MyPoint p3){
        addTriangle(p1.getX(), p1.getY(), p1.getZ(),
                p2.getX(), p2.getY(), p2.getZ(),
                p3.getX(), p3.getY(), p3.getZ());
    }



    /**
     * makeCylinder - Create polygons for a cylinder with unit height, centered at
     * the origin, with separate number of radial subdivisions and height 
     * subdivisions.
     *
     * @param radius - Radius of the base of the cylinder
     * @param radialDivisions - number of subdivisions on the radial base
     * @param heightDivisions - number of subdivisions along the height
     *
     * Can only use calls to addTriangle()
     */
    public void makeCylinder (float radius, int radialDivisions, int heightDivisions)
    {
        if( radialDivisions < 3 )
            radialDivisions = 3;

        if( heightDivisions < 1 )
            heightDivisions = 1;

        MyPoint origin = new MyPoint(0,0,-.5f);

        MyPoint p1f, p2f, p1b, p2b;

        float rads = (float)Math.toRadians(360f/radialDivisions);

        for(int i=0; i < radialDivisions; i++){
            float curDegs = i * rads;
            p2f = new MyPoint(radius* (float)Math.cos(curDegs),
                             radius * (float) Math.sin(curDegs),
                             -.5f);
            p1f = new MyPoint(radius* (float)Math.cos(curDegs+rads),
                    radius * (float) Math.sin(curDegs + rads),
                    -.5f);

            origin.setZ(-.5f);
            addTriangle(origin, p1f, p2f);

            origin.setZ(.5f);

            p1b = new MyPoint(p1f.getX(), p1f.getY(),.5f);
            p2b = new MyPoint(p2f.getX(), p2f.getY(),.5f);


            addTriangle(origin,p2b,p1b);

            makeQuad(p1f,p1b,p2b,p2f,heightDivisions);
        }
    }

    /**
     * makeCone - Create polygons for a cone with unit height, centered at the
     * origin, with separate number of radial subdivisions and height 
     * subdivisions.
     *
     * @param radius - Radius of the base of the cone
     * @param radialDivisions - number of subdivisions on the radial base
     * @param heightDivisions - number of subdivisions along the height
     *
     * Can only use calls to addTriangle()
     */
    public void makeCone (float radius, int radialDivisions, int heightDivisions)
    {
        if( radialDivisions < 3 )
            radialDivisions = 3;

        if( heightDivisions < 1 )
            heightDivisions = 1;

        // YOUR IMPLEMENTATION HERE
    }

    /**
     * makeSphere - Create sphere of a given radius, centered at the origin, 
     * using spherical coordinates with separate number of thetha and 
     * phi subdivisions.
     *
     * @param radius - Radius of the sphere
     * @param slices - number of subdivisions in the theta direction
     * @param stacks - Number of subdivisions in the phi direction.
     *
     * Can only use calls to addTriangle
     */
    public void makeSphere (float radius, int slices, int stacks)
    {
        if( slices < 3 )
            slices = 3;

        if( stacks < 3 )
            stacks = 3;

        // YOUR IMPLEMENTATION HERE
    }

    public class MyPoint{
        protected float x;
        protected float y;
        protected float z;

        public MyPoint(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public MyPoint add(MyPoint other){

            return new MyPoint(this.x + other.getX(), this.y + other.getY(), this.z + other.getZ());
        }

        public MyPoint mult(float  f){
            return new MyPoint(x * f, y*f, z*f);
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getZ() {
            return z;
        }

        public void setZ(float z) {
            this.z = z;
        }
    }

}
