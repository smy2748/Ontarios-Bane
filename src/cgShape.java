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

/**
 * A class that can draw a cube, cylinder, sphere, or cone
 *
 * Largely implemented by: Stephen Yingling
 */
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
     * Implemented by Stephen Yingling
     */
    public void makeCube (int subdivisions)
    {
        if( subdivisions < 1 )
            subdivisions = 1;

        //Make all the points at the corners of the cube
        MyPoint frontLowerLeft = new MyPoint(-.5f,-.5f,-.5f),
                frontLowerRight = new MyPoint(.5f,-.5f,-.5f),
                frontUpperRight = new MyPoint(.5f,.5f,-.5f),
                frontUpperLeft = new MyPoint(-.5f,.5f,-.5f),
                backLowerLeft = new MyPoint(-.5f,-.5f,.5f),
                backUpperLeft = new MyPoint(-.5f,.5f,.5f),
                backLowerRight = new MyPoint(.5f,-.5f,.5f),
                backUpperRight = new MyPoint(.5f,.5f,.5f)
                ;

        //Draw each face
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

    /**
     * Makes a quad from the given points with the given subdivisions
     * @param ll - The lower left corner of the quad
     * @param lr - The lower right corner of the quad
     * @param ur - The upper right corner of the quad
     * @param ul - The upper left corner of the quad
     * @param subs - The number of subdivisions for the quad
     * Implemented by: Stephen Yingling
     */
    public void makeQuad(MyPoint ll, MyPoint lr, MyPoint ur, MyPoint ul, int subs ){
        float subsize = 1.0f/subs;

        //Subdivide the quad into columns and draw the columns
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

    /**
     * Draws a column of quads
     * @param q - The left upper point of the column
     * @param r - The left lower point of the column
     * @param qp - The right upper point of the column
     * @param rp - The right lower point of the column
     * @param numSubs - The number of rows for this column
     * Implemented by: Stephen Yingling
     */
    public void makeQuadCol(MyPoint q, MyPoint r, MyPoint qp, MyPoint rp, int numSubs){
        float sublength = 1f/numSubs;

        //Calculate and draw two triangles for each row
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

    /**
     * A callthrough to the other addTriangle function
     * @param p1 - The first point of the triangle
     * @param p2 - The second point of the triangle
     * @param p3 - The third point of the triangle
     * Implemented by: Stephen Yingling
     */
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
     * Implemented by Stephen Yingling
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

            //Make triangles for the disks
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

            //Make the rectangle on the side
            makeQuadCol(p1f,p1b,p2f,p2b,heightDivisions);
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
     * Implemented by Stephen Yingling
     */
    public void makeCone (float radius, int radialDivisions, int heightDivisions)
    {
        if( radialDivisions < 3 )
            radialDivisions = 3;

        if( heightDivisions < 1 )
            heightDivisions = 1;

        MyPoint origin = new MyPoint(0,0,-.5f);

        MyPoint p1f, p2f, p1b, p2b;

        float rads = (float)Math.toRadians(360f/radialDivisions);

        //Make the disk
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

            //Make a "quad" with the far points of the quad the same
            //Thus making a conical shape
            makeQuadCol(p1f,origin,p2f,origin,heightDivisions);
        }
    }

    /**
     * makeSphere - Makes a sphere using the recursive subdivision method
     *
     * @param radius - Radius of the sphere
     * @param slices - number of recursions
     * @param stacks - Not used
     *
     * Can only use calls to addTriangle
     *
     * Implemented by Stephen Yingling
     */
    public void makeSphere (float radius, int slices, int stacks)
    {
        if( slices < 3 )
            slices = 3;

        if (slices > 5){
            slices = 5;
        }

        if( stacks < 3 )
            stacks = 3;

        //Make the isocohedron net
        float a = (float)(2/(1+Math.sqrt(5))),
              na = -1*a,
              p = 1, np = -1*p;

        MyPoint v0 = new MyPoint(0,a,np),
                v1 = new MyPoint(na,p,0),
                v2 = new MyPoint(a,p,0),
                v3 = new MyPoint(0,a,p),
                v4 = new MyPoint(np,0,a),
                v5 = new MyPoint(0,na,p),
                v6 = new MyPoint(p,0,a),
                v7 = new MyPoint(p,0,na),
                v8 = new MyPoint(0,na,np),
                v9 = new MyPoint(np,0,na),
                v10 = new MyPoint(na,np,0),
                v11 = new MyPoint(a,np,0);

        //Make recursive triangles
        recurTriEngels(slices,v0,v1,v2,radius);
        recurTriEngels(slices,v3,v2,v1,radius);
        recurTriEngels(slices,v3,v4,v5,radius);
        recurTriEngels(slices,v3,v5,v6,radius);

        recurTriEngels(slices,v0,v7,v8,radius);
        recurTriEngels(slices,v0,v8,v9,radius);
        recurTriEngels(slices,v5,v10,v11,radius);
        recurTriEngels(slices,v8,v11,v10,radius);

        recurTriEngels(slices,v1,v9,v4,radius);
        recurTriEngels(slices,v10,v4,v9,radius);
        recurTriEngels(slices,v2,v6,v7,radius);
        recurTriEngels(slices,v11,v7,v6,radius);

        recurTriEngels(slices,v3,v1,v4,radius);
        recurTriEngels(slices,v3,v6,v2,radius);
        recurTriEngels(slices,v0,v9,v1,radius);
        recurTriEngels(slices,v0,v2,v7,radius);

        recurTriEngels(slices,v8,v10,v9,radius);
        recurTriEngels(slices,v8,v7,v11,radius);
        recurTriEngels(slices,v5,v4,v10,radius);
        recurTriEngels(slices,v5,v11,v6,radius);


    }

    /**
     * Recursively add triangles to the given triangle.
     *
     * @param subs - The number of subdivions
     * @param p0 - The 1st point of the triangle
     * @param p1 - The 2nd point of the triangle
     * @param p2 - - The 3rd point of the triangle
     * @param rad - The radius of the sphere
     *
     * Implemented by Stephen Yingling
     *
     * If there were three Engels, then Marx would have made triple the money!
     */
    public void recurTriEngels(int subs, MyPoint p0, MyPoint p1 , MyPoint p2, float rad){

        //Draw at the base case
        if(subs == 1){
            addTriangle(p0.mult(rad/p0.getMagnitude()),
                    p1.mult(rad/p1.getMagnitude()),
                    p2.mult(rad/p2.getMagnitude()));
            return;
        }
        recurTriEngels(subs-1, p0,p0.midPoint(p1),p0.midPoint(p2),rad);
        recurTriEngels(subs-1, p0.midPoint(p1),p1,p1.midPoint(p2),rad);
        recurTriEngels(subs-1, p0.midPoint(p2),p1.midPoint(p2),p2,rad);
        recurTriEngels(subs-1, p0.midPoint(p1),p1.midPoint(p2),p2.midPoint(p0),rad);
    }


    /**
     * A class to represent a point in 3D space
     * Created by: Stephen Yingling
     */
    public class MyPoint{
        protected float x;
        protected float y;
        protected float z;

        /**
         * Make a point
         * @param x - The x value of the point
         * @param y - The y value of the point
         * @param z - The z value of the point
         * Implemented by: Stephen Yingling
         */
        public MyPoint(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Adds the value of one point to this point and returns a new point
         * containg the result
         * @param other - The point to add to this one
         * @return A point containing the point addition of this point and the other one
         * Implemented by: Stephen Yingling
         */
        public MyPoint add(MyPoint other){

            return new MyPoint(this.x + other.getX(), this.y + other.getY(), this.z + other.getZ());
        }

        /**
         * Multiply this point by a scalar value
         * @param f - The number to multiply the point by
         * @return A new point with the new values
         *
         * Implemented by: Stephen Yingling
         */
        public MyPoint mult(float  f){
            return new MyPoint(x * f, y*f, z*f);
        }

        /**
         * Determine the midpoint between this point and another
         * @param other The other point
         * @return A new point representing the midpoint
         * Implemented by: Stephen Yingling
         */
        public MyPoint midPoint(MyPoint other){
            float nx = (other.getX()+x)/2f,
                  ny = (other.getY()+y)/2f,
                  nz = (other.getZ()+z)/2f;

            return new MyPoint(nx,ny,nz);
        }

        /**
         * Gets the magnitude of this point
         * @return The magnitude of this point
         * Implemented by: Stephen Yingling
         */
        public float getMagnitude(){
            return (float)Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2));
        }

        /**
         *
         * @return The x value
         * Implemented by: Stephen Yingling
         */
        public float getX() {
            return x;
        }

        /**
         *
         * @return The y value
         * Implemented by: Stephen Yingling
         */
        public float getY() {
            return y;
        }


        /**
         *
         * @return The z value
         * Implemented by: Stephen Yingling
         */
        public float getZ() {
            return z;
        }

        /**
         * Sets the z value to the specified value
         * @param z - The new z value
         * Implemented by: Stephen Yingling
         */
        public void setZ(float z) {
            this.z = z;
        }
    }

}
