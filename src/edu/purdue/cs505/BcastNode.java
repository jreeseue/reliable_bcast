package edu.purdue.cs505;

import java.io.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.net.*;

public class BcastNode {
    private static final int MAX_PROCS = 3;
    private static final int NUM_MSGS = 100000;
    private static ArrayList<Process> processes = new ArrayList<Process>();
    
    public static void main(String args[]) {
        if (args[0].equals("fifo")) doFIFO();
        else doRcast();
            
    }

    private static void doFIFO() {
        ArrayList<FRBroadcast> fcasts = new ArrayList<FRBroadcast>();

        String localhost = "127.0.0.1";
        for (int i=0; i<MAX_PROCS; i++) {
            Process p = new Process(localhost, 6666+i);
            processes.add(p);
        }

        for (int i=0; i<MAX_PROCS; i++) {
            FRBroadcast b = new FRBroadcast();
            fcasts.add(b);
            Process p = processes.get(i);
            b.init(p);

            for (int j=0; j<MAX_PROCS; j++){
                if (i != j) {
                    Process pp = processes.get(j); 
                    b.addProcess(pp);
                }
            }
        }

        for (int i=0; i<MAX_PROCS; i++) 
            fcasts.get(i).rblisten(new BcastReceiver(i));

        for (int i=0; i<NUM_MSGS; i++)
            fcasts.get(0).rbroadcast(new Message(new Integer(i).toString(),
                                                 processes.get(0)));


        // Message m = new Message("OBS", processes.get(0));
        // m.makesObsolete(500);
        // try { Thread.sleep(500); }
        // catch (InterruptedException e) {System.out.println("sleep: "+e);}
        // int x[] = m.getObsoletedMessages();

        
        // for (int i=0; i<MAX_PROCS; i++) {
        //     fcasts.get(i).haltR();
        //     fcasts.get(i).halt();
        // }

        // for (int i=0; i<x.length; i++)
        //     System.out.println(x[i]);
        
        // System.exit(0);
        // fcasts.get(0).rbroadcast(m);
        
        for (int i=0; i<MAX_PROCS; i++) {
            // bcasts.get(i).haltR();
            // bcasts.get(i).halt();
        }
    }

    private static void doRcast() {
        ArrayList<RBroadcast> bcasts = new ArrayList<RBroadcast>();

        String localhost = "127.0.0.1";
        for (int i=0; i<MAX_PROCS; i++) {
            Process p = new Process(localhost, 6666+i);
            processes.add(p);
        }

        for (int i=0; i<MAX_PROCS; i++) {
            RBroadcast b = new RBroadcast();
            bcasts.add(b);
            Process p = processes.get(i);
            b.init(p);

            for (int j=0; j<MAX_PROCS; j++){
                if (i != j) {
                    Process pp = processes.get(j); 
                    b.addProcess(pp);
                }
            }
        }

        for (int i=0; i<MAX_PROCS; i++) {
            bcasts.get(i).rblisten(new BcastReceiver(i));
        }

        // for (int i=0; i<NUM_MSGS; i++)
        //     bcasts.get(0).rbroadcast(new Message(new Integer(i).toString(),
        //                                          processes.get(0)));
        for (int i=0; i<NUM_MSGS; i++)
            bcasts.get(2).rbroadcast(new Message(new Integer(i).toString(),
                                                 processes.get(2)));
        for (int i=0; i<MAX_PROCS; i++) {
            // bcasts.get(i).haltR();
            // bcasts.get(i).halt();
        }
    }
}
