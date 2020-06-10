package com.example.Client;

import org.junit.Test;

import java.util.PriorityQueue;

import Common.TimerStruct;

public class TimerStructTest {
    PriorityQueue<TimerStruct> timerQueue = new PriorityQueue<>();

    @Test
    public void compare(){
        timerQueue.add(new TimerStruct(, null, 1, ));
        timerQueue.add(new TimerStruct(, null, 2, ));
        timerQueue.add(new TimerStruct(, null, 3, ));
        timerQueue.add(new TimerStruct(, null, 4, ));

        while (true){
            TimerStruct ts = timerQueue.poll();
            if (ts == null) return;

            System.out.println(ts.timeToBeFired);
        }
    }
}
