// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   CompareNode.java

package ncsa.evolutionhighway.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class CompareNode
{
    public static class Leaf extends ncsa.evolutionhighway.util.CompareNode
    {

        private java.util.List list;

        public java.util.List eval()
        {
            return list;
        }

        public Leaf(java.util.List list)
        {
            this.list = list;
        }
    }

    public static class Branch extends ncsa.evolutionhighway.util.CompareNode
    {

        private ncsa.evolutionhighway.util.CompareNode left;
        private ncsa.evolutionhighway.util.CompareNode right;

        public java.util.List eval()
        {
            java.util.List leftList = left.eval();
            java.util.List rightList = right.eval();
            java.util.ArrayList matches = new ArrayList();
            for(java.util.Iterator leftIter = leftList.iterator(); leftIter.hasNext();)
            {
                double leftD[] = (double[])(double[])leftIter.next();
                java.util.Iterator rightIter = rightList.iterator();
                while(rightIter.hasNext()) 
                {
                    double rightD[] = (double[])(double[])rightIter.next();
                    if(leftD[0] <= rightD[0] && leftD[1] >= rightD[1])
                        matches.add(new double[] {
                            rightD[0], rightD[1]
                        });
                    else
                    if(rightD[0] <= leftD[0] && rightD[1] >= leftD[1])
                        matches.add(new double[] {
                            leftD[0], leftD[1]
                        });
                    else
                    if(leftD[0] < rightD[0] && leftD[1] > rightD[0] && leftD[1] < rightD[1])
                        matches.add(new double[] {
                            rightD[0], leftD[1]
                        });
                    else
                    if(leftD[0] > rightD[0] && leftD[0] < rightD[1] && leftD[1] > rightD[1])
                        matches.add(new double[] {
                            leftD[0], rightD[1]
                        });
                }
            }

            return matches;
        }

        public ncsa.evolutionhighway.util.CompareNode getLeft()
        {
            return left;
        }

        public ncsa.evolutionhighway.util.CompareNode getRight()
        {
            return right;
        }

        public void setLeft(ncsa.evolutionhighway.util.CompareNode c)
        {
            left = c;
        }

        public void setRight(ncsa.evolutionhighway.util.CompareNode c)
        {
            right = c;
        }

        public Branch()
        {
        }
    }


    private ncsa.evolutionhighway.util.CompareNode parent;

    public CompareNode()
    {
    }

    public abstract java.util.List eval();

    public ncsa.evolutionhighway.util.CompareNode getParent()
    {
        return parent;
    }

    public void setParent(ncsa.evolutionhighway.util.CompareNode c)
    {
        parent = c;
    }
}
