package com.example.Client;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import Common.CollisionState;


public class EqualsTest {
    ArrayList<CollisionState> _array = new ArrayList<>();
    PlayerClient _p = new PlayerClient(0, 0, "he");
    PlayerClient _p2 = new PlayerClient(0, 0, "he");
    PlayerClient _p3 = new PlayerClient(0, 0, "he");

    @Before
    public void b(){
        _array.add(new CollisionState(){{
            other = _p;
        }});
        _array.add(new CollisionState(){{
            other = _p2;
        }});
        _array.add(new CollisionState(){{
            other = _p3;
        }});
    }

    @Test
    public void equal(){
        System.out.println(_array.contains(_p));
        System.out.println(_array.contains(_p2));
        System.out.println(_array.contains(_p3));
        System.out.println(_array.indexOf(_p));
        System.out.println(_array.indexOf(_p2));
        System.out.println(_array.indexOf(_p3));
    }
}
