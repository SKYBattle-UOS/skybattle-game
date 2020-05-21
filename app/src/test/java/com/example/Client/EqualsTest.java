package com.example.Client;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import Common.CollisionState;


public class EqualsTest {
    ArrayList<CollisionState> _array = new ArrayList<>();
    Player _p = new Player(0, 0, "he");
    Player _p2 = new Player(0, 0, "he");
    Player _p3 = new Player(0, 0, "he");

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
