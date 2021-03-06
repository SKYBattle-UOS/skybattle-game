package Common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class Collider {
    private HashMap<GameObject, LinkedList<CollisionState>> _collisions = new HashMap<>();
    private ArrayList<GameObject> _allGOs = new ArrayList<>();
    private ArrayList<CollisionState> _toRemove = new ArrayList<>();

    public void update(long ms){
        for (LinkedList<CollisionState> a : _collisions.values()){
            for (CollisionState c : a){
                c.isEnter = false;
                if (c.isExit)
                    _toRemove.add(c);
            }

            for (CollisionState r : _toRemove)
                a.remove(r);

            _toRemove.clear();
        }
    }

    public void registerNew(GameObject go){
        _allGOs.add(go);
        _collisions.putIfAbsent(go, new LinkedList<>());
    }

    public void remove(GameObject go){
        _allGOs.remove(go);
        _collisions.remove(go);

        for (LinkedList<CollisionState> a : _collisions.values()){
            while (a.remove(go));
        }
    }

    public void positionDirty(GameObject go){
        for (GameObject otherGO : _allGOs){
            if (otherGO == go) continue;

            LinkedList<CollisionState> list = _collisions.get(go);
            LinkedList<CollisionState> otherList = _collisions.get(otherGO);

            if (doesCollide(go, otherGO)){
                if (!list.contains(otherGO)){
                    list.add(new CollisionState(){
                        {
                            other = otherGO;
                            isEnter = true;
                            isExit = false;
                        }
                    });

                    otherList.add(new CollisionState(){
                        {
                            other = go;
                            isEnter = true;
                            isExit = false;
                        }
                    });
                }
            }
            else {
                if (list.contains(otherGO)){
                    list.get(list.indexOf(otherGO)).isExit = true;
                    otherList.get(otherList.indexOf(go)).isExit = true;
                }
            }
        }
    }

    private boolean doesCollide(GameObject go0, GameObject go1) {
        double[] pos0 = go0.getPosition();
        double[] pos1 = go1.getPosition();

        float dist = Util.distanceBetweenLatLon(pos0[0], pos0[1], pos1[0], pos1[1]);
        return dist < go0.getRadius() + go1.getRadius();
    }

    public Collection<CollisionState> getCollisions(GameObject go){
        return _collisions.get(go);
    }
}
