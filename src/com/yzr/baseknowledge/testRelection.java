package com.yzr.baseknowledge;

public class testRelection {
    public String demo1;
    private int x;
    public Integer y;
    private Object obj;

    public testRelection() {
    }

    public testRelection(String demo1, int x, Integer y) {
        this.demo1 = demo1;
        this.x = x;
        this.y = y;
    }

    public void test1() {
        Integer out = x + y;
        System.out.println("Output:" + out);
    }

    // @Override
    // public String toString() {
    // return "testRelection{" +
    // "demo1='" + demo1 + '\'' +
    // ", x=" + x +
    // ", y=" + y +
    // '}';
    // }
}
